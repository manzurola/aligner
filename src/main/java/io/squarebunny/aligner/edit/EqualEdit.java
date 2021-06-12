package io.squarebunny.aligner.edit;

public class EqualEdit<T> extends Edit<T> {
    EqualEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.EQUAL;
    }

    @Override
    public <R> R accept(EditVisitor<R> visitor) {
        return visitor.visitEqual(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return operation().equals(other) ? operation() : Operation.SUBSTITUTE;
    }
}
