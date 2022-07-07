package io.github.manzurola.aligner.patch;

import io.github.manzurola.aligner.edit.Edit;

import java.util.ArrayList;
import java.util.List;

public class DeletePatcher<T> extends EditPatcher<T> {

    public DeletePatcher(Edit<T> edit) {
        super(edit);
    }


    @Override
    public void verify(List<T> target) throws IllegalStateException {
//        getOriginal().verify(target);
    }

    @Override
    public List<T> applyTo(List<T> target) {
        verify(target);
        List<T> buffer = new ArrayList<>(target);
        int position = getEdit().source().position();
        int size = getEdit().source().size();
        for (int i = 0; i < size; i++) {
            buffer.remove(position);
        }
        return buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> undoFrom(List<T> target) {
        int position = getEdit().target().position();
        List<T> tokens = getEdit().source().tokens();
        List<T> buffer = new ArrayList<>(target);
        for (int i = 0; i < tokens.size(); i++) {
            buffer.add(position + i, tokens.get(i));
        }
        return buffer;
    }
}
