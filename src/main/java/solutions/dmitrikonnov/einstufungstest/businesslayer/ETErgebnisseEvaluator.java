package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;
import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis.*;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;
import solutions.dmitrikonnov.einstufungstest.utils.TriFunction;
import solutions.dmitrikonnov.einstufungstest.utils.TriPredicate;


import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ETErgebnisseEvaluator {

    private final MindestSchwelleRepo mindestSchwelleRepo;

    private final BiPredicate <Integer,Integer> alleRichtig = (max, richtig) -> richtig.equals(max);
    private final BiPredicate<Integer,Integer> erreicht = (schwelle, richtig) -> richtig > schwelle;
    private final BiPredicate<Integer,Integer> knappErreicht = (schwelle,richtig) -> richtig.equals(schwelle);
    private final BiPredicate<Integer,Integer> nichtErreicht = (schwelle,richtig) -> richtig < schwelle && richtig > 0;
    private final BiPredicate<Integer,Integer> keineRichtig = (schwelle,richtig) -> richtig == 0;

    private final TriFunction<Integer, Integer, Integer, ETSchwellenErgebnis> evaluateLevel = (richtig, mindestSchwelle, maximum) -> {
        if(alleRichtig.test(maximum,richtig)) return ALLE_RICHTIG;
        if(erreicht.test(mindestSchwelle,richtig)) return ERREICHT;
        if(knappErreicht.test(mindestSchwelle,richtig)) return KNAPP_ERREICHT;
        if(nichtErreicht.test(mindestSchwelle,richtig)) return NICHT_ERREICHT;
        return KEINE_RICHTIG;
    };


    public ETErgebnisseDto evaluate(ETErgebnisseDto ergebnisse) {
        List<ETMindestschwelle> mindestSchwellen = mindestSchwelleRepo.findAllByOrderByNiveau();

        if (noneCorrect(ergebnisse)){
            mindestSchwellen.forEach(record -> setNachNiveauAlleFalsch(record.getNiveau(),ergebnisse.getNiveauZurZahlRichtiger()));
            ergebnisse.setMaxErreichtesNiveau(ETAufgabenNiveau.A0);
            return ergebnisse;
        }
        List<ETAufgabenNiveau> sortedCorrectAnswers = ergebnisse.getRichtigeLoesungenNachNiveau()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        mindestSchwellen.forEach(record ->
                countRichtigeJeNiveau(record.getNiveau(),sortedCorrectAnswers,ergebnisse.getNiveauZurZahlRichtiger()));

        var endGueltigesErgenis = evaluate2(ergebnisse,mindestSchwellen);

        return endGueltigesErgenis;
    }


    protected ETErgebnisseDto evaluate2 (ETErgebnisseDto ergebnisseDto, List<ETMindestschwelle> mindestSchwellen){

        var entrySetErgebnisse = ergebnisseDto.getNiveauZurZahlRichtiger().entrySet();

        // TODO: Check the performance of both implementations down below with respect to concurrent call;

        Map <ETAufgabenNiveau, ETSchwellenErgebnis> ergebnisMap = Collections.synchronizedMap(new HashMap<>());
        //Map <ETAufgabenNiveau,ETSchwellenErgebnis> ergebnisMap = new ConcurrentHashMap<>();


        mindestSchwellen.forEach(schwelle -> {
            ETAufgabenNiveau niveau = schwelle.getNiveau();
            List <ETSchwellenErgebnis> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
                    .map(naSet -> evaluateLevel.apply(naSet.getValue(),schwelle.getMindestSchwelle(), schwelle.getMaximumSchwelle()))
                    .collect(Collectors.toUnmodifiableList());
            ergebnisMap.put(niveau,list.get(0));

        } );
        defineMaxLevel(ergebnisseDto,ergebnisMap);
        return ergebnisseDto;
    }


    private void defineMaxLevel (ETErgebnisseDto ergebnisseDto, Map<ETAufgabenNiveau,ETSchwellenErgebnis> erreichteNiveausMap) {
        List <ETAufgabenNiveau> sortedNiveaus = erreichteNiveausMap.keySet().stream().sorted().collect(Collectors.toList());
        boolean keineRichtig = false;
        boolean nichtErreicht = false;
        ETAufgabenNiveau vorangehendesNiveau = ETAufgabenNiveau.A0;
        ETAufgabenNiveau aktuellErreicht = ETAufgabenNiveau.A0;
        for (ETAufgabenNiveau sortedNiveau : sortedNiveaus) {
            if (!keineRichtig && !nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(KEINE_RICHTIG)) {
                    keineRichtig = true;
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(NICHT_ERREICHT)) {
                    nichtErreicht = true;
                    vorangehendesNiveau = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(sortedNiveau); // STRICT MODE
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix2(sortedNiveau);
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = sortedNiveau;
                    continue;
                }
            }
            if (keineRichtig) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix2(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(vorangehendesNiveau);
                    keineRichtig = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)){
                    keineRichtig = false;
                    continue;
                }
                else break;
            }
            if (nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ALLE_RICHTIG)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(sortedNiveau);
                    nichtErreicht = false;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(KNAPP_ERREICHT)) {
                    aktuellErreicht = ETAufgabenNiveau.getNiveauMitPostfix1(vorangehendesNiveau);
                    nichtErreicht = false;
                    break;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ERREICHT)) {
                    aktuellErreicht = vorangehendesNiveau;
                    nichtErreicht = false;
                } else break;
            }
        }
        ergebnisseDto.setMaxErreichtesNiveau(aktuellErreicht);
    }

    private boolean noneCorrect(ETErgebnisseDto ergebnisse){
        return ergebnisse.getZahlRichtigerAntworten().equals(0);
    }

    private void setNachNiveauAlleFalsch (ETAufgabenNiveau niveau, Map <ETAufgabenNiveau,Integer> map) {
        map.put(niveau,0);
    }

    private void countRichtigeJeNiveau(ETAufgabenNiveau niveau, List<ETAufgabenNiveau> answers, Map <ETAufgabenNiveau,Integer> map) {
        Integer richtige = answers.stream().filter(niveau::equals).mapToInt(value -> 1).sum();

        map.put(niveau,richtige);
    }
}
