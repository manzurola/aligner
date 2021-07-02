package io.languagetoys.aligner.edit;

public class EqualEdit<T> extends Edit<T> {
    EqualEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.EQUAL;
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
        return String.format("Equal(%s,%s) @%d:%d",
                source().tokens(),
                target().tokens(),
                source().position(),
                target().position());
    }
}
