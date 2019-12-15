package com.danielflower.crickam.utils;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

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
    }

    @Test
    public void lastReturnsOptional() {
        ImmutableList<String> list = new ImmutableList<>();
        assertThat(list.last(), is(Optional.empty()));
        assertThat(list.add("one").last(), is(Optional.of("one")));
    }

}