package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MatchTest {

    @Test
    public void canBuildThem() {
        Match match = aMatch().build();
        assertThat(match.oversPerInnings(), is(Optional.of(50)));
    }

    public static MatchBuilder aMatch() {
        return new MatchBuilder()
            .withMatchID(UUID.randomUUID().toString())
            .withNumberOfInningsPerTeam(1)
            .withOversPerInnings(50)
            .withStartTime(Instant.now())
            .withTeams(ImmutableList.of(NewZealand.oneDayLineUp().build(), Australia.oneDayLineUp().build()))
            .withVenue(VenueTest.aVenue().build())
            ;
    }

}