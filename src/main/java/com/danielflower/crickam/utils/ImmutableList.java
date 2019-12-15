package com.danielflower.crickam.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ImmutableList<T> implements Iterable<T> {

    private final List<T> arrayList;

    /**
     * Index of the first element in this list, inclusive.
     */
    private final int first;

    /**
     * Index of the last element in this list, exclusive.
     */
    private final int last;

    private ImmutableList(List<T> arrayList, int first, int last) {
        this.arrayList = arrayList;
        this.first = first;
        this.last = last;
    }

    public ImmutableList() {
        this(new ArrayList<>(), 0, -1);
    }

    public int size() {
        return last - first + 1;
    }

    public ImmutableList<T> add(T value) {
        arrayList.add(value);
        return new ImmutableList<>(arrayList, first, last + 1);
    }

    public boolean isEmpty() {
        return first > last;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new ImmutableListIterator();
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Optional<T> last() {
        return isEmpty() ? Optional.empty() : Optional.of(arrayList.get(last));
    }

    private class ImmutableListIterator implements Iterator<T> {
        private int index = first;

        @Override
        public boolean hasNext() {
            return index <= last;
        }

        @Override
        public T next() {
            return arrayList.get(index++);
        }
    }
}
