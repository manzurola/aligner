package com.github.manzurola.aligner;


import com.github.manzurola.aligner.edit.Edit;
import com.github.manzurola.aligner.edit.Operation;
import com.github.manzurola.aligner.edit.Segment;
import com.github.manzurola.aligner.metrics.*;

import java.util.*;

import static com.github.manzurola.aligner.edit.Operation.*;

/**
 * This implementation is a port of <a href="https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py">
 * https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py</a>.
 */
final class AlignerImpl<T> implements Aligner<T> {

    private final Equalizer<T> equalizer;
    private final Comparator<T> comparator;
    private final DeleteCost<T> deleteCost;
    private final InsertCost<T> insertCost;
    private final SubstituteCost<T> substituteCost;
    private final TransposeCost<T> transposeCost;

    public AlignerImpl(Equalizer<T> equalizer,
                       Comparator<T> comparator,
                       DeleteCost<T> deleteCost,
                       InsertCost<T> insertCost,
                       SubstituteCost<T> substituteCost,
                       TransposeCost<T> transposeCost) {
        this.equalizer = Objects.requireNonNull(equalizer);
        this.comparator = comparator;
        this.deleteCost = Objects.requireNonNull(deleteCost);
        this.insertCost = Objects.requireNonNull(insertCost);
        this.substituteCost = Objects.requireNonNull(substituteCost);
        this.transposeCost = Objects.requireNonNull(transposeCost);
    }

    @Override
    public final Alignment<T> align(List<T> source,
                                    List<T> target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        T[] sourceArr = (T[]) source.toArray();
        T[] targetArr = (T[]) target.toArray();

        int originalLength = sourceArr.length;
        int correctedLength = targetArr.length;

        Cell[][] matrix = initMatrix(originalLength, correctedLength);

        for (int i = 0; i < originalLength; i++) {
            for (int j = 0; j < correctedLength; j++) {

                T sourceToken = sourceArr[i];
                T targetToken = targetArr[j];

                if (equalizer.isEqual(sourceToken, targetToken)) {
                    matrix[i + 1][j + 1].cost = matrix[i][j].cost;
                    matrix[i + 1][j + 1].op = EQUAL;

                } else {
                    double delCost = matrix[i][j + 1].cost + deleteCost.getCost(sourceToken);
                    double insCost = matrix[i + 1][j].cost + insertCost.getCost(targetToken);
                    double subCost = matrix[i][j].cost + substituteCost.getCost(sourceToken, targetToken);

                    // Transpositions require >=2 tokens
                    // Traverse the diagonal while there is not a Match.
                    double transCost = Double.MAX_VALUE;
                    int k = 1;
                    if (comparator != null) {
                        while (i - k >= 0 &&
                               j - k >= 0 &&
                               matrix[i - k + 1][j - k + 1].cost != matrix[i - k][j - k].cost) {

                            T[] sourceSub = Arrays.copyOfRange(sourceArr, i - k, i + 1);
                            T[] targetSub = Arrays.copyOfRange(targetArr, j - k, j + 1);

                            boolean isTransposed = isTransposed(sourceSub, targetSub);

                            if (isTransposed) {
                                transCost = matrix[i - k][j - k].cost + transposeCost.getCost(
                                        sourceSub,
                                        targetSub);
                                break;
                            }

                            k += 1;
                        }
                    }
                    // Costs
                    // TODO fix this hack - using the index of the list at the switch below
                    List<Double> costs = Arrays.asList(transCost, subCost, insCost, delCost);
                    int minCostIndex = costs.indexOf(costs.stream().min(Double::compareTo).get());
                    matrix[i + 1][j + 1].cost = costs.get(minCostIndex);
                    switch (minCostIndex) {
                        case 0: {
                            matrix[i + 1][j + 1].op = TRANSPOSE;
                            matrix[i + 1][j + 1].k = k + 1;
                            break;
                        }
                        case 1: {
                            matrix[i + 1][j + 1].op = SUBSTITUTE;
                            break;
                        }
                        case 2: {
                            matrix[i + 1][j + 1].op = INSERT;
                            break;
                        }
                        case 3: {
                            matrix[i + 1][j + 1].op = DELETE;
                            break;
                        }
                    }
                }
            }
        }

        List<Edit<T>> edits = backtrack(matrix, source, target);
        double cost = matrix[originalLength][correctedLength].cost;
        return Alignment.of(edits, cost);
    }

    private Cell[][] initMatrix(int m, int n) {

        Cell[][] matrix = new Cell[m + 1][n + 1];
        for (int i = 0, ilen = matrix.length; i < ilen; i++) {
            for (int j = 0, jlen = matrix[i].length; j < jlen; j++) {
                matrix[i][j] = new Cell(0.0);
            }
        }
        // Fill in the edges
        for (int i = 1; i < m + 1; i++) {
            matrix[i][0].cost = matrix[i - 1][0].cost + 1;
            matrix[i][0].op = DELETE;
        }
        for (int j = 1; j < n + 1; j++) {
            matrix[0][j].cost = matrix[0][j - 1].cost + 1;
            matrix[0][j].op = INSERT;
        }
        return matrix;
    }

    private boolean isTransposed(T[] source,
                                 T[] target) {
        if (source.length == 2) {
            //most common scenario, compare permutations without sort
            T source1 = source[0];
            T source2 = source[1];
            T target1 = target[0];
            T target2 = target[1];
            boolean identical = comparator.compare(source1, target1) == 0 && comparator.compare(source2, target2) == 0;
            boolean reversed = comparator.compare(source1, target2) == 0 && comparator.compare(source2, target1) == 0;
            return identical || reversed;
        }

        Arrays.sort(source, comparator);
        Arrays.sort(target, comparator);
        boolean match = false;
        for (int i = 0; i < source.length; i++) {
            T s = source[i];
            T t = target[i];
            match = comparator.compare(s, t) == 0;
            if (!match) {
                break;
            }
        }
        return match;
    }

    private List<Edit<T>> backtrack(Cell[][] matrix,
                                    List<T> source,
                                    List<T> target) {
        int i = matrix.length - 1;
        int j = matrix[0].length - 1;
        List<Edit<T>> sequence = new ArrayList<>();
        // Work backwards from bottom right until we hit top left
        while (i + j != 0) {
            // Get the edit operation in the current cell
            Operation op = matrix[i][j].op;
            switch (op) {
                case EQUAL:
                case SUBSTITUTE:
                    sequence.add(createEdit(op, i - 1, i, j - 1, j, source, target));
                    i -= 1;
                    j -= 1;
                    break;
                case DELETE:
                    sequence.add(createEdit(op, i - 1, i, j, j, source, target));
                    i -= 1;
                    break;
                case INSERT:
                    sequence.add(createEdit(op, i, i, j - 1, j, source, target));
                    j -= 1;
                    break;
                case TRANSPOSE:
                    int k = matrix[i][j].k;
                    sequence.add(createEdit(op, i - k, i, j - k, j, source, target));
                    i -= k;
                    j -= k;
                    break;
            }
        }

        Collections.reverse(sequence);
        return sequence;
    }

    private Edit<T> createEdit(Operation op,
                               int originalStart,
                               int originalEnd,
                               int correctedStart,
                               int correctedEnd,
                               List<T> source,
                               List<T> target) {

        return Edit.of(
                op,
                Segment.of(originalStart, source.subList(originalStart, originalEnd)),
                Segment.of(correctedStart, target.subList(correctedStart, correctedEnd))
        );
    }

    static class Cell {
        public double cost;
        public Operation op;
        public int k;

        public Cell(double cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "{" + cost + "," + op + ", " + k + '}';
        }
    }

}
