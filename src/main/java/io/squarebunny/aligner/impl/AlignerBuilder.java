package io.squarebunny.aligner.impl;

import io.squarebunny.aligner.Aligner;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public final class AlignerBuilder<T> {

    private BiPredicate<T, T> equalizer = T::equals;
    private Comparator<T> comparator = null;

    private Function<T, Double> deleteCost = (s) -> 1.0;
    private Function<T, Double> insertCost = (t) -> 1.0;
    private BiFunction<T, T, Double> substituteCost = (s, t) -> 1.0;
    private BiFunction<List<T>, List<T>, Double> transposeCost = (s, t) -> s.size() - 1.0;

    public final AlignerBuilder<T> setDeleteCost(Function<T, Double> deleteCost) {
        this.deleteCost = deleteCost;
        return this;
    }

    public final AlignerBuilder<T> setInsertCost(Function<T, Double> insertCost) {
        this.insertCost = insertCost;
        return this;
    }

    public final AlignerBuilder<T> setSubstituteCost(BiFunction<T, T, Double> substituteCost) {
        this.substituteCost = substituteCost;
        return this;
    }

    public final AlignerBuilder<T> setTransposeCost(BiFunction<List<T>, List<T>, Double> transposeCost) {
        this.transposeCost = transposeCost;
        return this;
    }

    public AlignerBuilder<T> setEqualizer(BiPredicate<T, T> equalizer) {
        this.equalizer = equalizer;
        return this;
    }

    public final AlignerBuilder<T> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    public final Aligner<T> build() {
        return new DamerauLevenshtein<>(new CostFunction<>() {
            @Override
            public double deleteCost(T source) {
                return deleteCost.apply(source);
            }

            @Override
            public double insertCost(T target) {
                return insertCost.apply(target);
            }

            @Override
            public double substituteCost(T source, T target) {
                return substituteCost.apply(source, target);
            }

            @Override
            public double transposeCost(List<T> source, List<T> target) {
                return transposeCost.apply(source, target);
            }
        }, equalizer, comparator);
    }
}