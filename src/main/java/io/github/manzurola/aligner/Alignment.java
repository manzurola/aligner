package io.github.manzurola.aligner;

import io.github.manzurola.aligner.edit.Edit;
import io.github.manzurola.aligner.edit.EqualEdit;
import io.github.manzurola.aligner.edit.Segment;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Alignment<T> {

    private final List<Edit<T>> edits;
    private final double cost;

    private Alignment(List<Edit<T>> edits, double cost) {
        this.edits = Objects.requireNonNull(edits);
        this.cost = cost;
    }

    public static <T> Alignment<T> of(List<Edit<T>> edits, double cost) {
        return new Alignment<>(edits, cost);
    }

    public final List<Edit<T>> edits() {
        return edits;
    }

    public final List<Edit<T>> diffs() {
        return edits.stream()
                .filter(e -> !(e instanceof EqualEdit))
                .collect(Collectors.toList());
    }

    public final Stream<Edit<T>> stream() {
        return edits().stream();
    }

    public final void forEach(Consumer<Edit<T>> consumer) {
        edits.forEach(consumer);
    }

    /**
     * The total cost of operations
     */
    public final double cost() {
        return cost;
    }

    /**
     * The normalized cost [0, 1]
     */
    public final double distance() {
        return cost() / Math.max(source().size(), target().size());
    }

    /**
     * The inverse of {@link #distance()}
     */
    public final double similarity() {
        return 1 - distance();
    }

    /**
     * The normalized cost ratio, defined as (maxLength - cost) / maxLength
     */
    public final double ratio() {
        int lensum = Math.max(source().size(), target().size());
        return (lensum - cost()) / lensum;
    }

    public final List<T> source() {
        return edits.stream()
                .map(Edit::source)
                .flatMap(Segment::stream)
                .collect(Collectors.toList());
    }

    public final List<T> target() {
        return edits.stream()
                .map(Edit::target)
                .flatMap(Segment::stream)
                .collect(Collectors.toList());
    }

    public final int size() {
        return edits.size();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alignment<?> alignment = (Alignment<?>) o;
        return Double.compare(alignment.cost, cost) == 0 &&
               edits.equals(alignment.edits);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(edits, cost);
    }

    @Override
    public final String toString() {
        return "Alignment{" +
               "edits=" + edits +
               ", cost=" + cost +
               '}';
    }


}
