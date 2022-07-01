package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAntwortBogenDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenBogen;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETEndResultForFE;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.ETErgebnisseConverterAndPersister;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ETAufgabenService {

    private final ETAufgabenAufsetzer aufsetzer;
    private final ETAntwortenPruefer pruefer;
    private final ETErgebnisseEvaluator evaluator;
    private final ETErgebnisseConverterAndPersister converterAndPersister;
    private final ETAufgabenBogenAufsetzer bogenAufsetzer;


    @Transactional
    public ETAufgabenBogen getAufgabenListe (){
        List<ETAufgabe> aufgesetzteListe = aufsetzer.listeAufsetzen();
        if(aufgesetzteListe.isEmpty()) return null;
        ETAufgabenBogen bogen = bogenAufsetzer.aufsetzen(aufgesetzteListe);
        return bogen;
    }

    @SneakyThrows
    @Transactional
    public ETEndResultForFE checkAntwortBogenAndGetTestErgebnisse (ETAntwortBogenDto antwortBogen, ETAufgabenBogen chachedAufgabenBogen){
        var ergebnisseDto = pruefer.checkBogen(antwortBogen,chachedAufgabenBogen);
        var ergebnisseDto1 = evaluator.evaluate(ergebnisseDto);
        var ergebnisseUUID = converterAndPersister.convertAndPersist(ergebnisseDto1);
        return ETEndResultForFE.builder()
                .erreichtesNiveau(ergebnisseDto1.getMaxErreichtesNiveau())
                .zahlRichtigerAntworten(ergebnisseDto1.getZahlRichtigerAntworten().toString())
                .id(ergebnisseUUID.get(2, TimeUnit.SECONDS))
                .build();
    }
}
