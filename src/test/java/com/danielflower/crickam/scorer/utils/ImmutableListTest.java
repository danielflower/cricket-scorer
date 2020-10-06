package com.danielflower.crickam.scorer.utils;

import com.danielflower.crickam.scorer.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        ImmutableList<String> three = one.add("three-on-1");
        assertThat(one.isEmpty(), is(true));
        assertThat(two, contains("Hello"));
        assertThat(three, contains("three-on-1"));
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
        assertThat(list.last(), is(nullValue()));
        assertThat(list.add("one").last(), is("one"));
    }

    @Test
    public void sublistsCanBeMade() {
        ImmutableList<Integer> list = ImmutableList.of(0, 1, 2, 3, 4, 5, 6);
        assertThat(list.subList(0, 6), contains(0, 1, 2, 3, 4, 5, 6));
        assertThat(list.subList(1, 6), contains(1, 2, 3, 4, 5, 6));
        assertThat(list.subList(0, 5), contains(0, 1, 2, 3, 4, 5));
        assertThat(list.subList(2, 4), contains(2, 3, 4));
        assertThat(list.subList(2, 3), contains(2, 3));
        assertThat(list.subList(2, 2), contains(2));

        ImmutableList<Integer> subList = list.subList(2, 4);
        assertThat(subList.subList(1, 1), contains(3));

        assertThrows(IllegalArgumentException.class, () -> subList.subList(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> subList.subList(1, 3));
    }

    @Test
    public void sublistsCanBeChanged() {
        ImmutableList<Integer> original = ImmutableList.of(0, 1, 2, 3, 4, 5, 6);
        ImmutableList<Integer> sublist = original.subList(2, 4);

        ImmutableList<Integer> original2 = original.add(7);
        ImmutableList<Integer> sublist2 = sublist.add(8);

        assertThat(original, contains(0, 1, 2, 3, 4, 5, 6));
        assertThat(original2, contains(0, 1, 2, 3, 4, 5, 6, 7));
        assertThat(sublist, contains(2, 3, 4));
        assertThat(sublist2, contains(2, 3, 4, 8));
    }


    @Test
    public void removeLastReturnsView() {
        ImmutableList<Integer> list = ImmutableList.of(1,2,3).removeLast();
        assertThat(list, contains(1,2));
        assertThat(list.add(12), contains(1,2,12));
        assertThat(list, contains(1,2));

        assertThat(ImmutableList.of(1).removeLast().size(), is(0));
        assertThrows(IllegalStateException.class, () -> new ImmutableList<Integer>().removeLast());
    }

    @Test
    public void canAddWhileIterating() {
        ImmutableList<Integer> list = ImmutableList.of(1,2,3);
        Iterator<Integer> iterator = list.iterator();
        assertThat(iterator.next(), is(1));
        ImmutableList<Integer> list2 = list.add(4);
        assertThat(iterator.next(), is(2));
        assertThat(list2.iterator().next(), is(1));
    }

    @Test
    void canFindIndexOfThings() {
        ImmutableList<Integer> l = new ImmutableList<>();
        assertThat(l.contains(1), is(false));
        assertThat(l.indexOf(1), is(-1));

        assertThat(ImmutableList.of(1,2,3,4).subList(1,2).indexOf(3), is(1));
        assertThat(ImmutableList.of(1,2,3,4).subList(1,2).contains(3), is(true));
        assertThat(ImmutableList.of(1,2,3,4).subList(1,2).indexOf(4), is(-1));
        assertThat(ImmutableList.of(1,2,3,4).subList(1,2).indexOf(1), is(-1));

    }

    @Test
    void canReplaceItems() {
        assertThat(new ImmutableList<>().replace(1, 2).isEmpty(), is(true));
        assertThat(new ImmutableList<>().replaceOrAdd(1, 2), contains(2));
        ImmutableList<Integer> _123 = ImmutableList.of(1, 2, 3);
        assertThat(_123.replace(0, 4), is(sameInstance(_123)));
        assertThat(_123.replace(3, 4), contains(1,2,4));
        assertThat(_123.replace(2, 4), contains(1,4,3));
        assertThat(_123.replace(1, 4), contains(4,2,3));
        assertThat(_123.replace(3, 3), is(sameInstance(_123)));

        ImmutableList<Integer> _121314151 = ImmutableList.of(1, 2, 1, 3, 1, 4, 1, 5, 1);
        assertThat(_121314151.replace(1, 0), contains(0,2,0,3,0,4,0,5,0));
        assertThat(_121314151.replaceOrAdd(0, 6), contains(1, 2, 1, 3, 1, 4, 1, 5, 1, 6));
    }

    @Test
    public void differentThingsCanBeAddedToEmptyLists() {
        ImmutableList<String> strings = ImmutableList.emptyList();
        strings = strings.add("Hello");
        ImmutableList<Integer> ints = ImmutableList.<Integer>emptyList().add(1);

        assertThat(strings, contains("Hello"));
        assertThat(ints, contains(1));
    }

    @Test
    public void canIterate() {
        Iterator<Integer> iter = ImmutableList.of(1, 2, 3).iterator();
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(1));
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(2));
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(3));
        assertThat(iter.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    public void canIterateInReverse() {
        Iterator<Integer> iter = ImmutableList.of(3, 2, 1).reverseIterator();
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(1));
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(2));
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.next(), is(3));
        assertThat(iter.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, iter::next);
    }

}