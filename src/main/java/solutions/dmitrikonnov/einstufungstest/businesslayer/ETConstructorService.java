package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import solutions.dmitrikonnov.einstufungstest.aws.s3.BucketName;
import solutions.dmitrikonnov.einstufungstest.aws.s3.S3FileStoreService;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETSchwellenConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETItem;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;
import solutions.dmitrikonnov.einstufungstest.exceptions.NotFoundException;
import solutions.dmitrikonnov.einstufungstest.exceptions.TaskNotFoundException;
import solutions.dmitrikonnov.einstufungstest.exceptions.ThresholdNotFoundException;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ETConstructorService {

    private final ETAufgabenRepo aufgabenRepo;
    private final SchwellenRepo schwellenRepo;
    private final S3FileStoreService s3store;

    public String updateAufgabeImageById (Integer id, MultipartFile file) {
        if(!aufgabenRepo.existsById(id))
            throw new NotFoundException(String.format("Keine Aufgabe mit id %d gefunden! Die Datei wurde nicht gespeichert.", id));
        if (file==null || file.isEmpty()) {
            throw new IllegalStateException("File to be uploaded is empty or not found!");
        }
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BucketName.AUFGABE_MEDIADATA.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());

        try {
            s3store.upload(path, fileName, Optional.of(metadata), file.getInputStream());
            StringJoiner joiner = new StringJoiner(":");
            joiner.add(fileName).add(path);
            aufgabenRepo.updateImageDataById(id,joiner.toString());
        } catch ( IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return fileName;
    }

    public byte[] downloadTodoImage(int id) {

        var aufgabe = aufgabenRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = aufgabe.getAufgabenInhalt().split(":");
        return s3store.download(fileAndPath[1],fileAndPath[0]);
    }

    public String deleteImageByAufgabenId(int id) {
        var a = aufgabenRepo.findById(id).orElseThrow(()-> {
            throw new NotFoundException("Keine Aufgabe mit id %d gefunden!");});
        var fileAndPath = a.getAufgabenInhalt().split(":");
        s3store.delete(fileAndPath[0]);
        a.setAufgabenInhalt("");
        aufgabenRepo.save(a);
        return String.format("Image for task #%d has been deleted",id);
    }


    public ETAufgabe addAufgabe(ETAufgabeConstructDTO aufgabe){
        if(aufgabe.getItems().isEmpty()){
            var aufgabeEntity = setUpLoneETAufgabeEntity(aufgabe);
            log.debug("Ready to save to DB: " + aufgabeEntity.toString());
            return aufgabenRepo.save(aufgabeEntity);
        }
        var aufgabeEntity = setUpLoneETAufgabeEntity(aufgabe);
        aufgabe.getItems().forEach(itemDto->
            aufgabeEntity.addItem(setUpLoneETItem(itemDto)));
        log.debug("Ready to save to DB: " + aufgabeEntity.toString());
        return aufgabeEntity;
    }

    public void addItemsToAufgabe (List<ETItemConstructDTO> items, Integer aufgabeId){
        var aufgabe = aufgabenRepo.findById(aufgabeId)
                .orElseThrow(()-> new TaskNotFoundException(aufgabeId));
        items.forEach(item->aufgabe.addItem(setUpLoneETItem(item)));
        aufgabenRepo.save(aufgabe);
    }

    public void deleteItemInAufgabe (Integer itemId, Integer aufgabeId) {

    }
    public void deleteAufgabeSamtItems (Integer aufgabeId) {

    }
    public List<ETAufgabe> findAllAufgaben(){
        return aufgabenRepo.findAll();
    }
    public List<ETAufgabe> findAllAufgabenByNiveau(ETAufgabenNiveau niveau){
        return aufgabenRepo.findAllByAufgabenNiveau(niveau);
    }
    public List<ETSchwelle> findAllSchwellen(){
        return schwellenRepo.findAll();
    }

    public ETSchwelle findSchwelleByNiveau(ETAufgabenNiveau niveau){
        return  schwellenRepo.findByNiveau(niveau)
                .orElseThrow(()-> new ThresholdNotFoundException(niveau));

    }

    public ETSchwelle addSchwelle(ETSchwellenConstructDTO schwelle) {

        getMaxSchwellenByNiveaus();
        return schwellenRepo.save(ETSchwelle.builder()
                .niveau(schwelle.getNiveau())
                .mindestSchwelle(schwelle.getMindestSchwelle().shortValue())
                .maximumSchwelle(schwelle.getMaximumSchwelle().shortValue())
                .build());
    }

    public Map<ETAufgabenNiveau,Short> getMaxSchwellenByNiveaus (){
        return schwellenRepo.findMaximumSchwellenByNiveaus();
    }

    public ETSchwelle updateSchwelle(ETSchwellenConstructDTO schwelle) {
        var entity = findSchwelleByNiveau(schwelle.getNiveau());
        entity.setMaximumSchwelle(schwelle.getMaximumSchwelle().shortValue());
        entity.setMindestSchwelle(schwelle.getMindestSchwelle().shortValue());
        return schwellenRepo.save(entity);
    }

    public void patchSchwelle(ETSchwellenConstructDTO schwelle) {
        if(schwellenRepo.existsByNiveau( schwelle.getNiveau())){
            schwellenRepo.updateByNiveau(
                    schwelle.getNiveau(),
                    schwelle.getMindestSchwelle().shortValue(),
                    schwelle.getMaximumSchwelle().shortValue());
        }
        else throw new ThresholdNotFoundException(schwelle.getNiveau());
    }

    private ETAufgabe setUpLoneETAufgabeEntity(ETAufgabeConstructDTO dto) {
        return ETAufgabe.builder()
                .aufgabenInhalt(dto.getAufgabenInhalt())
                .aufgabenNiveau(dto.getAufgabenNiveau())
                .aufgabenStellung(dto.getAufgabenStellung())
                .aufgabenTyp(dto.getAufgabenTyp())
                .frontEndType(dto.getFrontEndType())
                .gewichtung(dto.getGewichtung())
                .build();
    }

    private ETItem setUpLoneETItem(ETItemConstructDTO dto) {
        return ETItem.builder()
                .itemAufgabenInhalt(dto.getItemAufgabenInhalt())
                .moeglicheAntworten(Set.copyOf(dto.getMoeglicheAntworten()))
                .loesungen(dto.getLoesungen())
                .build();
    }
}
