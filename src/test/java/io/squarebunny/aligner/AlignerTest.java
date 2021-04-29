package io.squarebunny.aligner;

import io.squarebunny.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlignerTest {

    @Test
    void levenshteinTest() {
        Aligner<String> aligner = Aligner.levenshtein();

        List<String> source = List.of("the", "guy", "am");
        List<String> target = List.of("am", "guy", "the");

        List<Edit<String>> expected = List.of(
                Edit.builder().substitute("the").with("am").atPosition(0, 0),
                Edit.builder().equal("guy").and("guy").atPosition(1, 1),
                Edit.builder().substitute("am").with("the").atPosition(2, 2)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(2.0 / 3.0, actual.distance());
    }

    @Test
    void levenshteinTestEmptySource() {
        Aligner<String> aligner = Aligner.levenshtein();

        List<String> source = List.of();
        List<String> target = List.of("", "guy");

        List<Edit<String>> expected = List.of(
                Edit.builder().insert("").atPosition(0, 0),
                Edit.builder().insert("guy").atPosition(0, 1)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(1.0, actual.distance());
    }

    @Test
    void damerauTestTranspositionSmall() {
        Aligner<String> aligner = Aligner.damerauLevenshtein();

        List<String> source = List.of("guy", "is");
        List<String> target = List.of("is", "guy");

        List<Edit<String>> expected = List.of(
                Edit.builder().transpose("guy", "is").to("is", "guy").atPosition(0, 0)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(1.0, actual.cost());
        assertEquals(0.5, actual.distance());
    }

    @Test
    void damerauTestTranspositionLarge() {
        Aligner<String> aligner = Aligner.damerauLevenshtein();

        List<String> source = List.of("guy", "is", "good");
        List<String> target = List.of("is", "good", "guy");

        List<Edit<String>> expected = List.of(
                Edit.builder().transpose("guy", "is", "good").to("is", "good", "guy").atPosition(0, 0)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(2.0 / 3.0, actual.distance());
    }
}
