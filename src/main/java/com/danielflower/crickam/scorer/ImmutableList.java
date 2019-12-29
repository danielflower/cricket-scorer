package com.danielflower.crickam.scorer;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ImmutableList<T> implements Iterable<T> {

    private final List<T> arrayList;

    /**
     * Index of the first element in this list.
     */
    private final int first;

    /**
     * Index of the last element in this list, or a value less than first if its empty.
     */
    private final int last;

    protected ImmutableList(List<T> arrayList, int first, int last) {
        this.arrayList = arrayList;
        this.first = first;
        this.last = last;
    }

    public ImmutableList() {
        this(new ArrayList<>(), 0, -1);
    }

    public ImmutableList(Collection<T> values) {
        this(new ArrayList<>(values), 0, values.size() - 1);
    }

    @SafeVarargs
    public static <V> ImmutableList<V> of(V... values) {
        ArrayList<V> objects = new ArrayList<>(Arrays.asList(values));
        return new ImmutableList<>(objects, 0, objects.size() - 1);
    }


    public int size() {
        return last - first + 1;
    }

    public ImmutableList<T> add(T value) {
        List<T> toUse = this.arrayList;
        int newFirst = this.first;
        int newLast = this.last + 1;
        if (last < toUse.size() - 1) {
            toUse = new ArrayList<>(arrayList.subList(first, last + 1));
            newFirst = 0;
            newLast = last - first + 1;
        }
        toUse.add(value);
        return new ImmutableList<>(toUse, newFirst, newLast);
    }

    public boolean isEmpty() {
        return first > last;
    }

    public ImmutableList<T> subList(int firstIndex, int lastIndex) {
        if (firstIndex < 0) throw new IllegalArgumentException("firstIndex must be non-negative but was " + firstIndex);
        int newFirst = this.first + firstIndex;
        int newLast = this.first + lastIndex;
        if (newLast > last) throw new IllegalArgumentException("The lastIndex value " + lastIndex + " is too large. Max value allowed is " + (size() - 1));
        return new ImmutableList<>(arrayList, newFirst, newLast);
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

    public T get(int index) {
        return arrayList.get(index);
    }

    public boolean contains(T item) {
        for (T val : this) {
            if (Objects.equals(val, item)) {
                return true;
            }
        }
        return false;
    }

    public ImmutableList<T> removeLast() {
        if (size() == 0) {
            throw new IllegalStateException("The list was empty");
        }
        return subList(0, last -1);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableList<?> that = (ImmutableList<?>) o;
        return asList().equals(that.asList());
    }

    @NotNull
    private List<T> asList() {
        return Collections.unmodifiableList(this.arrayList.subList(first, last + 1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(asList());
    }

    @Override
    public String toString() {
        return asList().toString();
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@link ImmutableList}.
     * @param <T> the type of the input elements
     * @return a {@code Collector} which collects all the input elements into an {@link ImmutableList}, in encounter order
     */
    public static <T> Collector<T, List<T>, ImmutableList<T>> toImmutableList() {
        return new ImmutableListCollector<>();
    }


    private static final class ImmutableListCollector<T> implements Collector<T, List<T>, ImmutableList<T>> {

        @Override
        public Supplier<List<T>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<T>> combiner() {
            return (players, players2) -> {
                players.addAll(players2);
                return players;
            };
        }

        @Override
        public Function<List<T>, ImmutableList<T>> finisher() {
            return ImmutableList::new;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
