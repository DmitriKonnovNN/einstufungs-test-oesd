package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETSchwellenConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETItem;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;
import solutions.dmitrikonnov.einstufungstest.exceptions.TaskNotFoundException;
import solutions.dmitrikonnov.einstufungstest.exceptions.ThresholdNotFoundException;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ETConstructorService {

    private final ETAufgabenRepo aufgabenRepo;
    private final ETItemRepo itemRepo;
    private final SchwellenRepo schwellenRepo;


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
