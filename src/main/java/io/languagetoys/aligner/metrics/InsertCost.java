package io.languagetoys.aligner.metrics;

@FunctionalInterface
public interface InsertCost<T> {

    double getCost(T target);
}
