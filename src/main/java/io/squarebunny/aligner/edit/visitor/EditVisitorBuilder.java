package io.squarebunny.aligner.edit.visitor;

import io.squarebunny.aligner.edit.AbstractEdit;

import java.util.function.Function;

public class EditVisitorBuilder<E extends AbstractEdit<?, ?>, R> {

    private Function<E, R> onEqual = (e) -> null;
    private Function<E, R> onInsert = (e) -> null;
    private Function<E, R> onDelete = (e) -> null;
    private Function<E, R> onSubstitute = (e) -> null;
    private Function<E, R> onTranspose = (e) -> null;


    public EditVisitorBuilder<E, R> onEqual(Function<E, R> visitor) {
        this.onEqual = visitor;
        return this;
    }

    public EditVisitorBuilder<E, R> onInsert(Function<E, R> visitor) {
        this.onInsert = visitor;
        return this;
    }

    public EditVisitorBuilder<E, R> onDelete(Function<E, R> visitor) {
        this.onDelete = visitor;
        return this;
    }

    public EditVisitorBuilder<E, R> onSubstitute(Function<E, R> visitor) {
        this.onSubstitute = visitor;
        return this;
    }

    public EditVisitorBuilder<E, R> onTranspose(Function<E, R> visitor) {
        this.onTranspose = visitor;
        return this;
    }

    public EditVisitor<E, R> build() {
        return new EditVisitor<>() {

            @Override
            public R visitEqual(E edit) {
                return onEqual.apply(edit);
            }

            @Override
            public R visitInsert(E edit) {
                return onInsert.apply(edit);
            }

            @Override
            public R visitDelete(E edit) {
                return onDelete.apply(edit);
            }

            @Override
            public R visitSubstitute(E edit) {
                return onSubstitute.apply(edit);
            }

            @Override
            public R visitTranspose(E edit) {
                return onTranspose.apply(edit);
            }
        };
    }

}
