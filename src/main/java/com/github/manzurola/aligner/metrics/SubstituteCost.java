package com.github.manzurola.aligner.metrics;

@FunctionalInterface
public interface SubstituteCost<T> {

    double getCost(T source, T target);
}
