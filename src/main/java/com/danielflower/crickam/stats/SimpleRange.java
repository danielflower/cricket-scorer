package com.danielflower.crickam.stats;

import java.util.Random;

public class SimpleRange implements Range {

    private final transient Random rng = new Random();
    private final double lower;
    private final double upper;

    SimpleRange(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public double random() {
        return (rng.nextDouble() * (upper - lower)) + lower;
    }

    public double lower() {
        return lower;
    }

    public double upper() {
        return upper;
    }
}
