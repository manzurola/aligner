package io.squarebunny.aligner.edit;

public class TransposeEdit<T> extends Edit<T> {
    TransposeEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.TRANSPOSE;
    }

    @Override
    public <R> R accept(EditVisitor<R> visitor) {
        return visitor.visitTranspose(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return Operation.SUBSTITUTE;
    }
}
