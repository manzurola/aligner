package com.github.manzurola.aligner.metrics;

public interface Equalizer<T> {

    boolean isEqual(T source, T token);
}
