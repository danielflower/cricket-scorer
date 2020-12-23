package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.TimeZone;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MatchStartingEventTest {

    @Test
    void canBeRebuilt() {
        MatchStartingEvent original = new MatchStartingEvent.Builder()
            .withMatchID(UUID.randomUUID())
            .withBallsPerInnings(300)
            .withInningsPerTeam(2)
            .withNumberOfScheduledDays(4)
            .withOversPerInnings(50)
            .withScheduledStartTime(Instant.now().plusSeconds(10000))
            .withTimeZone(TimeZone.getDefault())
            .withCustomData("Some-custom-data")
            .withTeamLineUps(ImmutableList.of(NewZealand.oneDayLineUp().build(), Australia.oneDayLineUp().build()))
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

}