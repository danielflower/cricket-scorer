package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

class MatchTest {

    @Test
    public void matchesAreImmutable() {
        Match match = aMatch().build();
    }

    private MatchBuilder aMatch() {
        return new MatchBuilder()
            .withMatchID(UUID.randomUUID().toString())
            .withNumberOfInningsPerTeam(1)
            .withOversPerInnings(50)
            .withStartTime(Instant.now())
//            .withTeams()
            .withVenue(VenueTest.aVenue().build())
            ;
    }

}