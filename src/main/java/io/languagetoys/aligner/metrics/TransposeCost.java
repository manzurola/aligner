package io.languagetoys.aligner.metrics;

@FunctionalInterface
public interface TransposeCost<T> {

    double getCost(T[] source, T[] target);
}
