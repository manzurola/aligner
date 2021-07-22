package com.github.manzurola.aligner.patch;

import com.github.manzurola.aligner.edit.Edit;

import java.util.List;

public abstract class EditPatcher<T> implements Patcher<T> {

    private final Edit<T> edit;

    public EditPatcher(Edit<T> edit) {
        this.edit = edit;
    }

    public Edit<T> getEdit() {
        return edit;
    }

    public abstract void verify(List<T> target) throws IllegalStateException;

    public static <T> EditPatcher<T> forEdit(Edit<T> delta) {
        switch (delta.operation()) {
            case INSERT:
                return new InsertPatcher<>(delta);
            case DELETE:
                return new DeletePatcher<>(delta);
            case SUBSTITUTE:
            case TRANSPOSE:
                return new SubstitutePatcher<>(delta);
            case EQUAL:
            default:
                return new EmptyPatcher<>(delta);
        }
    }
}
