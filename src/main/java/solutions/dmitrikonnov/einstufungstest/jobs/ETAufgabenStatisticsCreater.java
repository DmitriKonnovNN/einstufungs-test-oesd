package solutions.dmitrikonnov.einstufungstest.jobs;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.businesslayer.ETItemRepo;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETErgebnisse;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseRepo;

import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor

public class ETAufgabenStatisticsCreater {
    private final ETErgebnisseRepo ergebnisseRepo;
    private final ETItemRepo itemRepo;

//TODO: test 3 set of data base calls below: Which one is faster?

    void incrementCorrectAnswersInItems(){
       var ergebnisse = ergebnisseRepo.findAllByCreatedOnBefore(new Date());

       ergebnisse.stream()
               .map(ETErgebnisse::getIdZuRichtigkeitMap)
               .flatMap(maps-> maps.entrySet().stream())
               .filter(Map.Entry::getValue)
               .forEach(entry -> itemRepo.updateCounterCorrectAnswers(entry.getKey()));

    }

    void incrementCorrectAnswersInItems2(){
        var ergebnisse = ergebnisseRepo.findAllMapsItemCorrectness(new Date());

        ergebnisse.entrySet().stream()
                .filter(Map.Entry::getValue).
                forEach(entry -> itemRepo.updateCounterCorrectAnswers(entry.getKey()));

    }

    void incrementCorrectAnswersInItems3(){
        var ids = ergebnisseRepo.findAllIdsOfCorrectAnsweredItems(new Date());
        ids.forEach(itemRepo::updateCounterCorrectAnswers);
    }


}
