# Aligner

Determine the minimal Edit path between two lists (source and target). 

This is a Java implementation of a configurable [Damerau Levenshtein](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance) aligner, inspired by [Errant by Chris Bryant](https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py) and [diff_match_patch by Neil Fraser](https://github.com/google/diff-match-patch).

Here are some recommended resources to catch up on Edit Distance in general:
1. [Speech and Language Processing](https://web.stanford.edu/~jurafsky/slp3/2.pdf)
2. [Stanford](https://web.stanford.edu/class/cs124/lec/med.pdf)
3. [Wikipedia](https://en.wikipedia.org/wiki/Edit_distance)

## Features

1. Supports 5 Edit operations - Insert, Delete, Substitute, Transpose and Equal
2. Variable length transposition edit
3. Custom Equality function
4. Custom Comparator to determine transposition
5. Edit Ratio, Distance and Cost given on an Alignment result
6. Powerfull Edit class supports merge operations and functional stream like operations
7. Patch class to patch a list of edits into a target list (beta, see tests)
8. And more...

## Quick Start / Configurable Damerau Levenshtein

```java
// The source and target lists to be aligned.
// An alignment will contain edits that describe how to transform source into target.
List<Integer> source = List.of(1, 3, 3);
List<Integer> target = List.of(1, 2, 3);

// The equality operation is used to determine whether two elements are equal
BiPredicate<Integer, Integer> equalizer = Integer::equals;

// The comparator is used to sort and compare two candidate lists for transposition
Comparator<Integer> comparator = Integer::compareTo;

// The cost function disables substitution for elements with values (3,2) by returning a Double.MAX_VALUE when matched
BiFunction<Integer, Integer, Double> substituteCost = (s, t) -> s == 3 && t == 2 ? Double.MAX_VALUE : 1.0;

// A custom damerau levenshtein aligner
Aligner<Integer> aligner = Aligner.damerauLevenshtein(equalizer, comparator, substituteCost);

// The expected list of edits
List<Edit<Integer>> expected = List.of(
        Edit.builder().equal(1).and(1).atPosition(0, 0),
        Edit.builder().delete(3).atPosition(1, 1),
        Edit.builder().insert(2).atPosition(2, 1),
        Edit.builder().equal(3).and(3).atPosition(2, 2)
);

// Align the two lists
Alignment<Integer> actual = aligner.align(source, target);

// Assert expected results
assertEquals(expected, actual.edits());
assertEquals(2.0, actual.cost());
assertEquals(2.0 / 3.0, actual.distance());
```

Check out the tests for more examples. 

Ping me for comments.
