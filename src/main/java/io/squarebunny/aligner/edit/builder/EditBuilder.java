package io.squarebunny.aligner.edit.builder;

import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.Operation;
import io.squarebunny.aligner.edit.Segment;

import java.util.List;

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
            super(Operation.INSERT, List.of(), target);
        }
    }

    public static final class Delete<T> extends AtPosition<T> {

        public Delete(List<T> source) {
            super(Operation.DELETE, source, List.of());
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
            return new AtPosition<>(Operation.SUBSTITUTE, source, target);
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
            return new AtPosition<>(Operation.TRANSPOSE, source, target);
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
            return new AtPosition<>(Operation.EQUAL, source, target);
        }

    }

    public static class AtPosition<T> {

        private final Operation operation;
        private final List<T> source;
        private final List<T> target;

        AtPosition(Operation operation,
                          List<T> source,
                          List<T> target) {
            this.operation = operation;
            this.source = source;
            this.target = target;
        }

        public final Edit<T> atPosition(int source, int target) {
            return Edit.of(operation, Segment.of(source, this.source), Segment.of(target, this.target));
        }
    }

}
