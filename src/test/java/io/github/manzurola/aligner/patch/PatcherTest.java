package io.github.manzurola.aligner.patch;

import io.github.manzurola.aligner.edit.Edit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PatcherTest {

    @Test
    void patchInsert() {
        List<String> target = Arrays.asList("hello", "hi", "there");
        Edit<String> edit = Edit.builder()
                .insert("hi")
                .atPosition(1, 1);
        List<String> expected = Arrays.asList("hello", "hi", "hi", "there");

        List<String> actual = EditPatcher.forEdit(edit).applyTo(target);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void patchDelete() {
        List<String> target = Arrays.asList("hello", "hi", "there");
        Edit<String> edit = Edit.builder()
                .delete("hi")
                .atPosition(1, 1);
        List<String> expected = Arrays.asList("hello", "there");

        List<String> actual = EditPatcher.forEdit(edit).applyTo(target);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void patchSubstitute() {
        List<String> target = Arrays.asList("hello", "hi", "there");
        Edit<String> edit = Edit.builder()
                .substitute("hi", "there")
                .with("shalom")
                .atPosition(1, 1);
        List<String> expected = Arrays.asList("hello", "shalom");

        List<String> actual = EditPatcher.forEdit(edit).applyTo(target);
        Assertions.assertEquals(expected, actual);
    }
}
