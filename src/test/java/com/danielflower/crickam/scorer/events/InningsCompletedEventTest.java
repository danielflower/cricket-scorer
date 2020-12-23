package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class InningsCompletedEventTest {

    @Test
    void canBeRebuilt() {
        InningsCompletedEvent original = new InningsCompletedEvent.Builder()
            .withScore(Score.SIX)
            .withInningsNumber(2)
            .withDeclared(true)
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

}