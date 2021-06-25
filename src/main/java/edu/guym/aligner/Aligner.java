package edu.guym.aligner;

import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.impl.DamerauLevenshtein;
import edu.guym.aligner.metrics.*;

import java.util.Comparator;
import java.util.List;

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
 * </ol>
 *
 * @param <T> the type of elements this aligner supports.
 */
public interface Aligner<T> {

    Alignment<T> align(List<T> source, List<T> target);

    /**
     * Get a new levenshtein aligner using {@code T::equals} as the equalizer.
     */
    static <T> Aligner<T> levenshtein() {
        return new Builder<T>()
                .setEqualizer(T::equals)
                .build();
    }

    /**
     * Get a new levenshtein aligner with a custom {@code equals} delegate function.
     *
     * @param equalizer a predicate to determine if two items are equal.
     */
    static <T> Aligner<T> levenshtein(Equalizer<T> equalizer) {
        return new Builder<T>()
                .setEqualizer(equalizer)
                .build();
    }

    /**
     * Get a new levenshtein aligner with a custom {@code equals} delegate function and substitution cost.
     *
     * @param equalizer      a predicate to determine if two items are equal.
     * @param substituteCost a bifunction that returns the cost of a substitute between two given items.
     */
    static <T> Aligner<T> levenshtein(Equalizer<T> equalizer,
                                      SubstituteCost<T> substituteCost) {
        return new Builder<T>()
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
        return new Builder<T>()
                .setEqualizer(T::equals)
                .setComparator(Comparator.naturalOrder())
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function.
     * <br/>
     * Param T must extend {@link Comparable}.
     *
     * @param equalizer a predicate to determine if two items are equal.
     */
    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein(Equalizer<T> equalizer) {
        return new Builder<T>()
                .setEqualizer(equalizer)
                .setComparator(Comparator.naturalOrder())
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function and comparator.
     *
     * @param equalizer  a predicate to determine if two items are equal.
     * @param comparator a {@link Comparator} used to determine if two lists are transposable.
     */
    static <T> Aligner<T> damerauLevenshtein(Equalizer<T> equalizer,
                                             Comparator<T> comparator) {
        return new Builder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .build();
    }

    /**
     * Get a new damerau-levenshtein aligner with a custom {@code equals} delegate function, comparator and substitution cost function.
     *
     * @param equalizer      a predicate to determine if two items are equal.
     * @param comparator     a {@link Comparator} used to determine if two lists are transposable.
     * @param substituteCost a bifunction that returns the cost of a substitute between two given items.
     */
    static <T> Aligner<T> damerauLevenshtein(Equalizer<T> equalizer,
                                             Comparator<T> comparator,
                                             SubstituteCost<T> substituteCost) {
        return new Builder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .setSubstituteCost(substituteCost)
                .build();
    }

    /**
     * Get a new builder to create a custom aligner based on the damerau levenshtein algorithm.
     * The builder is instantiated with default DL values for costs, an equalizer the uses T::equals and a null comparator (no transpose).
     */
    static <T> Aligner.Builder<T> builder() {
        return new Builder<>();
    }

    final class Builder<T> {

        private Equalizer<T> equalizer = T::equals;
        private Comparator<T> comparator = null;

        private DeleteCost<T> deleteCost = (s) -> 1.0;
        private InsertCost<T> insertCost = (t) -> 1.0;
        private SubstituteCost<T> substituteCost = (s, t) -> 1.0;
        private TransposeCost<T> transposeCost = (s, t) -> s.length - 1.0;

        public final Builder<T> setDeleteCost(DeleteCost<T> deleteCost) {
            this.deleteCost = deleteCost;
            return this;
        }

        public final Builder<T> setInsertCost(InsertCost<T> insertCost) {
            this.insertCost = insertCost;
            return this;
        }

        public final Builder<T> setSubstituteCost(SubstituteCost<T> substituteCost) {
            this.substituteCost = substituteCost;
            return this;
        }

        public final Builder<T> setTransposeCost(TransposeCost<T> transposeCost) {
            this.transposeCost = transposeCost;
            return this;
        }

        public Builder<T> setEqualizer(Equalizer<T> equalizer) {
            this.equalizer = equalizer;
            return this;
        }

        public final Builder<T> setComparator(Comparator<T> comparator) {
            this.comparator = comparator;
            return this;
        }

        public final Aligner<T> build() {
            return new DamerauLevenshtein<>(
                    equalizer,
                    comparator,
                    deleteCost,
                    insertCost,
                    substituteCost,
                    transposeCost
            );
        }
    }
}
