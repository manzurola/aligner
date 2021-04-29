package io.squarebunny.aligner.edit.comparator;

import io.squarebunny.aligner.edit.AbstractEdit;

import java.io.Serializable;
import java.util.Comparator;

public class EditComparator implements Comparator<AbstractEdit<?, ?>>, Serializable {
    private static final long serialVersionUID = 1L;
    public static final Comparator<AbstractEdit<?, ?>> INSTANCE = new EditComparator();

    public EditComparator() {
    }

    @Override
    public int compare(AbstractEdit<?, ?> a, AbstractEdit<?, ?> b) {
        int sourceComparison = Integer.compare(a.source().position(), b.source().position());
        int targetComparison = Integer.compare(a.target().position(), b.target().position());
        return sourceComparison != 0 ? sourceComparison : targetComparison;
    }
}