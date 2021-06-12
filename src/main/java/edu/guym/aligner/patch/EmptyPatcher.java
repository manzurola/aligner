package edu.guym.aligner.patch;

import edu.guym.aligner.edit.Edit;

import java.util.List;

public class EmptyPatcher<T> extends EditPatcher<T> {
    public EmptyPatcher(Edit<T> edit) {
        super(edit);
    }

    @Override
    public void verify(List<T> target) throws IllegalStateException {
    }

    @Override
    public List<T> applyTo(List<T> target) {
        return target;
    }

    @Override
    public List<T> undoFrom(List<T> target) {
        return target;
    }
}
