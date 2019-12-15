package com.danielflower.crickam.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class ImmutableListCollectorTest {

    @Test
    public void canCollectFromAStream() {
        ImmutableList<Integer> collected = ImmutableList.of(1, 2, 3, 4, 5).stream()
            .skip(1)
            .filter(n -> n % 2 == 0)
            .collect(ImmutableListCollector.toImmutableList());
        assertThat(collected, contains(2, 4));
    }

}