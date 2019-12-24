package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.utils.ImmutableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
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

        Match match = control.onEvent(inningsStarting()
            .withBattingTeam(nz)
            .withBowlingTeam(aus)
            .withOpeners(nz.players.get(0), nz.players.get(1))
            .withStartTime(Instant.now())
        );

        assertThat(control.current(), sameInstance(match));
        assertThat(match.oversPerInnings(), is(50));
        assertThat(match.currentInnings().isPresent(), is(true));

        Innings innings = match.currentInnings().get();
        assertThat(innings.inningsNumber(), is(1));
        assertThat(innings.yetToBat(), equalTo(nz.players.view(2, 10)));

        match = control.onEvent(overStarting().withBowler(aus.players.last().get()));

        Over over = match.currentInnings().get().currentOver().get();
        assertThat(over.balls().size(), is(0));
        assertThat(over.spell().spellNumber(), is(1));
        assertThat(over.spell().balls().size(), is(0));
        assertThat(over.spell().overs(), contains(over));
        assertThat(over.spell().bowlerInnings().bowler(), sameInstance(aus.players.last().get()));
        assertThat(over.spell().bowlerInnings().spells(), contains(sameInstance(over.spell())));
        assertThat(over.numberInInnings(), is(1));
        assertThat(over.legalBalls(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.isComplete(), is(false));
        assertThat(over.isMaiden(), is(true));
        assertThat(over.striker(), is(sameInstance(nz.players.get(0))));
        assertThat(over.nonStriker(), is(sameInstance(nz.players.get(1))));
        assertThat(over.runs(), is(0));
    }

}