package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class BatterInningsStartingEventTest {

    private final Player batter = NewZealand.oneDayLineUp().build().battingOrder().first();

    @Test
    void canBeRebuilt() {
        BatterInningsStartingEvent original = new BatterInningsStartingEvent.Builder()
            .withBatter(batter)
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

}