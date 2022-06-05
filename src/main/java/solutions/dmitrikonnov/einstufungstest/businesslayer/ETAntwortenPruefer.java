package solutions.dmitrikonnov.einstufungstest.businesslayer;

import ch.qos.logback.classic.turbo.MDCValueLevelPair;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.*;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private void evaluate2 (ETErgebnisseDto ergebnisseDto, List<ETMindestschwelle> mindestSchwellen){

        var entrySetErgebnisse = ergebnisseDto.getNiveauZurZahlRichtiger().entrySet();

        // TODO: Check the performance of both implementations down below with respect to concurrent call;

        Map <ETAufgabenNiveau,ETSchwellenErgebnis> ergebnisMap = Collections.synchronizedMap(new HashMap<>());
        //Map <ETAufgabenNiveau,ETSchwellenErgebnis> ergebnisMap = new ConcurrentHashMap<>();

        BiPredicate<Integer,Integer> erreicht = (schwelle,richtig) -> richtig > schwelle;
        BiPredicate<Integer,Integer> knappErreicht = (schwelle,richtig) -> richtig.equals(schwelle);
        BiPredicate<Integer,Integer> nichtErreicht = (schwelle,richtig) -> richtig < schwelle && richtig > 0;

        BiFunction<Integer, Integer, ETSchwellenErgebnis> evaluateLevel = (richtig,mindestSchwelle) -> {

            if(erreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.ERREICHT;
            if(knappErreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.KNAPP_ERREICHT;
            if(nichtErreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.NICHT_ERREICHT;
            return ETSchwellenErgebnis.KEINE_RICHTIG;
        };


   /*     for (ETMindestschwelle schwelle : mindestSchwellen) {

            ETAufgabenNiveau niveau = schwelle.getNiveau();
            List <ETSchwellenErgebnis> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
                        .map(naSet -> evaluateLevel.apply(naSet.getValue(),schwelle.getMindestSchwelle()))
                        .collect(Collectors.toUnmodifiableList());
            ergebnisMap.put(niveau,list.get(0));
        }*/

        mindestSchwellen.forEach(schwelle -> {
            ETAufgabenNiveau niveau = schwelle.getNiveau();
            List <ETSchwellenErgebnis> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
                    .map(naSet -> evaluateLevel.apply(naSet.getValue(),schwelle.getMindestSchwelle()))
                    .collect(Collectors.toUnmodifiableList());
            ergebnisMap.put(niveau,list.get(0));

        } );

    }




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
                        evaluate(ergebnisseDto,aufgabe);
                    }
                }
            });
        });

        evaluate2(ergebnisseDto,mindestSchwellen);




        return ergebnisseDto;
    }
}