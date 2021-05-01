package io.squarebunny.aligner;

import java.util.List;
import java.util.function.BiPredicate;

public interface Aligner<T> {

    Alignment<T> align(List<T> source, List<T> target);

    static <T> AlignerBuilder<T> builder() {
        return new AlignerBuilder<>();
    }

    static <T> AlignerBuilder<T> builder(Class<T> type) {
        return new AlignerBuilder<>();
    }

    static <T> Aligner<T> levenshtein(BiPredicate<T, T> isEqual) {
        return new AlignerBuilder<T>()
                .setIsEqual(isEqual)
                .build();
    }

    static <T> Aligner<T> levenshtein() {
        return new AlignerBuilder<T>()
                .setIsTransposed((s, t) -> false)
                .build();
    }

    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein() {
        return new AlignerBuilder<T>()
                .build();
    }

    static <T extends Comparable<T>> Aligner<T> damerauLevenshtein(BiPredicate<T, T> isEqual) {
        return new AlignerBuilder<T>()
                .setIsEqual(isEqual)
                .build();
    }
}
