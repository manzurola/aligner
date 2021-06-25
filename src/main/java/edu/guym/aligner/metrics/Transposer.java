package edu.guym.aligner.metrics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public interface Transposer<T> {

    boolean isTransposed(T[] source, T[] target);

    static <T> Transposer<T> none() {
        return (source, target) -> false;
    }

    static <T> Transposer<T> comparing(Comparator<T> comparator) {
        return new ComparingTransposer<>(comparator);
    }
}

class ComparingTransposer<T> implements Transposer<T> {

    private final Comparator<T> comparator;

    public ComparingTransposer(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean isTransposed(T[] source, T[] target) {
        Arrays.sort(source, comparator);
        Arrays.sort(target, comparator);
        return IntStream
                .range(0, source.length)
                .allMatch(index -> comparator.compare(source[index], target[index]) == 0);
    }
}
