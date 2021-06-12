package io.squarebunny.aligner.edit.builder;

import io.squarebunny.aligner.edit.*;

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

    public final <T> TypeResolvingBuilder<T> ofType(Operation operation) {
        return new TypeResolvingBuilder<>(operation);
    }

    public static final class Insert<T> extends AtPosition<T> {
        public Insert(List<T> target) {
            super(List.of(), target, InsertEdit::new);
        }
    }

    public static final class Delete<T> extends AtPosition<T> {

        public Delete(List<T> source) {
            super(source, List.of(), DeleteEdit::new);
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
            return new AtPosition<>(source, target, SubstituteEdit::new);
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
            return new AtPosition<>(source, target, TransposeEdit::new);
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
            return new AtPosition<>(source, target, EqualEdit::new);
        }

    }

    public static class AtPosition<T> {

        private final List<T> source;
        private final List<T> target;
        private final BiFunction<Segment<T>, Segment<T>, Edit<T>> creator;

        public AtPosition(List<T> source, List<T> target, BiFunction<Segment<T>, Segment<T>, Edit<T>> creator) {
            this.source = source;
            this.target = target;
            this.creator = creator;
        }

        public final Edit<T> atPosition(int source, int target) {
            return creator.apply(Segment.of(source, this.source), Segment.of(target, this.target));
        }
    }

    public static class TypeResolvingBuilder<T> {
        private final Operation operation;
        private Segment<T> source;
        private Segment<T> target;

        public TypeResolvingBuilder(Operation operation) {
            this.operation = operation;
        }

        public TypeResolvingBuilder<T> source(Segment<T> source) {
            this.source = source;
            return this;
        }

        public TypeResolvingBuilder<T> target(Segment<T> target) {
            this.target = target;
            return this;
        }

        public Edit<T> build() {
            switch (operation) {
                case EQUAL:
                    break;
                case SUBSTITUTE:
                    return new SubstituteEdit<>(source, target);
                    break;
                case INSERT:
                    break;
                case DELETE:
                    break;
                case TRANSPOSE:
                    break;
            }
        }
    }
}
