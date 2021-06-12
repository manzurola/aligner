package io.squarebunny.aligner.edit;

import io.squarebunny.aligner.edit.builder.EditBuilder;
import io.squarebunny.aligner.edit.comparator.EditComparator;
import io.squarebunny.aligner.edit.visitor.EditVisitor;
import io.squarebunny.aligner.edit.visitor.EditVisitorBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.squarebunny.aligner.edit.Operation.SUBSTITUTE;
import static io.squarebunny.aligner.edit.Operation.TRANSPOSE;

public final class Edit<T> implements Comparable<Edit<T>> {

    private final Operation operation;
    private final Segment<T> source;
    private final Segment<T> target;

    protected Edit(Operation operation,
                   Segment<T> source,
                   Segment<T> target) {
        this.operation = Objects.requireNonNull(operation);
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
    }

    public static EditBuilder builder() {
        return new EditBuilder();
    }

    public static <T, R> EditVisitorBuilder<R> visitor() {
        return new EditVisitorBuilder<>();
    }

    public static <T> Edit<T> of(Operation operation,
                                 Segment<T> source,
                                 Segment<T> target) {
        return new Edit<>(operation, source, target);
    }

    public final Operation operation() {
        return operation;
    }

    public final Segment<T> source() {
        return source;
    }

    public final Segment<T> target() {
        return target;
    }

    public final Stream<T> stream() {
        return Stream.concat(source().stream(), target().stream());
    }

    public final <E> Stream<E> streamSegments(Function<? super Segment<T>, ? extends E> s,
                                              Function<? super Segment<T>, ? extends E> t) {
        return Stream.concat(Stream.of(s.apply(source())), Stream.of(t.apply(target())));
    }

    public final Optional<? extends Edit<T>> filter(Predicate<? super Edit<T>> predicate) {
        return Optional.of(this).filter(predicate);
    }

    public final boolean matches(Predicate<? super Edit<T>> predicate) {
        return predicate.test(this);
    }

    public final <E> Edit<E> map(Function<? super T, ? extends E> mapper) {
        return of(operation(), source().map(mapper), target().map(mapper));
    }

    public final <E> Edit<E> mapSegments(Function<Segment<T>, Segment<E>> sourceMapper,
                                         Function<Segment<T>, Segment<E>> targetMapper) {
        return of(
                operation(),
                sourceMapper.apply(source()),
                targetMapper.apply(target())
        );
    }

    public final <R> R transform(Function<? super Edit<T>, ? extends R> mapper) {
        return mapper.apply(this);
    }

    public final <R> R accept(EditVisitor<R> visitor) {
        switch (operation()) {
            case INSERT:
                return visitor.visitInsert(this);
            case DELETE:
                return visitor.visitDelete(this);
            case SUBSTITUTE:
                return visitor.visitSubstitute(this);
            case TRANSPOSE:
                return visitor.visitTranspose(this);
            case EQUAL:
                return visitor.visitEqual(this);
        }
        throw new RuntimeException("Invalid Edit operation");
    }

    /**
     * Merges this edit with the supplied other, creating a new edit.
     * The merged edit will contain all elements but may have a different operation.
     * If both edits are equal, this is returned.
     * If edits cannot be merged (due to non adjacent segments), an exception is thrown.
     *
     * @param other
     * @return the merged edit
     */
    public final Edit<T> mergeWith(Edit<T> other) {
        Objects.requireNonNull(other);

        if (equals(other)) {
            return this;
        }

        List<Edit<T>> sorted = Stream.of(this, other)
                .sorted()
                .collect(Collectors.toList());
        Edit<T> left = sorted.get(0);
        Edit<T> right = sorted.get(1);

        if (!left.canMergeWith(right)) {
            throw new IllegalArgumentException("cannot merge edits");
        }

        Operation operation = resolveMergedOperation(other);
        Segment<T> source = left.source().concatenate(right.source());
        Segment<T> target = left.target().concatenate(right.target());

        return Edit.of(operation, source, target);
    }

    public final boolean canMergeWith(Edit<T> other) {
        return this.source().isNeighbour(other.source()) && this.target().isNeighbour(other.target());
    }

    private Operation resolveMergedOperation(Edit<T> other) {

        if (operation().equals(TRANSPOSE) ||
                other.operation().equals(TRANSPOSE) ||
                !operation().equals(other.operation())) {
            return SUBSTITUTE;
        }

        return operation();
    }

    @Override
    public final int compareTo(Edit<T> o) {
        return EditComparator.INSTANCE.compare(this, o);
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edit<?> that = (Edit<?>) o;
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
                ", position= [" + source().position() + ", " + target.position() + "]" +
                ']';
    }
}
