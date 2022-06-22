package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseConverterAndPersister;

import java.util.List;

@Service
@AllArgsConstructor
public class ETAufgabenService {


    private final ETAufgabenRepo aufgabenRepo;
    private final ETAufgabenAufsetzer aufsetzer;
    private final ETAntwortenPruefer pruefer;
    private final ETErgebnisseEvaluator evaluator;
    private final ETErgebnisseConverterAndPersister converterAndPersister;


    public void addAufgabe (ETAufgabe aufgabe) {

        aufgabenRepo.save(aufgabe);

    }



    public ETAufgabenBogen getAufgabenListe (){
        List<ETAufgabe> aufgesetzteListe = aufsetzer.listeAufsetzen();
        return ETAufgabenBogen.builder().aufgabenListe(aufgesetzteListe).build();
    }


    public ETEndResultForFE checkAntwortBogenAndGetTestErgebnisse (ETAntwortBogenDto antwortBogen, ETAufgabenBogen chachedAufgabenBogen){
        var ergebnisseDto = pruefer.checkBogen(antwortBogen,chachedAufgabenBogen);
        var ergebnisseDto1 = evaluator.evaluate(ergebnisseDto);
        converterAndPersister.convertAndPersist(ergebnisseDto1);
        return ETEndResultForFE.builder()
                .id(ergebnisseDto.getId())
                .erreichtesNiveau(ergebnisseDto1.getMaxErreichtesNiveau())
                .zahlRichtigerAntworten(ergebnisseDto1.getZahlRichtigerAntworten())
                .build();
    }
}
