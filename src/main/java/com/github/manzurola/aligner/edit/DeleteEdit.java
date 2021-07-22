package com.github.manzurola.aligner.edit;

public class DeleteEdit<T> extends Edit<T> {
    DeleteEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.DELETE;
    }

    @Override
    public <R> R accept(EditVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return operation().equals(other) ? operation() : Operation.SUBSTITUTE;
    }

    @Override
    public String toString() {
        return String.format("Delete(%s) @%d:%d", source().tokens(), source().position(), target().position());
    }
}
