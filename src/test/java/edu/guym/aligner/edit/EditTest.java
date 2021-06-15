package edu.guym.aligner.edit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
