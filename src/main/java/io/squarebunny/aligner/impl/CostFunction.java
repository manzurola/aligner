package io.squarebunny.aligner.impl;

import java.util.List;

public interface CostFunction<T> {

    double deleteCost(T source);

    double insertCost(T target);

    double substituteCost(T source, T target);

    double transposeCost(List<T> source, List<T> target);

}
