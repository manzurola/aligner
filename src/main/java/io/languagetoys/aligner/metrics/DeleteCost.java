package io.languagetoys.aligner.metrics;

@FunctionalInterface
public interface DeleteCost<T> {

    double getCost(T source);
}
