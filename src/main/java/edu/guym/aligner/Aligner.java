package edu.guym.aligner;

import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.impl.AlignerBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Base interface for aligner implementations.
 * <br/>
 * The factory methods support two different aligners - Levenshtein and Damerau Levenshtein, where levenshtein is basically Damerau without transpositions.
 * <br/>
 * <br/>
 * The aligner can be parameterized with the following:
 * <ol>
 * <li>A custom equality function - determines if two items are equal. If none is supplied, the object's {@code equals} method is used.</li>
 * <li>A custom comparator function - determines if two sub lists are transposable. If you are using a Levenshtein aligner than transposition is not supported.
 * If using Damerau and no comparator is supplied, a natural order comparator is used and you must use a generic type that implements {@link Comparable}.</li>
 * <li>A custom substitution cost function. If none is supplied, the default cost is 1.</li>
 *</ol>
 *
 * @param <T> the type of elements this aligner supports.
 */
public interface Aligner<T> {

    Alignment<T> align(List<T> source, List<T> target);

    /**
     * Get a new levenshtein aligner using {@code T::equals} as the equalizer.
     */
    static <T> Aligner<T> levenshtein() {
        return new AlignerBuilder<T>()
                .setEqualizer(T::equals)
                .build();
    }

    /**
     * Get a new levenshtein aligner with a custom {@code equals} delegate function.
     * @param equalizer a predicate to determine if two items are equal.
     */
    static <T> Aligner<T> levenshtein(BiPredicate<T, T> equalizer) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .build();
    }

    /**
     * Get a new levenshtein aligner with a custom {@code equals} delegate function and substitution cost.
     * @param equalizer a predicate to determine if two items are equal.
     * @param substituteCost a bifunction that returns the cost of a substitute between two given items.
     */
    static <T> Aligner<T> levenshtein(BiPredicate<T, T> equalizer,
                                      BiFunction<T, T, Double> substituteCost) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setSubstituteCost(substituteCost)
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner using {@code T::equals} as the equalizer and a natural order comparator.
     * <br/>
     * Param T must extend {@link Comparable}.
     */
    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein() {
        return new AlignerBuilder<T>()
                .setEqualizer(T::equals)
                .setComparator(Comparator.naturalOrder())
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function.
     * <br/>
     * Param T must extend {@link Comparable}.
     * @param equalizer a predicate to determine if two items are equal.
     */
    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein(BiPredicate<T, T> equalizer) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setComparator(Comparator.naturalOrder())
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function and comparator.
     * @param equalizer a predicate to determine if two items are equal.
     * @param comparator a {@link Comparator} used to determine if two lists are transposable.
     */
    static <T> Aligner<T> damerauLevenshtein(BiPredicate<T, T> equalizer,
                                             Comparator<T> comparator) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function, comparator and substitution cost function.
     * @param equalizer a predicate to determine if two items are equal.
     * @param comparator a {@link Comparator} used to determine if two lists are transposable.
     * @param substituteCost a bifunction that returns the cost of a substitute between two given items.
     */
    static <T> Aligner<T> damerauLevenshtein(BiPredicate<T, T> equalizer,
                                             Comparator<T> comparator,
                                             BiFunction<T, T, Double> substituteCost) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .setSubstituteCost(substituteCost)
                .build();
    }

}
