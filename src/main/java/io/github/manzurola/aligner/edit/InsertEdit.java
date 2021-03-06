package io.github.manzurola.aligner.edit;

public class InsertEdit<T> extends Edit<T> {
    InsertEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.INSERT;
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
        return String.format("Insert(%s) @%d:%d", target().tokens(), source().position(), target().position());
    }
}
