package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Aufgabe des Aufsetzers ist es, Aufgaben aus dem Pool für alle Niveaus und Fertigkeiten
 * im erforderlichen Umfang teilweise randomisiert zu wählen. Somit wird der Ursprungsaufgabenbogen aufgesetzt, der
 * anschließend zweimal reshuffelt wird.*/
@Service
@AllArgsConstructor
public class ETAufgabenAufsetzer {

    private final solutions.dmitrikonnov.einstufungstest.businesslayer.ETAufgabenRepo ETAufgabenRepo;
    private final ETAufgabenReshuffler aufgabenReshuffler;
    private final ETAntwortenReshuffler antwortenReshuffler;
    private final ETAufgabenRestricter aufgabenRestricter;
    public List<ETAufgabe> listeAufsetzen() {

        //TODO: Refactor the code down below by using Java Streams;
        List<ETAufgabe> aufgesetzteList = new ArrayList<>();
        for (ETAufgabenNiveau niveau: ETAufgabenNiveau.values()) {
            var aufgabenNotReshuffeld = ETAufgabenRepo.findAllByAufgabenNiveau(niveau);
            var aufgabenReshuffeld = aufgabenReshuffler.reshuffle(aufgabenNotReshuffeld);
            var aufgabenReshuffeldAndRestricted = aufgabenRestricter.restrict(aufgabenReshuffeld);
            var aufgabenWithReshuffeldAntworten = antwortenReshuffler.reshuffleAntworten(aufgabenReshuffeldAndRestricted);
            aufgesetzteList.addAll(aufgabenWithReshuffeldAntworten);
        }
        return aufgesetzteList;
    }
}
