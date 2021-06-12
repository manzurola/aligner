package edu.guym.aligner.patch;

import edu.guym.aligner.edit.Edit;

import java.util.ArrayList;
import java.util.List;

public class SubstitutePatcher<T> extends EditPatcher<T> {
    public SubstitutePatcher(Edit<T> edit) {
        super(edit);
    }

    @Override
    public List<T> applyTo(List<T> target) {
        verify(target);
        List<T> result = new ArrayList<>(target);
        int position = getEdit().source().position();
        int size = getEdit().source().size();
        for (int i = 0; i < size; i++) {
            result.remove(position);
        }
        int i = 0;
        for (T token : getEdit().target().tokens()) {
            result.add(position + i, token);
            i++;
        }
        return result;
    }

    @Override
    public List<T> undoFrom(List<T> target) {
        List<T> result = new ArrayList<>(target);
        int position = getEdit().target().position();
        int size = getEdit().target().size();
        for (int i = 0; i < size; i++) {
            result.remove(position);
        }
        int i = 0;
        for (T token : getEdit().source().tokens()) {
            result.add(position + i, token);
            i++;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void verify(List<T> target) throws IllegalStateException {
//        getEdit().source().verify(target);
        if (getEdit().source().position() > target.size()) {
            throw new IllegalStateException("Incorrect patch for getEdit: "
                    + "getEdit original position > target size");
        }
    }
}
