package io.squarebunny.aligner;

import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.Segment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Alignment<T> {

    private final List<Edit<T>> edits;
    private final double cost;
    private final double distance;

    private Alignment(List<Edit<T>> edits, double cost, double distance) {
        this.edits = Objects.requireNonNull(edits);
        this.cost = cost;
        this.distance = distance;
    }

    public static <T> Alignment<T> of(List<Edit<T>> edits, double cost, double distance) {
        return new Alignment<>(edits, cost, distance);
    }

    public final List<Edit<T>> edits() {
        return edits;
    }

    public final Stream<Edit<T>> stream() {
        return edits().stream();
    }

    public final double cost() {
        return cost;
    }

    public final double distance() {
        return distance;
    }

    public final double similarity() {
        return 1 - distance;
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

    public final double ratio() {
        int lensum = Math.max(source().size(), target().size());
        return (lensum - cost()) / lensum;
    }

    public final int size() {
        return edits.size();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alignment<?> alignment = (Alignment<?>) o;
        return Double.compare(alignment.cost, cost) == 0 && Double.compare(alignment.distance, distance) == 0 && edits.equals(alignment.edits);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(edits, cost, distance);
    }

    @Override
    public final String toString() {
        return "Alignment{" +
                "edits=" + edits +
                ", cost=" + cost +
                ", distance=" + distance +
                '}';
    }


}
