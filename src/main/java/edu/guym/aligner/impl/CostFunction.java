package edu.guym.aligner.impl;

public interface CostFunction<T> {

    double deleteCost(T source);

    double insertCost(T target);

    double substituteCost(T source, T target);

    double transposeCost(T[] source, T[] target);

}
