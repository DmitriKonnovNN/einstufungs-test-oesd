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
import solutions.dmitrikonnov.einstufungstest.exceptions.ThresholdNotFoundException;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ETConstructorService {

    private final ETAufgabenRepo aufgabenRepo;
    private final ETItemRepo itemRepo;
    private final SchwellenRepo schwellenRepo;


    public ETAufgabe addAufgabe(ETAufgabeConstructDTO aufgabe){
      return null;
    }

    public ETItem addItemToAufgabe (ETItemConstructDTO item, Integer aufgabeId){
        return null;
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
       return schwellenRepo.findByNiveau(niveau)
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
}
