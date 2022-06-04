package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ETErgebnisseEvaluator {

    private final MindestSchwelleRepo mindestSchwelleRepo;
    private boolean noneCorrect(ETErgebnisseDto ergebnisse){
        return ergebnisse.getZahlRichtigerAntworten().equals(0);
    }
    public void evaluate(ETErgebnisseDto ergebnisse) {
        Boolean [] ignoreNextNext = new Boolean[1];
        ignoreNextNext[0] = false;
        if (noneCorrect(ergebnisse)){
            return;
        }
        List<String> sortedCorrectAnswers = ergebnisse.getRichtigeLoesungenNachNiveau()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        mindestSchwelleRepo.findAllOrderByNiveauAsc().forEach(record ->{
            long zahlRichtiger = sortedCorrectAnswers.stream().filter(niveau-> niveau.equals(record.getNiveau())).count();
            if (zahlRichtiger==0){
                ignoreNextNext[0]=true;

            }
        });
        //TODO: Bringe in Erfahrung,
        // i) nach welchem Algorythmus das Niveau eingeschätzt werden soll
        // ii) wie groß der Aufgaben-Pool ist
        // iii) u.v.a.m.
    }
}
