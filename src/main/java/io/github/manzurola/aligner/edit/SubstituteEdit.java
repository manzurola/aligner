package io.github.manzurola.aligner.edit;

public class SubstituteEdit<T> extends Edit<T> {

    SubstituteEdit(Segment<T> source, Segment<T> target) {
        super(source, target);
    }

    @Override
    public Operation operation() {
        return Operation.SUBSTITUTE;
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
        return String.format("Substitute(%s,%s) @%d:%d",
                             source().tokens(),
                             target().tokens(),
                             source().position(),
                             target().position());
    }
}
