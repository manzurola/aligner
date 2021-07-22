package com.github.manzurola.aligner.metrics;

@FunctionalInterface
public interface InsertCost<T> {

    double getCost(T target);
}
