package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class BatterInningsCompletedEventTest {

    private final Player batter = NewZealand.oneDayLineUp().build().battingOrder().first();
    private final Player bowler = Australia.oneDayLineUp().build().battingOrder().last();

    @Test
    void canBeRebuilt() {
        BatterInningsCompletedEvent original = new BatterInningsCompletedEvent.Builder()
            .withScore(Score.SIX)
            .withBatter(batter)
            .withReason(BattingState.DISMISSED)
            .withDismissal(Dismissal.dismissal().withBatter(batter).withBowler(bowler).withType(DismissalType.BOWLED).build())
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

}