package com.danielflower.crickam.utils;

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

}