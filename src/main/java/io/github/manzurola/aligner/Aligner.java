package io.github.manzurola.aligner;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Aligner. <br> The factory methods support
 * two different aligners - Levenshtein and Damerau Levenshtein, where
 * levenshtein is basically Damerau without transpositions. <br> <br> The
 * aligner can be parameterized with the following:
 * <ol>
 * <li>A custom equality function - determines if two items are equal. If
 * none is supplied, the object's {@code
 * equals} method is used.</li>
 * <li>A custom comparator function - determines if two sub lists are
 * transposable. If you are using a Levenshtein
 * aligner than transposition is not supported.
 * If using Damerau and no comparator is supplied, a natural order comparator
 * is used and you must use a generic type
 * that implements {@link Comparable}.</li>
 * <li>A custom substitution cost function. If none is supplied, the default
 * cost is 1.</li>
 * </ol>
 *
 * @param <T> the type of elements this aligner supports.
 */
public final class Aligner<T> {

    private final Algorithm<T> algorithm;

    public Aligner(Algorithm<T> algorithm) {
        this.algorithm = Objects.requireNonNull(algorithm);
    }

    public Alignment<T> align(List<T> source, List<T> target) {
        return algorithm.align(
            Objects.requireNonNull(source),
            Objects.requireNonNull(target)
        );
    }

    /**
     * Get a new builder to create a custom aligner based on the damerau
     * levenshtein algorithm. The builder is instantiated with default DL values
     * for costs, an equalizer that uses T::equals and a null comparator (=no
     * transpose).
     */
    public static <T> DamerauAlignerBuilder<T> builder() {
        return new DamerauAlignerBuilder<>();
    }

    /**
     * Get a new levenshtein aligner using {@code T::equals} as the equalizer.
     */
    public static <T> Aligner<T> levenshtein() {
        return Aligner.<T>builder()
            .setEquals(T::equals)
            .build();
    }

    /**
     * Get a new damerau-levenshtein aligner using {@code T::equals} as the
     * equalizer and a natural order comparator.
     * <br> Param T must extend {@link Comparable}.
     */
    public static <T extends Comparable<T>> Aligner<T> damerauLevenshtein() {
        return Aligner.<T>builder()
            .setEquals(T::equals)
            .setCompareTo(Comparator.naturalOrder())
            .build();
    }

}
