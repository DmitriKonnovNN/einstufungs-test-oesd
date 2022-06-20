package solutions.dmitrikonnov.einstufungstest.utils;

import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the three-arity specialization of {@link Function}.
 *
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <Y> the type of the third argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface TriFunction <T,U,Y,R> {
    R apply(T t, U u,Y y);
}
