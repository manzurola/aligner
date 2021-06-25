package edu.guym.aligner.metrics;

@FunctionalInterface
public interface InsertCost<T> {

    double getCost(T target);
}
