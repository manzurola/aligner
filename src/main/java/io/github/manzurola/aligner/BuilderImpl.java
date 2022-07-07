package io.github.manzurola.aligner;

import io.github.manzurola.aligner.metrics.*;

import java.util.Comparator;

final class BuilderImpl<T> implements Aligner.Builder<T> {

    private Equalizer<T> equalizer;
    private Comparator<T> comparator;

    private DeleteCost<T> deleteCost;
    private InsertCost<T> insertCost;
    private SubstituteCost<T> substituteCost;
    private TransposeCost<T> transposeCost;

    @Override
    public final Aligner.Builder<T> setDeleteCost(DeleteCost<T> deleteCost) {
        this.deleteCost = deleteCost;
        return this;
    }

    @Override
    public final Aligner.Builder<T> setInsertCost(InsertCost<T> insertCost) {
        this.insertCost = insertCost;
        return this;
    }

    @Override
    public final Aligner.Builder<T> setSubstituteCost(SubstituteCost<T> substituteCost) {
        this.substituteCost = substituteCost;
        return this;
    }

    @Override
    public final Aligner.Builder<T> setTransposeCost(TransposeCost<T> transposeCost) {
        this.transposeCost = transposeCost;
        return this;
    }

    @Override
    public Aligner.Builder<T> setEqualizer(Equalizer<T> equalizer) {
        this.equalizer = equalizer;
        return this;
    }

    @Override
    public final Aligner.Builder<T> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    @Override
    public final Aligner<T> build() {
        return new AlignerImpl<>(
                equalizer,
                comparator,
                deleteCost,
                insertCost,
                substituteCost,
                transposeCost
        );
    }
}
