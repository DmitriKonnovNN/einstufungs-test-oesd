package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETSchwellenConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETItem;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETMindestschwelle;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETAufgabeConstructDTO;
import solutions.dmitrikonnov.einstufungstest.domainlayer.construct.ETItemConstructDTO;
import solutions.dmitrikonnov.einstufungstest.exceptions.ThresholdNotFoundException;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ETConstructorService {
    private final static String SCHWELLE_NOT_FOUND_MSG = "Threshold for level %s not found!\n" +
            "Keine Schwelle fürs Niveau %s gefunden!";
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
    public List<ETMindestschwelle> findAllSchwellen(){
        return schwellenRepo.findAll();
    }
    public ETSchwelle findSchwelleByNiveau(ETAufgabenNiveau niveau){
       return schwellenRepo.findByNiveau(niveau)
               .orElseThrow(()-> new ThresholdNotFoundException(String.format(SCHWELLE_NOT_FOUND_MSG,niveau,niveau)));
    }

    public ETMindestschwelle addSchwelle(ETSchwellenConstructDTO schwelle) {

        getMaxSchwellenByNiveaus();
        return schwellenRepo.save(ETMindestschwelle.builder()
                .niveau(schwelle.getNiveau())
                .mindestSchwelle(schwelle.getMindestSchwelle().shortValue())
                .maximumSchwelle(schwelle.getMaximumSchwelle().shortValue())
                .build());
    }

    public void getMaxSchwellenByNiveaus (){
        var result = schwellenRepo.findMaximumSchwellenByNiveaus();
        log.error("alle Schwellen:{}", result);
    }
}
