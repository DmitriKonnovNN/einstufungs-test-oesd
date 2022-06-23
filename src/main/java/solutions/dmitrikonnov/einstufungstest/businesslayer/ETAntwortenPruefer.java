package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.List;

/**
 * The Service checks the correctness of answers.
 * */

@Service
@AllArgsConstructor
public class ETAntwortenPruefer {

    private final MindestSchwelleRepo mindestSchwelleRepo;

    public ETErgebnisseDto checkBogen(ETAntwortBogenDto antwortBogen, ETAufgabenBogen cachedAufgabenBogen) {

        List<ETAufgabe> cachedAufgaben = cachedAufgabenBogen.getAufgabenListe();
        ETErgebnisseDto ergebnisseDto = new ETErgebnisseDto();

        List<ETMindestschwelle> mindestSchwellen = mindestSchwelleRepo.findAllByOrderByNiveau();

        mindestSchwellen.forEach(schwelle -> ergebnisseDto
                .getNiveauZurZahlRichtiger()
                .put(schwelle.getNiveau(),0));

        ergebnisseDto.setAufgabenBogenHash(cachedAufgabenBogen.getAufgabenBogenHash());

        var cachedBogenHash = cachedAufgabenBogen.getAufgabenBogenHash();
        var aufgabenHashZuAntwortMap = antwortBogen.getAufgabenHashZuAntwortMap();

        //TODO: Die Liste unten soll überprüft werden, ob sie mehrere Elemente hat. Wenn ja,
        // i) soll sichergestellt werden, ob alle Elemente richtig sein sollen oder
        // ii) ob JEDES Element als EINE richtige Antwort gilt.
        aufgabenHashZuAntwortMap.forEach((hashedId, list) -> {
            var antwId = hashedId - cachedBogenHash;
            cachedAufgaben.forEach(aufgabe -> {
                if (aufgabe.getAufgabeId().equals(antwId)){
                    aufgabe.getItems().forEach(item -> {
                        Boolean correct = item.getLoesungen().equals(list);
                        ergebnisseDto.getIdZuRichtigkeitMap().put(antwId, correct);
                        if (correct){
                            ergebnisseDto.getRichtigeLoesungenNachNiveau().add(aufgabe.getAufgabenNiveau());
                        }
                    });
                }
            });
        });
        ergebnisseDto.setZahlRichtigerAntworten(ergebnisseDto.getRichtigeLoesungenNachNiveau().size());

        return new ETErgebnisseDto(ergebnisseDto);
    }

}