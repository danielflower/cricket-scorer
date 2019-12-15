package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.LineUp;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class InningsStartingEventTest {

    @Test
    public void canCreate() {
        LineUp nz = NewZealand.oneDayLineUp().build();
        InningsStartingEvent ise = anInningsStartingEvent().withBowlingTeam(nz).build();
        assertThat(ise.bowlingTeam(), is(nz));
    }

    public static InningsStartingEvent.Builder anInningsStartingEvent() {
        return new InningsStartingEvent.Builder()
            .withBattingTeam(Australia.oneDayLineUp().build())
            .withBowlingTeam(NewZealand.oneDayLineUp().build())
            .withStartTime(Instant.now());
    }

}