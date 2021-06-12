package io.squarebunny.aligner.edit;

public class SubstituteEdit<T> extends Edit<T> {

    SubstituteEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.SUBSTITUTE;
    }

    @Override
    public <R> R accept(EditVisitor<R> visitor) {
        return visitor.visitSubstitute(this);
    }

    @Override
    protected Operation mergeOperations(Operation other) {
        return Operation.SUBSTITUTE;
    }
}
