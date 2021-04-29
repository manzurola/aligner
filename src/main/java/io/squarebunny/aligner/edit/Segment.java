package io.squarebunny.aligner.edit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public final class Segment<T> implements Serializable, Comparable<Segment<T>> {

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

    public final IntStream indices() {
        return IntStream.range(position, position + size());
    }

    public final <E> Segment<E> select(List<E> items) {
        return mapWithIndex(items::get);
    }

    public final Segment<T> mergeWith(Segment<T> next) {
        if (equals(next)) {
            return this;
        }

        if (!canMergeWith(next)) {
            throw new IllegalArgumentException("next is not adjacent to this");
        }

        int newPosition = position();
        List<T> newTokens = Stream
                .concat(stream(), next.stream())
                .collect(Collectors.toList());

        return Segment.of(newPosition, newTokens);
    }

    public final boolean canMergeWith(Segment<T> next) {
        List<Segment<T>> sorted = Stream.of(this, next)
                .sorted()
                .collect(Collectors.toList());
        Segment<T> left = sorted.get(0);
        Segment<T> right = sorted.get(1);
        return left.position() + left.size() == right.position();
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
        return "{" +
                "position=" + position +
                ", words=" + tokens +
                '}';
    }


    @Override
    public int compareTo(Segment<T> o) {
        return Integer.compare(position, o.position);
    }
}
