package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchResult;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MatchCompletedEventTest {

    @Test
    void canBeRebuilt() {
        MatchCompletedEvent original = new MatchCompletedEvent.Builder()
            .withResult(MatchResult.NoResult)
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

}