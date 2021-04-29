package io.squarebunny.aligner;

import io.squarebunny.aligner.algorithm.CostFunction;
import io.squarebunny.aligner.algorithm.DamerauLevenshtein;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AlignerBuilder<T> {

    private Function<T, Double> deleteCost = (s) -> 1.0;
    private Function<T, Double> insertCost = (t) -> 1.0;
    private BiFunction<T, T, Double> substituteCost = (s, t) -> 1.0;
    private BiFunction<List<T>, List<T>, Double> transposeCost = (s, t) -> s.size() - 1.0;
    private BiPredicate<T, T> isEqual = T::equals;
    private BiPredicate<List<T>, List<T>> isTransposed = (s, t) -> {
        List<T> sList = s.stream()
                .sorted()
                .collect(Collectors.toList());
        List<T> tList = t.stream()
                .sorted()
                .collect(Collectors.toList());
        return sList.equals(tList);
    };

    AlignerBuilder() {
    }

    public AlignerBuilder<T> setDeleteCost(Function<T, Double> deleteCost) {
        this.deleteCost = deleteCost;
        return this;
    }

    public AlignerBuilder<T> setInsertCost(Function<T, Double> insertCost) {
        this.insertCost = insertCost;
        return this;
    }

    public AlignerBuilder<T> setSubstituteCost(BiFunction<T, T, Double> substituteCost) {
        this.substituteCost = substituteCost;
        return this;
    }

    public AlignerBuilder<T> setTransposeCost(BiFunction<List<T>, List<T>, Double> transposeCost) {
        this.transposeCost = transposeCost;
        return this;
    }

    public AlignerBuilder<T> setIsEqual(BiPredicate<T, T> isEqual) {
        this.isEqual = isEqual;
        return this;
    }

    public AlignerBuilder<T> setIsTransposed(BiPredicate<List<T>, List<T>> isTransposed) {
        this.isTransposed = isTransposed;
        return this;
    }

    public Aligner<T> build() {
        return new DamerauLevenshtein<>(new CostFunction<T>() {
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
        }, isEqual, isTransposed);
    }
}