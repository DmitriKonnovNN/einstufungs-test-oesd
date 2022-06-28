package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;
import solutions.dmitrikonnov.einstufungstest.utils.AntwortBogenCheckedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Service checks the correctness of answers.
 * */

@Service
@AllArgsConstructor
public class ETAntwortenPruefer {

    private final ApplicationEventPublisher publisher;
    private final MindestSchwelleRepo mindestSchwelleRepo;

    public ETErgebnisseDto checkBogen(ETAntwortBogenDto antwortBogen, ETAufgabenBogen cachedAufgabenBogen) {


        final Map<Integer,List<String>> cachedItemZuloesungen = cachedAufgabenBogen.getItemZuLoesungen();
        final ETErgebnisseDto ergebnisseDto = new ETErgebnisseDto();
        final Map<Integer, ETAufgabenNiveau> itemIdZuNiveau = cachedAufgabenBogen.getItemZuNiveau();
        final Long cachedBogenId = cachedAufgabenBogen.getAufgabenBogenId();
        final Integer cachedBogenHash = cachedAufgabenBogen.getAufgabenBogenHash();
        final Map<Integer, ArrayList<String>> itemHashZuAntwortMap = antwortBogen.getItemHashZuAntwortMap();
        final List<ETMindestschwelle> mindestSchwellen = mindestSchwelleRepo.findAllByOrderByNiveau();

        mindestSchwellen.forEach(schwelle -> ergebnisseDto
                .getNiveauZurZahlRichtiger()
                .put(schwelle.getNiveau(),0));

        ergebnisseDto.setAufgabenBogenHash(cachedAufgabenBogen.getAufgabenBogenHash());

        itemHashZuAntwortMap.forEach((hashedId, list) -> {
            var itemId = hashedId - cachedBogenHash;
            var cLoesungen = cachedItemZuloesungen.get(itemId);
            Boolean correct = cLoesungen.equals(list);
            ergebnisseDto.getIdZuRichtigkeitMap().put(itemId, correct);
            if(correct){
                ergebnisseDto.getRichtigeLoesungenNachNiveau().add(itemIdZuNiveau.get(itemId));
            }
            });

        ergebnisseDto.setZahlRichtigerAntworten(ergebnisseDto.getRichtigeLoesungenNachNiveau().size());
        publisher.publishEvent(new AntwortBogenCheckedEvent(this, cachedBogenId,ergebnisseDto.toString()));
        return new ETErgebnisseDto(ergebnisseDto);
    }

}