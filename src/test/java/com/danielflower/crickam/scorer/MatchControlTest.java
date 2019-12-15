package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

class MatchControlTest {

    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final LineUp aus = Australia.oneDayLineUp().withPlayingAtHome(false).build();

    @Test
    public void eventsAreAdded() {

        MatchControl control = new MatchControl(
            MatchTest.aMatch()
                .withNumberOfInningsPerTeam(1)
                .withOversPerInnings(50)
                .withTeams(List.of(nz, aus))
                .build()
        );

        Match match = control.onEvent(new InningsStartingEvent.Builder()
            .withBattingTeam(nz)
            .withBowlingTeam(aus)
            .withOpeners(nz.players.get(0), nz.players.get(1))
            .withStartTime(Instant.now()).build());

        assertThat(control.current(), sameInstance(match));
        assertThat(control.current().oversPerInnings, is(50));
        assertThat(control.current().getCurrentInnings().isPresent(), is(true));

    }

}