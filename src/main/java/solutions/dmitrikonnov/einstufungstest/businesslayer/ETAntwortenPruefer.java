package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.List;
import java.util.function.Predicate;

/**
 * The Service checks the correctness of answers.
 * */

@Service
@AllArgsConstructor
public class ETAntwortenPruefer {


    private final MindestSchwelleRepo mindestSchwelleRepo;

    private void evaluate (ETErgebnisseDto ergebnisseDto, ETAufgabe aufgabe){
        ETAufgabenNiveau niveau = aufgabe.getAufgabenNiveau();
        Integer count = ergebnisseDto.getNiveauZurZahlRichtiger().get(niveau);
        count++;
        ergebnisseDto.getNiveauZurZahlRichtiger().put(aufgabe.getAufgabenNiveau(),count);

    }
    private void evaluate2 (ETErgebnisseDto ergebnisseDto, Iterable<ETMindestschwelle> mindestSchwellen){
        int [] ignoreNextNext = new int[1];


        Predicate <ETSchwelle> erreicht = schwelle -> schwelle.getMindestSchwelle()<ergebnisseDto.getNiveauZurZahlRichtiger().get(schwelle.getNiveau());
        Predicate <ETSchwelle> knappErreicht = schwelle -> schwelle.getMindestSchwelle().equals(ergebnisseDto.getNiveauZurZahlRichtiger().get(schwelle.getNiveau()));
        Predicate <ETSchwelle> nichtErreicht = schwelle -> schwelle.getMindestSchwelle()>ergebnisseDto.getNiveauZurZahlRichtiger().get(schwelle.getNiveau());
        Predicate <ETSchwelle> keineRichtigen = schwelle -> ergebnisseDto.getNiveauZurZahlRichtiger().get(schwelle.getNiveau())==0;


        mindestSchwellen.forEach(schwelle -> {
            if (ignoreNextNext[0]>=2) {}
            else {
                if (keineRichtigen.test(schwelle))ignoreNextNext[0]=+2;
                if (knappErreicht.test(schwelle))ignoreNextNext[0]++;
            }

        });
    }



    public ETErgebnisseDto checkBogen(ETAntwortBogenDto antwortBogen, ETAufgabenBogen cachedAufgabenBogen) {

        List<ETAufgabe> cachedAufgaben = cachedAufgabenBogen.getAufgabenListe();
        ETErgebnisseDto ergebnisseDto = new ETErgebnisseDto();
        Iterable<ETMindestschwelle> mindestSchwellen = mindestSchwelleRepo.findAllOrderByNiveauAsc();
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
                        evaluate(ergebnisseDto,aufgabe);
                    }
                }
            });
        });

        evaluate2(ergebnisseDto,mindestSchwellen);




        return ergebnisseDto;
    }
}