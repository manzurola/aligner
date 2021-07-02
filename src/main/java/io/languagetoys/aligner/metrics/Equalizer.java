package io.languagetoys.aligner.metrics;

public interface Equalizer<T> {

    boolean isEqual(T source, T token);
}
