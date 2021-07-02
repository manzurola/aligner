package io.languagetoys.aligner.metrics;

@FunctionalInterface
public interface SubstituteCost<T> {

    double getCost(T source, T target);
}
