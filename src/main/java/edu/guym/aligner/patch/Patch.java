package edu.guym.aligner.patch;


import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.EditComparator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public final class Patch<T> implements Patcher<T> {

    private final List<Edit<T>> edits;

    public Patch() {
        edits = new LinkedList<>();
    }

    public Patch(List<Edit<T>> edits) {
        this.edits = new LinkedList<>(edits);
        this.edits.sort(EditComparator.INSTANCE);
    }

    public Patch<T> add(Edit<T> edit) {
        List<Edit<T>> copy = new ArrayList<>(this.edits);
        copy.add(edit);
        return new Patch<>(copy);
    }

    /**
     * Apply this patch to the given target
     *
     * @return the patched text
     * @throws IllegalStateException if can't apply patch
     */
    public List<T> applyTo(List<T> target) {
        List<T> result = new LinkedList<>(target);
        ListIterator<Edit<T>> it = getEdits().listIterator(edits.size());
        while (it.hasPrevious()) {
            Edit<T> edit = it.previous();
            result = EditPatcher.forEdit(edit).applyTo(result);
        }
        return result;
    }

    /**
     * Restore the text to original. Opposite to applyTo() method.
     *
     * @param target the given target
     * @return the restored text
     */
    public List<T> undoFrom(List<T> target) {
        List<T> result = new LinkedList<>(target);
        ListIterator<Edit<T>> it = getEdits().listIterator(edits.size());
        while (it.hasPrevious()) {
            Edit<T> edit = it.previous();
            result = EditPatcher.forEdit(edit).undoFrom(result);
        }
        return result;
    }

    /**
     * Get the list of computed deltas
     *
     * @return the deltas
     */
    public List<Edit<T>> getEdits() {
        return edits;
    }
}
