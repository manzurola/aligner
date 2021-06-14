# Aligner

Determine the minimal Edit path between two lists (source and target). 

This is a Java implementation of a parameterizable [Damerau Levenshtein](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance) aligner, inspired by [Errant by Chris Bryant](https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py) and [diff_match_patch by Neil Fraser](https://github.com/google/diff-match-patch).

I'm using it for annotating grammar mistakes in parallel texts and for speech pronunciation assessment.

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

## Use via github packages

To the right you will see the packages tab. Follow through it and you will see the dependency that should be added to your project's maven file.

You will need to [configure maven to work with Github's repo](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).
This includes changing your settings.xml file and generating a Private Access Token in github. Make sure to enable snapshots.

## Quick Start

### Step 1 - Create an Aligner

```java
Aligner<String> aligner = Aligner.damerauLevenshtein();
```

### Step 2 - Align

```java
Alignment<String> alignment = aligner.align(source, target);
```

## Advanced - Configurable Aligner with expected alignment test

```java
// The source and target lists to be aligned.
// An alignment will contain edits that describe how to transform source into target.
List<Integer> source = List.of(1, 3, 3);
List<Integer> target = List.of(1, 2, 3);

// The equality operation is used to determine whether two elements are equal
BiPredicate<Integer, Integer> equalizer = Integer::equals;

// The comparator is used to sort and compare two candidate lists for transposition
Comparator<Integer> comparator = Integer::compareTo;

// This cost function disables substitution for elements with values (3,2) by returning a Double.MAX_VALUE when matched
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

# Performance

Aligning two Integer lists of size 100 takes around 1500ms on my 3.5 GHz Dual-Core Intel Core i7 Macbook Pro.
Considerably slow. The heap size is also not great and explodes with two lists of size 1000.
Definitely room for improvement!

# More Use Cases

Check out the [tests](https://github.com/manzurola/aligner/blob/67618def27d18e0e29e4f07905a4509907b379a3/src/test/java/io/squarebunny/aligner/AlignerTest.java) for more examples. 

# Contributions

[Contributions](https://github.com/manzurola/aligner/blob/a39d2719394fa258d3193e8258231950a3647920/CONTRIBUTING.md) are welcome.
