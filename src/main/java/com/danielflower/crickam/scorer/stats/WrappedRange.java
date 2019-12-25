package com.danielflower.crickam.scorer.stats;

import java.util.Random;

public final class WrappedRange implements Range {
    private final transient Random rng = new Random();
    private final double lower;
    private final double upper;
    private final double max;

    WrappedRange(double lower, double upper, double max) {
        this.lower = lower;
        this.upper = upper;
        this.max = max;
    }

    @Override
    public double random() {
        double randomRange = lower + (max - upper);
        double random = rng.nextDouble() * randomRange;
        if (random <= lower) {
            return random;
        }
        double overflow = random - lower;
        return upper + overflow;
    }
}
