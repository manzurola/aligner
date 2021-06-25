package edu.guym.aligner.metrics;

public interface Equalizer<T> {

    boolean isEqual(T source, T token);
}
