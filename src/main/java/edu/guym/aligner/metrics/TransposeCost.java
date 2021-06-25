package edu.guym.aligner.metrics;

@FunctionalInterface
public interface TransposeCost<T> {

    double getCost(T[] source, T[] target);
}
