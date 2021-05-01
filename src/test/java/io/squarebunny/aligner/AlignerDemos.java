package io.squarebunny.aligner;

import io.squarebunny.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlignerDemos {

    @Test
    void levenshtein() {

        // Create an aligner via the static factory methods on the Aligner class.
        // Try experimenting with the different configurations and the customizable builder class.
        Aligner<String> aligner = Aligner.levenshtein();

        List<String> source = List.of("one", "three", "three");
        List<String> target = List.of("one", "two", "three");

        // The alignment stores the edits required to transform source into target.
        Alignment<String> alignment = aligner.align(source, target);

        // Print and review the edits
        alignment.forEach(System.out::println);

        // These are the expected edits, followed by expected alignment cost and distance
        List<Edit<String>> expectedEdits = List.of(
                Edit.builder().equal("one").and("one").atPosition(0, 0),
                Edit.builder().substitute("three").with("two").atPosition(1, 1),
                Edit.builder().equal("three").and("three").atPosition(2, 2)
        );
        double expectedCost = 1.0;
        double expectedDistance = 1.0 / 3.0;

        assertEquals(expectedEdits, alignment.edits());
        assertEquals(expectedCost, alignment.cost());
        assertEquals(expectedDistance, alignment.distance());
    }

    @Test
    void levenshteinWithoutSubstitute() {
        Aligner<String> aligner = Aligner.<String>builder()
                .setSubstituteCost((s, t) -> Double.MAX_VALUE)
                .build();

        List<String> source = List.of("one", "three", "three");
        List<String> target = List.of("one", "two", "three");

        Alignment<String> alignment = aligner.align(source, target);

        List<Edit<String>> expectedEdits = List.of(
                Edit.builder().equal("one").and("one").atPosition(0, 0),
                Edit.builder().delete("three").atPosition(1, 1),
                Edit.builder().insert("two").atPosition(2, 1),
                Edit.builder().equal("three").and("three").atPosition(2, 2)
        );
        double expectedCost = 2.0;
        double expectedDistance = 2.0 / 3.0;

        assertEquals(expectedEdits, alignment.edits());
        assertEquals(expectedCost, alignment.cost());
        assertEquals(expectedDistance, alignment.distance());
    }
}
