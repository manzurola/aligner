package io.squarebunny.aligner.edit;

import io.squarebunny.aligner.edit.builder.EditBuilder;
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

public class Edit<T> extends AbstractEdit<T, T> {

    protected Edit(Operation operation,
                   Segment<T> source,
                   Segment<T> target) {
        super(operation, source, target);
    }

    public static EditBuilder builder() {
        return new EditBuilder();
    }

    public static <T, R> EditVisitorBuilder<Edit<T>, R> visitor() {
        return new EditVisitorBuilder<>();
    }

    public static <T> Edit<T> of(Operation operation,
                                 Segment<T> source,
                                 Segment<T> target) {
        return new Edit<>(operation, source, target);
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

    public final <R> R accept(EditVisitor<Edit<T>, R> visitor) {
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
        throw new RuntimeException("Invalid Edit op");
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
        Segment<T> source = left.source().mergeWith(right.source());
        Segment<T> target = left.target().mergeWith(right.target());

        return Edit.of(operation, source, target);
    }

    public final boolean canMergeWith(Edit<T> other) {
        return this.source().canMergeWith(other.source()) && this.target().canMergeWith(other.target());
    }

    private Operation resolveMergedOperation(Edit<T> other) {

        if (operation().equals(TRANSPOSE) ||
                other.operation().equals(TRANSPOSE) ||
                !operation().equals(other.operation())) {
            return SUBSTITUTE;
        }

        return operation();
    }
}
