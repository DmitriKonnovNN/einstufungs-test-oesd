package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseConverterAndPersister;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ETAufgabenService {


    private final ETAufgabenRepo aufgabenRepo;
    private final ETAufgabenAufsetzer aufsetzer;
    private final ETAntwortenPruefer pruefer;
    private final ETErgebnisseEvaluator evaluator;
    private final ETErgebnisseConverterAndPersister converterAndPersister;
    private final ETAufgabenBogenAufsetzer bogenAufsetzer;


    public void addAufgabe (ETAufgabe aufgabe) {
        aufgabenRepo.save(aufgabe);

    }

    public ETAufgabenBogen getAufgabenListe (){
        List<ETAufgabe> aufgesetzteListe = aufsetzer.listeAufsetzen();
        ETAufgabenBogen bogen = bogenAufsetzer.aufsetzen(aufgesetzteListe);
        return bogen;
    }

    @SneakyThrows
    public ETEndResultForFE checkAntwortBogenAndGetTestErgebnisse (ETAntwortBogenDto antwortBogen, ETAufgabenBogen chachedAufgabenBogen){
        var ergebnisseDto = pruefer.checkBogen(antwortBogen,chachedAufgabenBogen);
        var ergebnisseDto1 = evaluator.evaluate(ergebnisseDto);
        var ergebnisseUUID = converterAndPersister.convertAndPersist(ergebnisseDto1);
        return ETEndResultForFE.builder()
                .erreichtesNiveau(ergebnisseDto1.getMaxErreichtesNiveau())
                .zahlRichtigerAntworten(ergebnisseDto1.getZahlRichtigerAntworten())
                .id(ergebnisseUUID.get(2, TimeUnit.SECONDS))
                .build();
    }
}
