package io.squarebunny.aligner;

import io.squarebunny.aligner.alignment.Alignment;
import io.squarebunny.aligner.impl.AlignerBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface Aligner<T> {

    Alignment<T> align(List<T> source, List<T> target);

    @Deprecated
    static <T> AlignerBuilder<T> builder() {
        return new AlignerBuilder<>();
    }

    @Deprecated
    static <T> AlignerBuilder<T> builder(Class<T> forType) {
        return new AlignerBuilder<>();
    }

    static <T> Aligner<T> levenshtein() {
        return new AlignerBuilder<T>()
                .setEqualizer(T::equals)
                .build();
    }

    static <T> Aligner<T> levenshtein(BiPredicate<T, T> equalizer) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .build();
    }

    static <T> Aligner<T> levenshtein(BiPredicate<T, T> equalizer,
                                      BiFunction<T, T, Double> substituteCost) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setSubstituteCost(substituteCost)
                .build();
    }

    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein() {
        return new AlignerBuilder<T>()
                .setEqualizer(T::equals)
                .setComparator(Comparator.naturalOrder())
                .build();
    }

    static <T> Aligner<T> damerauLevenshtein(Comparator<T> comparator) {
        return new AlignerBuilder<T>()
                .setEqualizer(Object::equals)
                .setComparator(comparator)
                .build();
    }

    static <T> Aligner<T> damerauLevenshtein(BiPredicate<T, T> equalizer,
                                             Comparator<T> comparator) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .build();
    }

    static <T> Aligner<T> damerauLevenshtein(BiPredicate<T, T> equalizer,
                                             Comparator<T> comparator,
                                             BiFunction<T, T, Double> substituteCost) {
        return new AlignerBuilder<T>()
                .setEqualizer(equalizer)
                .setComparator(comparator)
                .setSubstituteCost(substituteCost)
                .build();
    }

}
