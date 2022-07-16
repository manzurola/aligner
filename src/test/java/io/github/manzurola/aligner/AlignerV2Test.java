package io.github.manzurola.aligner;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.aligner.edit.Operation;
import io.github.manzurola.aligner.edit.Segment;
import io.github.manzurola.aligner.metrics.Equalizer;
import io.github.manzurola.aligner.metrics.SubstituteCost;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

public class AlignerV2Test {

    @Test
    void alignAndMerge() {
        Aligner<Integer> aligner = Aligner.<Integer>builder()
            .setEquals(Integer::equals)
            .setCompareTo(Integer::compareTo)
            .setSubstituteCost((s, t) -> Double.MAX_VALUE)
            .build();

        List<Integer> source = List.of(1, 3, 3);
        List<Integer> target = List.of(1, 2, 3);

        Alignment<Integer> alignment = aligner.align(source, target);

        // Get the cost and distance of the alignment
        double cost = alignment.cost();
        double distance = alignment.distance();
        System.out.printf("Cost: %s, distance: %s%n", cost, distance);

        // Inspect individual edits
        for (Edit<Integer> edit : alignment.edits()) {
            Operation op = edit.operation();
            Segment<Integer> s = edit.source();
            Segment<Integer> t = edit.target();
            System.out.printf("Operation: %s, source: %s, target: %s%n", op, s, t);
        }
    }
}
