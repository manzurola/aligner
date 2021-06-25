package edu.guym.aligner;

import edu.guym.aligner.edit.Edit;
import edu.guym.aligner.metrics.Equalizer;
import edu.guym.aligner.metrics.SubstituteCost;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlignerTest {

    @Test
    void customDamerauLevenshtein() {

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
    }


    @Test
    void levenshteinSimple() {
        Aligner<String> aligner = Aligner.levenshtein(String::equals);

        List<String> source = List.of("guy", "is", "good");
        List<String> target = List.of("is", "good", "guy");

        List<Edit<String>> expected = List.of(
                Edit.builder().delete("guy").atPosition(0, 0),
                Edit.builder().equal("is").and("is").atPosition(1, 0),
                Edit.builder().equal("good").and("good").atPosition(2, 1),
                Edit.builder().insert("guy").atPosition(3, 2)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(2.0 / 3.0, actual.distance());
    }

    @Test
    void withDefaultObjectEqualsAndComparator() {
        Aligner<String> aligner = Aligner.damerauLevenshtein();

        List<String> source = List.of("the", "guy", "am");
        List<String> target = List.of("am", "guy", "the");

        List<Edit<String>> expected = List.of(
                Edit.builder().substitute("the").with("am").atPosition(0, 0),
                Edit.builder().equal("guy").and("guy").atPosition(1, 1),
                Edit.builder().substitute("am").with("the").atPosition(2, 2)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(2.0 / 3.0, actual.distance());
    }

    @Test
    void levenshteinTestEmptySource() {
        Aligner<String> aligner = Aligner.levenshtein();

        List<String> source = List.of();
        List<String> target = List.of("", "guy");

        List<Edit<String>> expected = List.of(
                Edit.builder().insert("").atPosition(0, 0),
                Edit.builder().insert("guy").atPosition(0, 1)
        );

        Alignment<String> actual = aligner.align(source, target);

        assertEquals(expected, actual.edits());
        assertEquals(2.0, actual.cost());
        assertEquals(1.0, actual.distance());
    }

    @Test
    void alignerBuilder() {
        Aligner.Builder<String> builder = Aligner.builder();
        Aligner<String> aligner = builder
                .setEqualizer(String::equals)
                .setDeleteCost(source -> 5.0)
                .build();

        List<String> source = List.of("guy");
        List<String> target = List.of("");
        Alignment<String> actual = aligner.align(source, target);

        List<Edit<String>> expected = List.of(
                Edit.builder().substitute("guy").with("").atPosition(0, 0)
        );

        assertEquals(expected, actual.edits());
    }

    @Test
    void withCustomComparator() {
        Aligner<Element> aligner = Aligner.damerauLevenshtein(
                Element::equals,
                Comparator.comparing(element -> element.value.toLowerCase())
        );

        List<Element> source = List.of(new Element("a"), new Element("Guy"), new Element("IS"), new Element("GOOd"));
        List<Element> target = List.of(new Element("a"), new Element("is"), new Element("good"), new Element("guy"));

        List<Edit<Element>> expected = List.of(
                Edit.builder()
                        .transpose(source.subList(1, 4))
                        .to(target.subList(1, 4))
                        .atPosition(1, 1)
        );

        Alignment<Element> alignment = aligner.align(source, target);

        assertEquals(expected, alignment.diffs());
    }

    private static class Element {
        public final String value;

        public Element(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return Objects.equals(value, element.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    @Disabled
    @Test
    void veryLargeAndRandomIntegerListAlignment() {
        List<Integer> source = new Random().ints(100)
                .boxed()
                .collect(Collectors.toList());
        List<Integer> target = new Random().ints(100)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("aligning...");
        Aligner<Integer> aligner = Aligner.damerauLevenshtein();

        long startTime = System.nanoTime();
        Alignment<Integer> alignment = aligner.align(source, target);
        long endTime = System.nanoTime();

        System.out.println("elapsed time millis:" + (endTime - startTime) / 1000000);

        System.out.println(alignment.distance());
    }

}
