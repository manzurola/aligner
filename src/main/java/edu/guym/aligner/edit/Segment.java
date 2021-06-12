package edu.guym.aligner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public final class Segment<T> implements Comparable<Segment<T>> {

    private final int position;
    private final List<T> tokens;

    private Segment(int position, List<T> tokens) {
        this.position = position;
        this.tokens = Objects.requireNonNull(tokens);
    }

    public static <T> Segment<T> of(int position, List<T> tokens) {
        return new Segment<>(position, tokens);
    }

    public static <T> Segment<T> empty(int position) {
        return new Segment<>(position, List.of());
    }

    public final int position() {
        return position;
    }

    public final List<T> tokens() {
        return new ArrayList<>(tokens);
    }

    public final Stream<T> stream() {
        return tokens.stream();
    }

    public final boolean anyMatch(Predicate<? super T> predicate) {
        return tokens.stream().anyMatch(predicate);
    }

    public final boolean allMatch(Predicate<? super T> predicate) {
        return tokens.stream().allMatch(predicate);
    }

    public final List<T> sublist(int start, int end) {
        return tokens.subList(start, end);
    }

    public final <E> E collect(Collector<T, ?, E> collector) {
        return tokens.stream().collect(collector);
    }

    public final T first() {
        return stream().findFirst().orElse(null);
    }

    public final boolean isEmpty() {
        return tokens.isEmpty();
    }

    public final int size() {
        return tokens.size();
    }

    public final T last() {
        return stream().reduce((first, second) -> second).orElse(null);
    }

    public final <R> Segment<R> map(Function<? super T, ? extends R> teFunction) {
        return of(
                this.position(),
                this.stream().map(teFunction).collect(Collectors.toList())
        );
    }

    public final <E> Segment<E> mapWithIndex(Function<Integer, E> mapper) {
        List<E> collect = IntStream
                .range(position, position + size())
                .mapToObj(mapper::apply)
                .collect(Collectors.toList());
        return of(position, collect);
    }

    /**
     * Get the indices that this segment references.
     * @return an IntStream of range {@code (position, position + size())}.
     */
    public final IntStream indices() {
        return IntStream.range(position, position + size());
    }

    /**
     * Selects the current segment from the supplied list, based on the position of this segment.
     * For example, given a list of length 10 and a segment of size 4 starting from position 3,
     * the returned segment will contain elements 3,4,5,6 from the supplied list.
     * <p>
     * This is equivalent of running {@code mapWithIndex(items::get)};
     *
     * @param items
     * @param <E>
     * @return
     */
    @Deprecated
    public final <E> Segment<E> select(List<E> items) {
        return mapWithIndex(items::get);
    }

    /**
     * Concatenates this {@code Segment} with {@code other}, resulting in a new object representing both adjacent chunks as one consecutive Segment
     * starting at position {@code min(this.position, other.position)}.
     * <p>
     * If both edits are equal then {@code this} is returned.
     * If edits are not neighbours, i.e. {@link #isNeighbour(Segment)} returns false, an IllegalArgumentException is thrown.
     * <p>
     *
     * @param other an adjacent segment, either before or after {@code this}
     * @return a new Segment if the conditions for concatenation are met, {@code this} if {@code this} and {@code other} are equal.
     * @throws IllegalArgumentException if {@code other} is not a valid neighbour, i.e. {@link #isNeighbour(Segment)} returns false
     */
    public final Segment<T> concatenate(Segment<T> other) {
        if (equals(other)) {
            return this;
        }

        if (!isNeighbour(other)) {
            throw new IllegalArgumentException("other is not adjacent to this");
        }

        int newPosition = Math.min(position(), other.position());
        List<T> newTokens = Stream
                .concat(stream(), other.stream())
                .collect(Collectors.toList());

        return Segment.of(newPosition, newTokens);
    }

    /**
     * Returns true if other is adjacent to this, either from to left or to the right of {@code this}.
     *
     * @param other an adjacent Segment, such that when both are sorted, {@code left.position() + left.size() == right.position()} returns true.
     * @return true if segments are neighbours, false otherwise.
     */
    public final boolean isNeighbour(Segment<T> other) {
        List<Segment<T>> sorted = Stream.of(this, other)
                .sorted()
                .collect(Collectors.toList());
        Segment<T> left = sorted.get(0);
        Segment<T> right = sorted.get(1);
        return left.position() + left.size() == right.position();
    }

    @Override
    public final int compareTo(Segment<T> o) {
        return Integer.compare(position, o.position);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment<?> segment = (Segment<?>) o;
        return position == segment.position &&
                tokens.equals(segment.tokens);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(position, tokens);
    }

    @Override
    public final String toString() {
        return "Segment{" +
                "position=" + position +
                ", tokens=" + tokens +
                '}';
    }
}
