package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETMindestschwelle;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.SchwellenRepo;
import solutions.dmitrikonnov.einstufungstest.utils.AntwortBogenCheckedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Service checks the correctness of answers.
 * */

@Service
@AllArgsConstructor
public class ETAntwortenPruefer {

    private final ApplicationEventPublisher publisher;
    private final SchwellenRepo schwellenRepo;

    public ETErgebnisseDto checkBogen(ETAntwortBogenDto antwortBogen, ETAufgabenBogen cachedAufgabenBogen) {


        final Map<Integer,List<String>> cachedItemZuloesungen = cachedAufgabenBogen.getItemZuLoesungen();
        final ETErgebnisseDto ergebnisseDto = new ETErgebnisseDto();
        final Map<Integer, ETAufgabenNiveau> itemIdZuNiveau = cachedAufgabenBogen.getItemZuNiveau();
        final Long cachedBogenId = cachedAufgabenBogen.getAufgabenBogenId();
        final Integer cachedBogenHash = cachedAufgabenBogen.getAufgabenBogenHash();
        final Map<Integer, ArrayList<String>> itemHashZuAntwortMap = antwortBogen.getItemHashZuAntwortMap();
        final List<ETMindestschwelle> mindestSchwellen = schwellenRepo.findAllByOrderByNiveau();
        final List<ETAufgabenNiveau> richtigeLoesungenNachNiveauTemp = new ArrayList<>();

        mindestSchwellen.forEach(schwelle -> ergebnisseDto
                .getNiveauZurZahlRichtiger()
                .put(schwelle.getNiveau(),(short)0));

        ergebnisseDto.setAufgabenBogenHash(cachedAufgabenBogen.getAufgabenBogenHash());

        itemHashZuAntwortMap.forEach((hashedId, list) -> {
            var itemId = hashedId - cachedBogenHash;
            var cLoesungen = cachedItemZuloesungen.get(itemId);
            Boolean correct = cLoesungen.equals(list);
            ergebnisseDto.getIdZuRichtigkeitMap().put(itemId, correct);
            if(correct){
                richtigeLoesungenNachNiveauTemp.add(itemIdZuNiveau.get(itemId));
            }
            });

        ergebnisseDto.setZahlRichtigerAntworten((short)richtigeLoesungenNachNiveauTemp.size());
        ergebnisseDto.getRichtigeLoesungenNachNiveau().addAll(
                richtigeLoesungenNachNiveauTemp
                        .stream()
                        .sorted()
                        .collect(Collectors.toList()));
        publisher.publishEvent(new AntwortBogenCheckedEvent(this, cachedBogenId,ergebnisseDto.toString()));
        return new ETErgebnisseDto(ergebnisseDto);
    }

}