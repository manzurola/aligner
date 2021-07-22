package com.github.manzurola.aligner.patch;

import java.util.List;

public interface Patcher<T> {

    List<T> applyTo(List<T> target);

    List<T> undoFrom(List<T> target);
}
