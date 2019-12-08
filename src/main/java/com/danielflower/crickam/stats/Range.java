package com.danielflower.crickam.stats;

public interface Range {

    double random();

    static SimpleRange between(double lower, double upper) {
        return new SimpleRange(lower, upper);
    }

    static Range circular(double lower, double upper) {
        return new WrappedRange(lower, upper, 360.0);
    }

    static Range wrapped(double lower, double upper, double max) {
        return new WrappedRange(lower, upper, max);
    }
}
