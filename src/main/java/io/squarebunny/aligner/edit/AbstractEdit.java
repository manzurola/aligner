package io.squarebunny.aligner.edit;

import io.squarebunny.aligner.edit.comparator.EditComparator;

import java.util.Objects;

public abstract class AbstractEdit<S, T> implements Comparable<AbstractEdit<S, T>> {

    private final Operation operation;
    private final Segment<S> source;
    private final Segment<T> target;

    protected AbstractEdit(Operation operation,
                           Segment<S> source,
                           Segment<T> target) {
        this.operation = Objects.requireNonNull(operation);
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
    }

    public final Operation operation() {
        return operation;
    }

    public final Segment<S> source() {
        return source;
    }

    public final Segment<T> target() {
        return target;
    }

    @Override
    public int compareTo(AbstractEdit<S, T> o) {
        return new EditComparator().compare(this, o);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEdit<?, ?> that = (AbstractEdit<?, ?>) o;
        return operation == that.operation &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(operation, source, target);
    }

    @Override
    public final String toString() {
        return "Edit[" + operation +
                ", source=" + source +
                ", target=" + target +
                ']';
    }
}
