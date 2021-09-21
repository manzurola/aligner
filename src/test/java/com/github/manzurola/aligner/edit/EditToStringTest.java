package com.github.manzurola.aligner.edit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EditToStringTest {


    @Test
    void toStringInsert() {
        Edit<Integer> edit = Edit.builder()
                .insert(1, 2, 3)
                .atPosition(0, 0);

        String expected = "Insert [1, 2, 3]";
        String actual = edit.toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toStringDelete() {
        Edit<Integer> edit = Edit.builder()
                .delete(1, 2, 3)
                .atPosition(0, 0);

        String expected = "Delete [1, 2, 3]";
        String actual = edit.toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toStringSubstitute() {
        Edit<Integer> edit = Edit.builder()
                .substitute(1, 2, 3)
                .with(4, 5, 6)
                .atPosition(0, 0);

        String expected = "Substitute [1, 2, 3] with [4, 5, 6]";
        String actual = edit.toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toStringTranspose() {
        Edit<Integer> edit = Edit.builder()
                .transpose(1, 2, 3)
                .to(3, 2, 1)
                .atPosition(0, 0);

        String expected = "Transpose [1, 2, 3] to [3, 2, 1]";
        String actual = edit.toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toStringEqual() {
        Edit<Integer> edit = Edit.builder()
                .equal(1, 2, 3)
                .and(1, 2, 3)
                .atPosition(0, 0);

        String expected = "Equal [1, 2, 3] and [1, 2, 3]";
        String actual = edit.toString();
        Assertions.assertEquals(expected, actual);
    }
}
