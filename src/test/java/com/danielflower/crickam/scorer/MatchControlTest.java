package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import com.danielflower.crickam.utils.ImmutableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MatchControlTest {

    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final LineUp aus = Australia.oneDayLineUp().withPlayingAtHome(false).build();

    @Test
    public void eventsAreAdded() {

        MatchControl control = new MatchControl(
            MatchTest.aMatch()
                .withNumberOfInningsPerTeam(1)
                .withOversPerInnings(50)
                .withTeams(ImmutableList.of(nz, aus))
                .build()
        );

        Match match = control.onEvent(new InningsStartingEvent.Builder()
            .withBattingTeam(nz)
            .withBowlingTeam(aus)
            .withOpeners(nz.players.get(0), nz.players.get(1))
            .withStartTime(Instant.now()).build());

        assertThat(control.current(), sameInstance(match));
        assertThat(match.oversPerInnings, is(50));
        assertThat(match.getCurrentInnings().isPresent(), is(true));

        Innings innings = match.getCurrentInnings().get();
        assertThat(innings.inningsNumber, is(1));
        assertThat(innings.getYetToBat(), equalTo(nz.players.subList(2, 10)));

    }

}