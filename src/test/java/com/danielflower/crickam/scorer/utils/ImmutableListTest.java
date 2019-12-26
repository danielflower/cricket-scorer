package com.danielflower.crickam.scorer.utils;

import com.danielflower.crickam.scorer.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ImmutableListTest {

    @Test
    public void addingToListDoesNotChangeOriginal() {
        ImmutableList<String> one = new ImmutableList<>();
        ImmutableList<String> two = one.add("Hello");
        assertThat(one.isEmpty(), is(true));
        assertThat(one.size(), is(0));
        assertThat(one.iterator().hasNext(), is(false));
        assertThat(two.isEmpty(), is(false));
        assertThat(two.size(), is(1));
        assertThat(two, contains("Hello"));
        assertThat(two.add("three").add("four").stream().collect(Collectors.toList()), contains("Hello", "three", "four"));
        assertThat(two, contains("Hello"));

        assertThat(one.contains("Hello"), is(false));
        assertThat(two.contains("Hello"), is(true));
    }

    @Test
    public void ofCreatesAList() {
        assertThat(ImmutableList.of("one"), contains("one"));
        ImmutableList<String> three = ImmutableList.of("one", "two", "three");
        assertThat(three, contains("one", "two", "three"));
        assertThat(three.size(), is(3));

        assertThat(three, equalTo(new ImmutableList<>(asList("one", "two", "three"))));
    }

    @Test
    public void listsAreEqualIfViewOfCollectionHasEqualItems() {
        ImmutableList<String> two = ImmutableList.of("one", "two");
        ImmutableList<String> three = two.add("three");
        assertThat(three, equalTo(three));
        assertThat(three, equalTo(new ImmutableList<>(asList("one", "two", "three"))));
        assertThat(three, not(equalTo(two)));
    }

    @Test
    public void lastReturnsOptional() {
        ImmutableList<String> list = new ImmutableList<>();
        assertThat(list.last(), is(Optional.empty()));
        assertThat(list.add("one").last(), is(Optional.of("one")));
    }

    @Test
    public void sublistsCanBeMadeButAreReadonly() {
        ImmutableList<Integer> list = ImmutableList.of(0, 1, 2, 3, 4, 5, 6);
        assertThat(list.view(0, 6), contains(0, 1, 2, 3, 4, 5, 6));
        assertThat(list.view(1, 6), contains(1, 2, 3, 4, 5, 6));
        assertThat(list.view(0, 5), contains(0, 1, 2, 3, 4, 5));
        assertThat(list.view(2, 4), contains(2, 3, 4));
        assertThat(list.view(2, 3), contains(2, 3));
        assertThat(list.view(2, 2), contains(2));

        ImmutableList<Integer> subList = list.view(2, 4);
        assertThat(subList.view(1, 1), contains(3));

        Assertions.assertThrows(IllegalArgumentException.class, () -> subList.view(-1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> subList.view(1, 3));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> list.view(1, 3).add(4));
    }

    @Test
    public void viewsCanBeCopiedToBeWritable() {
        ImmutableList<Integer> list = ImmutableList.of(0, 1, 2, 3, 4, 5, 6)
            .view(2, 3).copy().add(42);
        assertThat(list, contains(2, 3, 42));
    }

    @Test
    public void removeLastReturnsView() {
        ImmutableList<Integer> list = ImmutableList.of(1,2,3).removeLast();
        assertThat(list, contains(1,2));
        assertThat(list.copy().add(12), contains(1,2,12));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> list.add(4));

        assertThat(ImmutableList.of(1).removeLast().size(), is(0));
        Assertions.assertThrows(IllegalStateException.class, () -> new ImmutableList<Integer>().removeLast());

    }

}