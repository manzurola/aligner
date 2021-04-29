package io.squarebunny.aligner.utils;

import io.squarebunny.aligner.Aligner;
import io.squarebunny.aligner.Alignment;

import java.util.stream.Collectors;

public final class AlignerUtils {

    private AlignerUtils() {
    }

    public static double charEditRatio(String source, String target) {
        return alignCharacters(source, target).ratio();
    }

    public static Alignment<Character> alignCharacters(String source, String target) {
        return Aligner.levenshtein(Character::equals).align(
                source.chars().mapToObj(value -> (char) value).collect(Collectors.toList()),
                target.chars().mapToObj(value -> (char) value).collect(Collectors.toList()));
    }

}
