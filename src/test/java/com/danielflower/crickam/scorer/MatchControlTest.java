package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MatchControlTest {

    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final LineUp aus = Australia.oneDayLineUp().build();

    @Test
    public void eventsAreAdded() {

        MatchControl control = MatchControl.newMatch(
            MatchTest.aMatch()
                .withNumberOfInningsPerTeam(1)
                .withOversPerInnings(50)
                .withTeams(ImmutableList.of(nz, aus))
                .build()
        );

        Match match = control.onEvent(inningsStarting()
            .withBattingTeam(nz)
            .withBowlingTeam(aus)
            .withOpeners(nz.battingOrder().get(0), nz.battingOrder().get(1))
            .withTime(Instant.now())
        );

        assertThat(control.current(), sameInstance(match));
        assertThat(match.oversPerInnings(), is(Optional.of(50)));
        assertThat(match.ballsPerInnings(), is(Optional.of(300)));
        assertThat(match.currentInnings().isPresent(), is(true));

        Innings innings = match.currentInnings().get();
        assertThat(innings.inningsNumber(), is(1));
        assertThat(innings.yetToBat(), equalTo(nz.battingOrder().view(2, 10)));

        match = control.onEvent(overStarting().withBowler(aus.battingOrder().last().get()));

        Over over = match.currentInnings().get().currentOver().get();
        assertThat(over.balls().size(), is(0));
        assertThat(over.bowler(), sameInstance(aus.battingOrder().last().get()));
        assertThat(over.numberInInnings(), is(0));
        assertThat(over.validDeliveries(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.isComplete(), is(false));
        assertThat(over.isMaiden(), is(false));
        assertThat(over.striker().player(), is(sameInstance(nz.battingOrder().get(0))));
        assertThat(over.nonStriker().player(), is(sameInstance(nz.battingOrder().get(1))));
        assertThat(over.runs(), is(0));
    }

}