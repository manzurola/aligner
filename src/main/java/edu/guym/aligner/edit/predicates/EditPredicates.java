package edu.guym.aligner.edit.predicates;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.Operation;

import java.util.function.Predicate;


public class EditPredicates {

    public static Predicate<Edit<?>> isSubstitute() {
        return edit -> edit.operation().equals(Operation.SUBSTITUTE);
    }

    public static Predicate<Edit<?>> isInsert() {
        return edit -> edit.operation().equals(Operation.INSERT);
    }

    public static Predicate<Edit<?>> isDelete() {
        return edit -> edit.operation().equals(Operation.DELETE);
    }

    public static Predicate<Edit<?>> isTranspose() {
        return edit -> edit.operation().equals(Operation.TRANSPOSE);
    }

    public static Predicate<Edit<?>> isEqual() {
        return edit -> edit.operation().equals(Operation.EQUAL);
    }

    public static Predicate<Edit<?>> ofSize(int sourceSize, int targetSize) {
        return edit -> edit.source().size() == sourceSize && edit.target().size() == targetSize;
    }

    public static Predicate<Edit<?>> ofMaxSize(int maxSourceSize, int maxTargetSize) {
        return edit -> edit.source().size() <= maxSourceSize && edit.target().size() <= maxTargetSize;
    }

    public static Predicate<Edit<?>> ofSizeOneToOne() {
        return ofSize(1, 1);
    }

}
