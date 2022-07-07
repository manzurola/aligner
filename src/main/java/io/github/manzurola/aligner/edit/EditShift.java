package io.github.manzurola.aligner.edit;

import java.util.function.Function;

public class EditShift<T, R> {

    private final EditVisitor.Builder<T, R> visitor;
    private final Edit<T> edit;

    EditShift(Edit<T> edit, Function<Edit<T>, R> initial) {
        this.edit = edit;
        EditVisitor.Builder<T, R> builder = EditVisitor.builder();
        this.visitor = builder
                .onDelete(initial)
                .onSubstitute(initial)
                .onEqual(initial)
                .onInsert(initial)
                .onTranspose(initial);
    }

    public EditShift<T, R> equal(Function<Edit<T>, R> visitor) {
        this.visitor.onEqual(visitor);
        return this;
    }

    public EditShift<T, R> equal(R value) {
        this.visitor.onEqual(tEdit -> value);
        return this;
    }

    public EditShift<T, R> insert(Function<Edit<T>, R> visitor) {
        this.visitor.onInsert(visitor);
        return this;
    }

    public EditShift<T, R> delete(Function<Edit<T>, R> visitor) {
        this.visitor.onDelete(visitor);
        return this;
    }

    public EditShift<T, R> substitute(Function<Edit<T>, R> visitor) {
        this.visitor.onSubstitute(visitor);
        return this;
    }

    public EditShift<T, R> transpose(Function<Edit<T>, R> visitor) {
        this.visitor.onTranspose(visitor);
        return this;
    }

    public R get() {
        return edit.accept(visitor.build());
    }

}
