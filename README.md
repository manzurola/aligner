# Aligner

Determine the optimal edit path between two lists.

This is a Java implementation of a parameterizable [Damerau Levenshtein](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance) aligner, inspired by [Errant by Chris Bryant](https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py) and [diff_match_patch by Neil Fraser](https://github.com/google/diff-match-patch).

![maven](https://github.com/manzurola/aligner/actions/workflows/maven.yml/badge.svg)

## Features

* Supports 5 Edit operations - Insert, Delete, Substitute, Transpose and Equal
* Variable length transposition edit
* Custom Equality function
* Custom Comparator to determine transposition
* Edit Ratio, Distance and Cost given on an Alignment result
* Powerfull Edit class supports merge operations and functional stream like operations
* Patch class to patch a list of edits into a target list (beta, see tests)
* And more...

## Installation

This library is currenlty available only on Github Packages.
You will need to [configure maven to work with Github's repo](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry). This includes changing your settings.xml file and generating a Private Access Token in github. Make sure to enable snapshots.
Once configured, add this to your `pom.xml`:

```
<dependency>
  <groupId>io.languagetoys</groupId>
  <artifactId>aligner</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## Quick Start

```java
// create a new damerau levenshtein aligner
Aligner<String> aligner = Aligner.damerauLevenshtein();

// align source and target
Alignment<String> alignment = aligner.align(source, target);

// inspect edits
alignment.edits().forEach(System.out::println); // inspect edits
```

## Advanced - Custom Aligner

```java
// The source and target lists to be aligned.
// An alignment will contain edits that describe how to transform source into target.
List<Integer> source = List.of(1, 3, 3);
List<Integer> target = List.of(1, 2, 3);

// The equality operation is used to determine whether two elements are equal
Equalizer<Integer> equalizer = Integer::equals;

// The comparator is used to sort and compare two candidate lists for transposition
Comparator<Integer> comparator = Integer::compareTo;

// This cost function disables substitution for elements with values (3,2) by returning a Double.MAX_VALUE when matched
SubstituteCost<Integer> substituteCost = (s, t) -> s == 3 && t == 2 ? Double.MAX_VALUE : 1.0;

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

## Performance

Aligning two Integer lists of size 100 takes around 1200ms on my 3.5 GHz Dual-Core Intel Core i7 Macbook Pro.
With levenshtein only (no traspositions) it takes around 70 ms.

## More use cases

Check out the [tests](https://github.com/manzurola/aligner/blob/67618def27d18e0e29e4f07905a4509907b379a3/src/test/java/io/squarebunny/aligner/AlignerTest.java) for more examples. 

## Contributions

[Contributions](https://github.com/manzurola/aligner/blob/a39d2719394fa258d3193e8258231950a3647920/CONTRIBUTING.md) are welcome.
