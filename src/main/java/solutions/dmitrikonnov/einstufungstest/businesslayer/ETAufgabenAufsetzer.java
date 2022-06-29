package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabe;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Aufgabe des Aufsetzers ist es, Aufgaben aus dem Pool für alle Niveaus und Fertigkeiten
 * im erforderlichen Umfang teilweise randomisiert zu wählen. Somit wird der Ursprungsaufgabenbogen aufgesetzt, der
 * anschließend zweimal reshuffelt wird.*/
@Service
@AllArgsConstructor
public class ETAufgabenAufsetzer {

    private final ETAufgabenRepo ETAufgabenRepo;
    private final ETAufgabenReshuffler aufgabenReshuffler;
    private final ETAntwortenReshuffler antwortenReshuffler;
    private final ETAufgabenRestricter aufgabenRestricter;
    private final SchwellenRepo schwellenRepo;

    //TODO: try out parallstream() instead of just stream()
    public List<ETAufgabe> listeAufsetzen() {
        var maxSchwellenMap = schwellenRepo.findMaximumSchwellenByNiveaus();
        return ETAufgabenRepo.findAllByOrderByAufgabenNiveauAsc()
                .stream()
                .collect(Collectors.groupingBy(ETAufgabe::getAufgabenNiveau))
                .values().stream().map(aufgabenReshuffler::reshuffle)
                .map(aufgaben->aufgabenRestricter.restrict(aufgaben,maxSchwellenMap))
                .map(antwortenReshuffler::reshuffleAntworten)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }
}
