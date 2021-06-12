package edu.guym.aligner.edit;

import java.util.Comparator;

public enum EditComparator implements Comparator<Edit<?>> {

    INSTANCE;

    @Override
    public int compare(Edit<?> a, Edit<?> b) {
        int sourceComparison = Integer.compare(a.source().position(), b.source().position());
        int targetComparison = Integer.compare(a.target().position(), b.target().position());
        return sourceComparison != 0 ? sourceComparison : targetComparison;
    }
}