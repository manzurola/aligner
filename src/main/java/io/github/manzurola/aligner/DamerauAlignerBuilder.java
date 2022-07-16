package io.github.manzurola.aligner;

import io.github.manzurola.aligner.metrics.*;

import java.util.Comparator;

final class DamerauAlignerBuilder<T> {

    private Equalizer<T> equalizer = T::equals;
    private Comparator<T> comparator;

    private DeleteCost<T> deleteCost = (s) -> 1.0;
    private InsertCost<T> insertCost = (s) -> 1.0;
    private SubstituteCost<T> substituteCost = (s, t) -> 1.0;
    private TransposeCost<T> transposeCost = (s, t) -> s.length - 1.0;

    public DamerauAlignerBuilder<T> setDeleteCost(DeleteCost<T> deleteCost) {
        this.deleteCost = deleteCost;
        return this;
    }

    public DamerauAlignerBuilder<T> setInsertCost(InsertCost<T> insertCost) {
        this.insertCost = insertCost;
        return this;
    }

    public DamerauAlignerBuilder<T> setSubstituteCost(SubstituteCost<T> substituteCost) {
        this.substituteCost = substituteCost;
        return this;
    }

    public DamerauAlignerBuilder<T> setTransposeCost(TransposeCost<T> transposeCost) {
        this.transposeCost = transposeCost;
        return this;
    }

    public DamerauAlignerBuilder<T> setEquals(Equalizer<T> equalizer) {
        this.equalizer = equalizer;
        return this;
    }

    public DamerauAlignerBuilder<T> setCompareTo(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    public Aligner<T> build() {
        if (comparator != null && transposeCost != null) {
            return new Aligner<>(new DamerauAlgorithm<T>(
                equalizer,
                deleteCost,
                insertCost,
                substituteCost,
                comparator,
                transposeCost
            ));
        }
        return new Aligner<>(new DamerauAlgorithm<T>(
            equalizer,
            deleteCost,
            insertCost,
            substituteCost
        ));
    }
}
