package io.github.manzurola.aligner.metrics;

@FunctionalInterface
public interface DeleteCost<T> {

    double getCost(T source);
}
