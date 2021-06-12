package edu.guym.aligner.impl;


import edu.guym.aligner.Aligner;
import edu.guym.aligner.alignment.Alignment;
import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.edit.Operation;
import edu.guym.aligner.edit.Segment;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.guym.aligner.edit.Operation.*;

/**
 * This implementation is a port of <a href="https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py">
 * https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py</a>.
 */
public class DamerauLevenshtein<T> implements Aligner<T> {

    private final CostFunction<T> costFunction;
    private final BiPredicate<T, T> equalizer;
    private final Comparator<T> comparator;

    public DamerauLevenshtein(CostFunction<T> costFunction,
                              BiPredicate<T, T> equalizer,
                              Comparator<T> comparator) {
        this.costFunction = Objects.requireNonNull(costFunction);
        this.equalizer = Objects.requireNonNull(equalizer);
        this.comparator = comparator;
    }

    @Override
    public Alignment<T> align(List<T> source,
                              List<T> target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        int originalLength = source.size();
        int correctedLength = target.size();

        double[][] costMatrix = initCostMatrix(originalLength, correctedLength);
        String[][] opMatrix = initOpMatrix(originalLength, correctedLength);

        for (int i = 0; i < originalLength; i++) {
            for (int j = 0; j < correctedLength; j++) {

                T sourceToken = source.get(i);
                T targetToken = target.get(j);

                if (equalizer.test(sourceToken, targetToken)) {
                    costMatrix[i + 1][j + 1] = costMatrix[i][j];
                    opMatrix[i + 1][j + 1] = "M";

                } else {
                    double delCost = costMatrix[i][j + 1] + costFunction.deleteCost(sourceToken);
                    double insCost = costMatrix[i + 1][j] + costFunction.insertCost(targetToken);
                    double subCost = costMatrix[i][j] + costFunction.substituteCost(sourceToken, targetToken);

                    // Transpositions require >=2 tokens
                    // Traverse the diagonal while there is not a Match.
                    double transCost = Double.MAX_VALUE;
                    int k = 1;
                    if (comparator != null) {
                        while (i - k >= 0 && j - k >= 0 && costMatrix[i - k + 1][j - k + 1] != costMatrix[i - k][j - k]) {

                            List<T> sourceSublist = source.subList(i - k, i + 1);
                            List<T> targetSublist = target.subList(j - k, j + 1);
                            boolean isTransposed = isTransposed(sourceSublist, targetSublist);

                            if (isTransposed) {
                                transCost = costMatrix[i - k][j - k] + costFunction.transposeCost(
                                        sourceSublist,
                                        targetSublist);
                                break;
                            }

                            k += 1;
                        }
                    }
                    // Costs
                    // TODO fix this hack - using the index of the list at the switch below
                    List<Double> costs = Arrays.asList(transCost, subCost, insCost, delCost);
                    int minCostIndex = costs.indexOf(costs.stream().min(Double::compareTo).get());
                    costMatrix[i + 1][j + 1] = costs.get(minCostIndex);
                    switch (minCostIndex) {
                        case 0: {
                            opMatrix[i + 1][j + 1] = "T" + (k + 1);
                            break;
                        }
                        case 1: {
                            opMatrix[i + 1][j + 1] = "S";
                            break;
                        }
                        case 2: {
                            opMatrix[i + 1][j + 1] = "I";
                            break;
                        }
                        case 3: {
                            opMatrix[i + 1][j + 1] = "D";
                            break;
                        }
                    }
                }
            }
        }

        List<Edit<T>> edits = getCheapestAlignmentSequence(opMatrix, source, target);
        double cost = costMatrix[originalLength][correctedLength];
        double length = Math.max(source.size(), target.size());
        return Alignment.of(edits, cost, cost / length);
    }

    private double[][] initCostMatrix(int m, int n) {
        double[][] costMatrix = new double[m + 1][n + 1];
        for (double[] matrix : costMatrix) {
            Arrays.fill(matrix, 0.0);
        }
        // Fill in the edges
        for (int i = 1; i < m + 1; i++) {
            costMatrix[i][0] = costMatrix[i - 1][0] + 1;
        }
        for (int j = 1; j < n + 1; j++) {
            costMatrix[0][j] = costMatrix[0][j - 1] + 1;
        }
        return costMatrix;
    }

    private String[][] initOpMatrix(int m, int n) {
        String[][] opMatrix = new String[m + 1][n + 1];
        for (String[] matrix : opMatrix) {
            Arrays.fill(matrix, "0");
        }
        // Fill in the edges
        for (int i = 1; i < m + 1; i++) {
            opMatrix[i][0] = "D";
        }
        for (int j = 1; j < n + 1; j++) {
            opMatrix[0][j] = "I";
        }
        return opMatrix;
    }

    private boolean isTransposed(List<T> source, List<T> target) {
        List<T> sourceSublist = source
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        List<T> targetSublist = target
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return IntStream.range(0, sourceSublist.size())
                .allMatch(index -> comparator.compare(
                        sourceSublist.get(index),
                        targetSublist.get(index)) == 0);
    }

    private List<Edit<T>> getCheapestAlignmentSequence(String[][] opMatrix,
                                                       List<T> source,
                                                       List<T> target) {
        int i = opMatrix.length - 1;
        int j = opMatrix[0].length - 1;
        List<Edit<T>> sequence = new ArrayList<>();
        // Work backwards from bottom right until we hit top left
        while (i + j != 0) {
            // Get the edit operation in the current cell
            String op = opMatrix[i][j];
            // Matches and substitutions
            if (Arrays.asList("M", "S").contains(op)) {
                sequence.add(createEdit(op, i - 1, i, j - 1, j, source, target));
                i -= 1;
                j -= 1;
            }
            // Deletions
            else if (op.equals("D")) {
                sequence.add(createEdit(op, i - 1, i, j, j, source, target));
                i -= 1;
            }
            // Insertions
            else if (op.equals("I")) {
                sequence.add(createEdit(op, i, i, j - 1, j, source, target));
                j -= 1;
            }
            // Transpositions
            else {
                // Get the size of the transposition (TODO fix this hack)
                int k = Integer.parseInt(op.substring(1));
                sequence.add(createEdit(op, i - k, i, j - k, j, source, target));
                i -= k;
                j -= k;
            }
        }

        Collections.reverse(sequence);
        return sequence;
    }

    private Edit<T> createEdit(String op,
                               int originalStart,
                               int originalEnd,
                               int correctedStart,
                               int correctedEnd,
                               List<T> source,
                               List<T> target) {

        return Edit.of(
                resolveOperation(op),
                Segment.of(originalStart, source.subList(originalStart, originalEnd)),
                Segment.of(correctedStart, target.subList(correctedStart, correctedEnd))
        );
    }

    private Operation resolveOperation(String op) {
        if (op.equals("M")) {
            return EQUAL;
        }
        if (op.equals("S")) {
            return SUBSTITUTE;
        }
        if (op.equals("I")) {
            return INSERT;
        }
        if (op.equals("D")) {
            return DELETE;
        }
        if (op.startsWith("T")) {
            return TRANSPOSE;
        }
        throw new RuntimeException("Illegal op " + op); // should not come to this
    }

}
