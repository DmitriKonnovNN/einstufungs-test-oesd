package solutions.dmitrikonnov.einstufungstest.utils;


import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;
import solutions.dmitrikonnov.einstufungstest.domainlayer.entities.ETSchwelle;

import java.util.function.BiFunction;

import static solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis.*;


public interface ResultsEvaluator extends BiFunction<ETSchwelle,Short, ETSchwellenErgebnis> {

    static ResultsEvaluator isAllCorrect(){
        return (schwelle, richtig)-> (richtig>=(schwelle.getMaximumSchwelle())) ? ALLE_RICHTIG : KEINE_RICHTIG;
    }
    static ResultsEvaluator isReached(){
        return  (schwelle, richtig)-> (richtig>schwelle.getMindestSchwelle()) ? ERREICHT: KEINE_RICHTIG;
    }
    static ResultsEvaluator isJustReached(){
        return  (schwelle, richtig)-> (richtig.equals(schwelle.getMindestSchwelle())) ? KNAPP_ERREICHT : KEINE_RICHTIG;
    }
    static ResultsEvaluator isNotReached(){
        return  (schwelle, richtig)-> (richtig <schwelle.getMindestSchwelle() && richtig >0)? NICHT_ERREICHT : KEINE_RICHTIG;
    }
    default ResultsEvaluator or (ResultsEvaluator other){
        return (schwelle, richtig) -> {
            ETSchwellenErgebnis result = this.apply(schwelle,richtig);
            return result.equals(KEINE_RICHTIG) ? other.apply(schwelle,richtig) : result;
        };
    }
}


