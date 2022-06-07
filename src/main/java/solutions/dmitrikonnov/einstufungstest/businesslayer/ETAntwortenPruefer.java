package solutions.dmitrikonnov.einstufungstest.businesslayer;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.*;

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
        List<ETMindestschwelle> mindestSchwellen = mindestSchwelleRepo.findAllOrderByNiveauAsc();

        mindestSchwellen.forEach(schwelle -> ergebnisseDto
                .getNiveauZurZahlRichtiger()
                .put(schwelle.getNiveau(),0));

        ergebnisseDto.setAufgabenBogenHash(cachedAufgabenBogen.getAufgabenBogenHash());

        var cachedBogenHash = cachedAufgabenBogen.getAufgabenBogenHash();
        var aufgabenHashZuAntwortMap = antwortBogen.getAufgabenHashZuAntwortMap();

        aufgabenHashZuAntwortMap.forEach((hashedId, list) -> {
            var antwId = cachedBogenHash - hashedId;
            cachedAufgaben.forEach(aufgabe -> {

                if (aufgabe.getAufgabeId().equals(antwId)){
                    Boolean correct = aufgabe.getLoesungen().equals(list);
                    ergebnisseDto.getIdZuRichtigkeitMap().put(antwId, correct);
                    if (correct){
                        ergebnisseDto.getRichtigeLoesungenNachNiveau().add(aufgabe.getAufgabenNiveau());
                        /*evaluate(ergebnisseDto,aufgabe.getAufgabenNiveau());*/
                    }
                }
            });
        });
        ergebnisseDto.setZahlRichtigerAntworten(ergebnisseDto.getRichtigeLoesungenNachNiveau().size());
        return ergebnisseDto;
    }

  /*  private void evaluate (ETErgebnisseDto ergebnisseDto, ETAufgabenNiveau niveau){
        Integer count = ergebnisseDto.getNiveauZurZahlRichtiger().get(niveau);
        count++;
        ergebnisseDto.getNiveauZurZahlRichtiger().put(niveau,count);

    }*/
}