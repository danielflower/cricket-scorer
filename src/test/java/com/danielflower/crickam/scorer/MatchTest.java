package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MatchTest {

    @Test
    public void canBuildThem() {
        MatchStartingEvent match = aMatch().build();
        assertThat(match.oversPerInnings(), is(50));
    }

    public static MatchStartingEvent.Builder aMatch() {
        return MatchEvents.matchStarting()
            .withInningsPerTeam(1)
            .withOversPerInnings(50)
            .withScheduledStartTime(Instant.now())
            .withTeamLineUps(ImmutableList.of(NewZealand.oneDayLineUp().build(), Australia.oneDayLineUp().build()))
            ;
    }

}