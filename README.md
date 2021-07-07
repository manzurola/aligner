# Aligner 🤖

Aligner computes a backtrace of the minimum edit distance using the [Damerau Levenshtein](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance) algorithm.

Inspired by [Errant by Chris Bryant](https://github.com/chrisjbryant/errant/blob/master/errant/alignment.py)
and [diff_match_patch by Neil Fraser](https://github.com/google/diff-match-patch).

![maven](https://github.com/manzurola/aligner/actions/workflows/maven.yml/badge.svg)

## Prerequisits

Before you begin, ensure you have met the following requirements:

* You have Java 11 installed.

## Installing Aligner

Available as a Maven dependency via [GitHub Packages](https://github.com/orgs/LanguageToys/packages?repo_name=aligner).  

See GitHub documentation on [installing a package](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package).

## Using Aligner

To use Aligner in code, follow these steps:

```java
// Prepare source and target lists to be aligned
List<Integer> source = List.of(1, 3, 3);
List<Integer> target = List.of(1, 2, 3);

// Create a new aligner via one of the available static factory methods
Aligner<String> aligner = Aligner.damerauLevenshtein();

// Align source and target
Alignment<String> alignment = aligner.align(source,target);

// Get the cost and distance of the alignment
double cost = alignment.cost();
double distance = alignment.distance();
System.out.printf("Cost: %s, distance: %s%n", cost, distance);

// Inspect individual edits
for (Edit<Integer> edit : alignment.edits()) {
    Operation op = edit.operation();
    Segment<Integer> s = edit.source();
    Segment<Integer> t = edit.target();
    System.out.printf("Operation: %s, source: %s, target: %s%n", op, s, t);
}
```

You can also customise an Aligner with your own metrics:

```java
// The equality operation is used to determine whether two elements are equal
Equalizer<Integer> equalizer = Integer::equals;

// The comparator is used to sort and compare two candidate lists for transposition
Comparator<Integer> comparator = Integer::compareTo;

// This cost function disables substitution for elements with values (3,2) by returning a Double.MAX_VALUE
// when matched
SubstituteCost<Integer> substituteCost = (s, t) -> s == 3 && t == 2 ? Double.MAX_VALUE : 1.0;

// A custom damerau levenshtein aligner
Aligner<Integer> aligner = Aligner.damerauLevenshtein(equalizer, comparator, substituteCost);
```

## Contributions

To contribute to Aligner, follow these steps:

1. Fork this repository.
2. Create a branch: `git checkout -b <branch_name>`.
3. Make your changes and commit them: `git commit -m '<commit_message>'`
4. Push to the original branch: `git push origin <project_name>/<location>`
5. Create the pull request.

Alternatively see the GitHub documentation on [creating a pull request](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).

        
## Contributors
        
Thanks to the following people who have contributed to this project:
        
* [@manzurola](https://github.com/manzurola) 🐈        

## Contact

If you want to contact me you can reach me at [guy.manzurola@gmail.com](guy.manzurola@gmail.com).

## License
        
This project uses the following license: [MIT](https://github.com/LanguageToys/aligner/blob/555fd35e842feb8d899d7197a1965ea01bc74c95/LICENSE).
