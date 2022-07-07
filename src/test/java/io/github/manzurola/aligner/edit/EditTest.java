package io.github.manzurola.aligner.edit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EditTest {

    @Test
    void mergeIsSymmetrical() {

        Edit<String> left = Edit.builder()
                .delete("i")
                .atPosition(0, 0);

        Edit<String> right = Edit.builder()
                .insert("s")
                .atPosition(1, 0);

        Edit<String> expected = Edit.builder()
                .substitute("i")
                .with("s")
                .atPosition(0, 0);

        Assertions.assertEquals(expected, left.mergeWith(right));
        Assertions.assertEquals(expected, right.mergeWith(left));
    }

    @Test
    void testProject() {
        List<Integer> source = List.of(1, 2, 3);
        List<Integer> target = List.of(4, 5, 6);

        Edit<Integer> edit = Edit.builder()
                .substitute(2, 3)
                .with(5)
                .atPosition(1, 1);

        Edit<String> expected = Edit.builder()
                .substitute("2", "3")
                .with("5")
                .atPosition(1, 1);

        Edit<String> actual = edit.project(List.of("1", "2", "3"), List.of("4", "5", "6"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testShift() {

        Edit<Integer> edit = Edit.builder()
                .substitute(2, 3)
                .with(5)
                .atPosition(1, 1);

        String name = edit
                .shift("")
                .equal("equal")
                .substitute(e -> "substitute")
                .delete(e -> "delete")
                .get();

        Assertions.assertEquals("substitute", name);
    }
}
