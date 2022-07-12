package solutions.dmitrikonnov.einstufungstest.utils;

import solutions.dmitrikonnov.einstufungstest.domainlayer.ETSchwellenErgebnis;

import java.util.Objects;

@FunctionalInterface
public interface BiPredicateToResult<T,U> {

    ETSchwellenErgebnis test(T t, U u);


    default BiPredicateToResult<T, U> or(BiPredicateToResult<? super T, ? super U> other) {
        Objects.requireNonNull(other);
        return (T t, U u) -> (test(t, u).equals(ETSchwellenErgebnis.KEINE_RICHTIG))? other.test(t, u): test(t,u);
    }
}
