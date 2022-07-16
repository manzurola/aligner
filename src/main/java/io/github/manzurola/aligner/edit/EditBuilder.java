package io.github.manzurola.aligner.edit;

import java.util.List;
import java.util.function.BiFunction;

public final class EditBuilder {

    @SafeVarargs
    public final <T> Insert<T> insert(T... tokens) {
        return insert(List.of(tokens));
    }

    public final <T> Insert<T> insert(List<T> tokens) {
        return new Insert<>(tokens);
    }

    @SafeVarargs
    public final <T> Delete<T> delete(T... tokens) {
        return delete(List.of(tokens));
    }

    public final <T> Delete<T> delete(List<T> tokens) {
        return new Delete<>(tokens);
    }

    @SafeVarargs
    public final <T> Substitute<T> substitute(T... tokens) {
        return substitute(List.of(tokens));
    }

    public final <T> Substitute<T> substitute(List<T> tokens) {
        return new Substitute<>(tokens);
    }

    @SafeVarargs
    public final <T> Transpose<T> transpose(T... tokens) {
        return transpose(List.of(tokens));
    }

    public final <T> Transpose<T> transpose(List<T> tokens) {
        return new Transpose<>(tokens);
    }

    @SafeVarargs
    public final <T> Equal<T> equal(T... tokens) {
        return equal(List.of(tokens));
    }

    public final <T> Equal<T> equal(List<T> tokens) {
        return new Equal<>(tokens);
    }

    public static final class Insert<T> extends AtPosition<T> {
        public Insert(List<T> target) {
            super(List.of(), target, Operation.INSERT);
        }
    }

    public static final class Delete<T> extends AtPosition<T> {

        public Delete(List<T> source) {
            super(source, List.of(), Operation.DELETE);
        }
    }

    public static final class Substitute<T> {

        private final List<T> source;

        Substitute(List<T> source) {
            this.source = source;
        }

        @SafeVarargs
        public final AtPosition<T> with(T... target) {
            return with(List.of(target));
        }

        public final AtPosition<T> with(List<T> target) {
            return new AtPosition<>(source, target, Operation.SUBSTITUTE);
        }

    }

    public static final class Transpose<T> {

        private final List<T> source;

        Transpose(List<T> source) {
            this.source = source;
        }

        @SafeVarargs
        public final AtPosition<T> to(T... target) {
            return to(List.of(target));
        }

        public final AtPosition<T> to(List<T> target) {
            return new AtPosition<>(source, target, Operation.TRANSPOSE);
        }
    }

    public static final class Equal<T> {

        private final List<T> source;

        Equal(List<T> source) {
            this.source = source;
        }

        @SafeVarargs
        public final AtPosition<T> and(T... target) {
            return and(List.of(target));
        }

        public final AtPosition<T> and(List<T> target) {
            return new AtPosition<>(source, target, Operation.EQUAL);
        }

    }

    public static class AtPosition<T> {

        private final List<T> source;
        private final List<T> target;
        private final Operation operation;

        public AtPosition(List<T> source, List<T> target, Operation operation) {
            this.source = source;
            this.target = target;
            this.operation = operation;
        }

        public final Edit<T> atPosition(int source, int target) {
            return Edit.of(Segment.of(source, this.source), Segment.of(target, this.target), operation);
        }
    }
}
