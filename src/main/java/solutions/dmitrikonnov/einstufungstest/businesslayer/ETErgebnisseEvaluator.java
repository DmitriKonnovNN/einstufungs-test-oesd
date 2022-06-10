package solutions.dmitrikonnov.einstufungstest.businesslayer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETAufgabenNiveau;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETErgebnisseDto;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETMindestschwelle;
import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;
import solutions.dmitrikonnov.einstufungstest.persistinglayer.MindestSchwelleRepo;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ETErgebnisseEvaluator {

    private final MindestSchwelleRepo mindestSchwelleRepo;
    private final BiPredicate<Integer,Integer> erreicht = (schwelle, richtig) -> richtig > schwelle;
    private final BiPredicate<Integer,Integer> knappErreicht = (schwelle,richtig) -> richtig.equals(schwelle);
    private final BiPredicate<Integer,Integer> nichtErreicht = (schwelle,richtig) -> richtig < schwelle && richtig > 0;
    private final BiPredicate<Integer,Integer> keineRichtig = (schwelle,richtig) -> richtig == 0;


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



        BiFunction<Integer, Integer, ETSchwellenErgebnis> evaluateLevel = (richtig, mindestSchwelle) -> {

            if(erreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.ERREICHT;
            if(knappErreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.KNAPP_ERREICHT;
            if(nichtErreicht.test(mindestSchwelle,richtig)) return ETSchwellenErgebnis.NICHT_ERREICHT;
            return ETSchwellenErgebnis.KEINE_RICHTIG;
        };


        mindestSchwellen.forEach(schwelle -> {
            ETAufgabenNiveau niveau = schwelle.getNiveau();
            List <ETSchwellenErgebnis> list = entrySetErgebnisse.stream().filter(naSet -> naSet.getKey().equals(niveau))
                    .map(naSet -> evaluateLevel.apply(naSet.getValue(),schwelle.getMindestSchwelle()))
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
        ETAufgabenNiveau aktuellErreicht = ETAufgabenNiveau.A0;
        for (ETAufgabenNiveau sortedNiveau : sortedNiveaus) {
            if (!keineRichtig && !nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.KEINE_RICHTIG)) {
                    keineRichtig = true;
                    nichtErreicht = true;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.NICHT_ERREICHT)) {
                    nichtErreicht = true;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.KNAPP_ERREICHT)) {
                    aktuellErreicht = sortedNiveau;
                    continue;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.ERREICHT)) {
                    aktuellErreicht = sortedNiveau;
                    continue;
                }
            }
            if (keineRichtig) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.ERREICHT)) {
                    aktuellErreicht = sortedNiveau;
                    keineRichtig = false;
                    continue;
                } else break;
            }
            if (nichtErreicht) {
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.KNAPP_ERREICHT)) {
                    aktuellErreicht = sortedNiveau;
                    break;
                }
                if (erreichteNiveausMap.get(sortedNiveau).equals(ETSchwellenErgebnis.ERREICHT)) {
                    aktuellErreicht = sortedNiveau;
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
