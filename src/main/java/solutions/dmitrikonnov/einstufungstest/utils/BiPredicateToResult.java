package solutions.dmitrikonnov.einstufungstest.utils;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;

import java.util.Objects;

@FunctionalInterface
@Deprecated
public interface BiPredicateToResult<T,U> {

    ETSchwellenErgebnis test(T t, U u);



    default BiPredicateToResult<T, U> or(BiPredicateToResult<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> (test(t, u).equals(ETSchwellenErgebnis.KEINE_RICHTIG))? other.test(t, u): test(t,u);
    }

       /*                 .map(naSet -> allCorrect
                             .or(reached)
                             .or(almostReached)
                             .or(notReached)
                             .test(schwelle,naSet.getValue()))*/

    /*

    private final BiPredicateToResult<ETSchwelle,Short> allCorrect =
            (schwelle, richtig)-> (richtig>=(schwelle.getMaximumSchwelle()))?ALLE_RICHTIG:KEINE_RICHTIG;
    private final BiPredicateToResult<ETSchwelle,Short> reached =
            (schwelle, richtig)-> (richtig>schwelle.getMindestSchwelle())?ERREICHT:KEINE_RICHTIG;
    private final BiPredicateToResult<ETSchwelle,Short> almostReached =
            (schwelle, richtig)-> (richtig.equals(schwelle.getMindestSchwelle()))?KNAPP_ERREICHT:KEINE_RICHTIG;
    private final BiPredicateToResult<ETSchwelle,Short> notReached =
            (schwelle, richtig)-> (richtig <schwelle.getMindestSchwelle() && richtig >0)?NICHT_ERREICHT:KEINE_RICHTIG;
*/

}
