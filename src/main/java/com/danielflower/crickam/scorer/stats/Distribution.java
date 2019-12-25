package com.danielflower.crickam.scorer.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Represents a set of objects with weightings.
 * @param <T> The type of object that has a weighting.
 */
public final class Distribution<T> {
	private int total = 0;
	private final List<Pair<T>> values = new ArrayList<>();
	private final transient Random rng;

	public Distribution() {
		rng = new Random();
	}

	public Distribution(long seed) {
		rng = new Random(seed);
	}
    public Distribution(List<T> valuesWeightedAtOneHundred) {
        for (T value : valuesWeightedAtOneHundred) {
            add(value, 100);
        }
	    rng = new Random();
    }

    public double expectedValue() {
    	int count = 0;
    	double sum = 0.0;
	    for (Pair<T> value : values) {
	    	Number n;
	    	if (value.Value instanceof Boolean) {
			    n = (Boolean) value.Value ? 1.0 : 0.0;
		    } else {
			    n = (Number) value.Value;
		    }
		    sum += value.Weighting * n.doubleValue();
		    count += value.Weighting;
	    }
	    return sum / count;
    }

    private int totalWeighting() {
		return values.stream().mapToInt(value -> value.Weighting).sum();
    }


    public Distribution<T> add(T value, int weighting) {
		total += weighting;
		this.values.add(new Pair<>(value, weighting));
		return this;
	}

	public T random() {
		if (values.size() == 0) {
			throw new IllegalStateException("The weighted picker must have at least one value in it");
		}

		int val = rng.nextInt(total);
		int count = 0;
		for (Pair<T> item : values) {
			count += item.Weighting;
			if (val < count) {
				return item.Value;
			}
		}
		throw new RuntimeException("This shouldn't happen.");
	}

	public static <T> Distribution<T> combine(Distribution<T> one, Distribution<T> two) {
		double twoMultiplier = one.totalWeighting() / (double)two.totalWeighting();
		Distribution<T> newOne = new Distribution<>();
		for (Pair<T> value : one.values) {
			newOne.add(value.Value, value.Weighting);
		}
		for (Pair<T> value : two.values) {
			newOne.add(value.Value, (int) Math.round(value.Weighting * twoMultiplier));
		}
		return newOne;
	}

	public static class Pair<TValue> {
		public final TValue Value;
		public final int Weighting;

		public Pair(TValue value, int weighting) {
			Value = value;
			Weighting = weighting;
		}

		@Override
		public String toString() {
			return "[" + Value + " (" + Weighting + ")]";
		}
	}

	public static <T> Pair<T> pair(T value, int weighting) {
    	return new Pair<>(value, weighting);
	}

	public static <T> Distribution<T> of(Pair<T>... weightings) {
    	Distribution<T> d = new Distribution<>();
		for (Pair<T> weighting : weightings) {
			d.add(weighting.Value, weighting.Weighting);
		}
		return d;
	}

	public static <T> Distribution<T> even(T... values) {
		return even(asList(values));
	}

	public static <T> Distribution<T> even(Collection<T> values) {
		Distribution<T> dist = new Distribution<>();
		for (T value : values) {
			dist.add(value, 100);
		}
		return dist;
	}

	@Override
	public String toString() {
		return "Distribution " + values;
	}
}


