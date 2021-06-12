package io.squarebunny.aligner.edit;

public class InsertEdit<T> extends Edit<T> {
    InsertEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.INSERT;
    }

    @Override
    public <R> R accept(EditVisitor<R> visitor) {
        return visitor.visitInsert(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return operation().equals(other) ? operation() : Operation.SUBSTITUTE;
    }
}
