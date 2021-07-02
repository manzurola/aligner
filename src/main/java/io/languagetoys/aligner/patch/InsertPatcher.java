package io.languagetoys.aligner.patch;

import io.languagetoys.aligner.edit.Edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertPatcher<T> extends EditPatcher<T> {

    public InsertPatcher(Edit<T> edit) {
        super(edit);
    }

    @Override
    public void verify(List<T> target) throws IllegalStateException {
        Optional.of(target)
                .filter(ts -> getEdit().source().position() <= target.size())
                .orElseThrow(() -> new IllegalStateException("Incorrect patch for getEdit: "
                        + "getEdit original position > target size"));
    }

    @Override
    public List<T> applyTo(List<T> target) {
        verify(target);
        List<T> result = new ArrayList<>(target);
        int position = getEdit().source().position();
        List<T> tokens = getEdit().target().tokens();
        for (int i = 0; i < tokens.size(); i++) {
            result.add(position + i, tokens.get(i));
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
        return result;
    }
}
