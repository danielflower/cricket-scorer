package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.OptionalInt;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MatchTest {

    @Test
    public void canBuildThem() {
        MatchStartingEvent match = aMatch().build(null);
        assertThat(match.oversPerInnings(), is(OptionalInt.of(50)));
    }

    public static MatchStartingEvent.Builder aMatch() {
        return MatchEvents.matchStarting()
            .withMatchID(UUID.randomUUID().toString())
            .withInningsPerTeam(1)
            .withMatchType(MatchType.ODI)
            .withOversPerInnings(50)
            .withScheduledStartTime(Instant.now())
            .withTeamLineUps(ImmutableList.of(NewZealand.oneDayLineUp().build(), Australia.oneDayLineUp().build()))
            .withVenue(VenueTest.aVenue().build())
            ;
    }

}