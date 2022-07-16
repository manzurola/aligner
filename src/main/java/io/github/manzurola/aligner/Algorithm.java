package io.github.manzurola.aligner;

import java.util.List;

public interface Algorithm<T> {

    Alignment<T> align(List<T> source, List<T> target);
}
