package io.squarebunny.aligner;

import io.squarebunny.aligner.alignment.Alignment;
import io.squarebunny.aligner.edit.Edit;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlignerTest {

    /**
     * Configure and aligner with a custom equalizer, comparator and substitution cost function
     * The equality operation is used to determine whether two elements are equal
     * The comparator is used to sort and compare two candidate lists for transposition
     * The cost function disables substitution for elements with values (3,2) by returning a Double.MAX_VALUE when matched
     */
    @Test
    void customDamerauLevenshtein() {

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
    void withCustomComparator() {
        Aligner<Element> aligner = Aligner.damerauLevenshtein(
                Element::equals,
                Comparator.comparing(element -> element.value.toLowerCase())
        );

        List<Element> source = List.of(new Element("Guy"), new Element("IS"), new Element("GOOd"));
        List<Element> target = List.of(new Element("is"), new Element("good"), new Element("guy"));

        List<Edit<Element>> expected = List.of(
                Edit.builder().transpose(source).to(target).atPosition(0, 0)
        );

        Alignment<Element> alignment = aligner.align(source, target);

        assertEquals(expected, alignment.edits());
    }

    private static class Element {
        public final String value;

        public Element(String value) {
            this.value = value;
        }
    }
}
