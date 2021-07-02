package io.languagetoys.aligner.edit;

public class TransposeEdit<T> extends Edit<T> {
    TransposeEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.TRANSPOSE;
    }

    @Override
    public <R> R accept(EditVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return Operation.SUBSTITUTE;
    }

    @Override
    public String toString() {
        return String.format("Transpose(%s,%s) @%d:%d",
                source().tokens(),
                target().tokens(),
                source().position(),
                target().position());
    }
}
