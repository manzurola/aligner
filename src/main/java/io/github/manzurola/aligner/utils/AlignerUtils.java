package io.github.manzurola.aligner.utils;

import io.github.manzurola.aligner.Aligner;

import java.util.stream.Collectors;

public final class AlignerUtils {

    private final static Aligner<Character> levenshteinChar = Aligner.levenshtein();

    private AlignerUtils() {
    }

    public static double charEditRatio(String source, String target) {
        return levenshteinChar.align(
                source.chars().mapToObj(value -> (char) value).collect(Collectors.toList()),
                target.chars().mapToObj(value -> (char) value).collect(Collectors.toList())
        ).ratio();
    }

}
