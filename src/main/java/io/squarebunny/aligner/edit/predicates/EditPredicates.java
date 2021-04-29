package io.squarebunny.aligner.edit.predicates;

import io.squarebunny.aligner.edit.AbstractEdit;
import io.squarebunny.aligner.edit.Edit;
import io.squarebunny.aligner.edit.Operation;

import java.util.function.Predicate;


public class EditPredicates {

    public static Predicate<? super Edit<?>> isSubstitute() {
        return edit -> edit.operation().equals(Operation.SUBSTITUTE);
    }

    public static <T extends AbstractEdit<?, ?>> Predicate<T> isInsert() {
        return edit -> edit.operation().equals(Operation.INSERT);
    }

    public static <T extends AbstractEdit<?, ?>> Predicate<T> isDelete() {
        return edit -> edit.operation().equals(Operation.DELETE);
    }

    public static <T extends AbstractEdit<?, ?>> Predicate<T> isTranspose() {
        return edit -> edit.operation().equals(Operation.TRANSPOSE);
    }

    public static <T extends AbstractEdit<?, ?>> Predicate<T> isEqual() {
        return edit -> edit.operation().equals(Operation.EQUAL);
    }

    public static Predicate<Edit<?>> ofSize(int sourceSize, int targetSize) {
        return edit -> edit.source().size() == sourceSize && edit.target().size() == targetSize;
    }

    public static Predicate<? super Edit<?>> ofMaxSize(int maxSourceSize, int maxTargetSize) {
        return edit -> edit.source().size() <= maxSourceSize && edit.target().size() <= maxTargetSize;
    }

    public static Predicate<Edit<?>> ofSizeOneToOne() {
        return ofSize(1, 1);
    }

}
