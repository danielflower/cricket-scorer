package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
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
        MatchStartingEvent match = aMatch().build();
        assertThat(match.oversPerInnings(), is(OptionalInt.of(50)));
    }

    public static MatchStartingEvent.Builder aMatch() {
        return MatchStartingEvent.matchStarting()
            .withMatchID(UUID.randomUUID().toString())
            .withNumberOfInningsPerTeam(1)
            .withMatchType(MatchType.ODI)
            .withOversPerInnings(50)
            .withScheduledStartTime(Instant.now())
            .withTeams(ImmutableList.of(NewZealand.oneDayLineUp().build(), Australia.oneDayLineUp().build()))
            .withVenue(VenueTest.aVenue().build())
            ;
    }

}