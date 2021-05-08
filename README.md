# Aligner

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

https://github.com/manzurola/aligner/blob/f57d580fc068bf4116df273d51624f36a867a2c8/src/test/java/io/squarebunny/aligner/AlignerTest.java#L25-L54

Check out the tests for more example uses. Ping me for comments.
