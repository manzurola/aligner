package io.squarebunny.aligner.edit.visitor;

import io.squarebunny.aligner.edit.Edit;

public interface EditVisitor<R> {

    R visitEqual(Edit<?> edit);

    R visitInsert(Edit<?> edit);

    R visitDelete(Edit<?> edit);

    R visitSubstitute(Edit<?> edit);

    default R visitTranspose(Edit<?> edit) {
        return visitSubstitute(edit);
    }
}
