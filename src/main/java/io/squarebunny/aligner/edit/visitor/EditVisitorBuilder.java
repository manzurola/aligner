package io.squarebunny.aligner.edit.visitor;

import io.squarebunny.aligner.edit.Edit;

import java.util.function.Function;

public class EditVisitorBuilder<R> {

    private Function<Edit<?>, R> onEqual = (e) -> null;
    private Function<Edit<?>, R> onInsert = (e) -> null;
    private Function<Edit<?>, R> onDelete = (e) -> null;
    private Function<Edit<?>, R> onSubstitute = (e) -> null;
    private Function<Edit<?>, R> onTranspose = (e) -> null;

    public EditVisitorBuilder<R> onEqual(Function<Edit<?>, R> visitor) {
        this.onEqual = visitor;
        return this;
    }

    public EditVisitorBuilder<R> onInsert(Function<Edit<?>, R> visitor) {
        this.onInsert = visitor;
        return this;
    }

    public EditVisitorBuilder<R> onDelete(Function<Edit<?>, R> visitor) {
        this.onDelete = visitor;
        return this;
    }

    public EditVisitorBuilder<R> onSubstitute(Function<Edit<?>, R> visitor) {
        this.onSubstitute = visitor;
        return this;
    }

    public EditVisitorBuilder<R> onTranspose(Function<Edit<?>, R> visitor) {
        this.onTranspose = visitor;
        return this;
    }

    public EditVisitor<R> build() {
        return new EditVisitor<>() {

            @Override
            public R visitEqual(Edit<?> edit) {
                return onEqual.apply(edit);
            }

            @Override
            public R visitInsert(Edit<?> edit) {
                return onInsert.apply(edit);
            }

            @Override
            public R visitDelete(Edit<?> edit) {
                return onDelete.apply(edit);
            }

            @Override
            public R visitSubstitute(Edit<?> edit) {
                return onSubstitute.apply(edit);
            }

            @Override
            public R visitTranspose(Edit<?> edit) {
                return onTranspose.apply(edit);
            }
        };
    }

}
