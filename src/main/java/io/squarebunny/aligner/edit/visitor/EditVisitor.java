package io.squarebunny.aligner.edit.visitor;

import io.squarebunny.aligner.edit.AbstractEdit;

public interface EditVisitor<E extends AbstractEdit<?, ?>, R> {

    R visitEqual(E edit);

    R visitInsert(E edit);

    R visitDelete(E edit);

    R visitSubstitute(E edit);

    default R visitTranspose(E edit) {
        return visitSubstitute(edit);
    }
}
