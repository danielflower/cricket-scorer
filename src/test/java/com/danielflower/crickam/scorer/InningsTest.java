package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.BallCompleteEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class InningsTest {

    private final LineUp aus = Australia.oneDayLineUp().build();
    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final Player opener2 = nz.players.get(1);
    private final Player opener1 = nz.players.get(0);
    private final Innings firstInnings = Innings.newInnings(MatchTest.aMatch().build(), nz, aus, nz.getPlayers().view(0, 1), 1, Instant.now(), 50);

    @Test
    void beforeAnyEventsThingsAreEmpty() {
        assertThat(firstInnings.currentStriker().getPlayer(), sameInstance(nz.getPlayers().get(0)));
        assertThat(firstInnings.currentNonStriker().getPlayer(), sameInstance(nz.getPlayers().get(1)));
        assertThat(firstInnings.yetToBat(), contains(nz.getPlayers().get(2), nz.getPlayers().get(3), nz.getPlayers().get(4), nz.getPlayers().get(5), nz.getPlayers().get(6), nz.getPlayers().get(7), nz.getPlayers().get(8), nz.getPlayers().get(9), nz.getPlayers().get(10)));
        assertThat(firstInnings.numberOfScheduledOvers(), is(50));
        assertThat(firstInnings.overs().isEmpty(), is(true));
        assertThat(firstInnings.currentOver(), is(Optional.empty()));
        assertThat(firstInnings.allOut(), is(false));
        assertThat(firstInnings.numberOfBallsRemaining(), is(300));
        assertThat(firstInnings.balls().size(), is(0));
        assertThat(firstInnings.inningsNumber(), is(1));
        assertThat(firstInnings.batterInningsList(), contains(firstInnings.currentStriker(), firstInnings.currentNonStriker()));
        assertThat(firstInnings.battingTeam(), sameInstance(nz));
        assertThat(firstInnings.bowlingTeam(), sameInstance(aus));
        assertThat(firstInnings.currentPartnership().balls().size(), is(0));
        assertThat(firstInnings.currentPartnership().firstBatter(), sameInstance(firstInnings.currentStriker()));
        assertThat(firstInnings.currentPartnership().secondBatter(), sameInstance(firstInnings.currentNonStriker()));
        assertThat(firstInnings.partnerships(), contains(firstInnings.currentPartnership()));
        assertThat(firstInnings.wicketsRemaining(), is(10));
        assertThat(firstInnings.bowlerInningsList().isEmpty(), is(true));
    }

    @Test
    void aNewOverEventAddsToTheOverList() {
        Innings innings = firstInnings.onEvent(new OverStartingEvent.Builder()
            .withBowler(aus.players.get(10))
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withBallsInOver(6)
            .build());

        assertThat(innings.currentStriker().getPlayer(), sameInstance(nz.getPlayers().get(0)));
        assertThat(innings.currentNonStriker().getPlayer(), sameInstance(nz.getPlayers().get(1)));
        assertThat(innings.yetToBat(), contains(nz.getPlayers().get(2), nz.getPlayers().get(3), nz.getPlayers().get(4), nz.getPlayers().get(5), nz.getPlayers().get(6), nz.getPlayers().get(7), nz.getPlayers().get(8), nz.getPlayers().get(9), nz.getPlayers().get(10)));
        assertThat(innings.numberOfScheduledOvers(), is(50));
        assertThat(innings.overs().size(), is(1));
        Over over = innings.overs().get(0);
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.runs(), is(0));
        assertThat(over.isMaiden(), is(true));
        assertThat(over.isComplete(), is(false));
        assertThat(over.legalBalls(), is(0));
        assertThat(over.numberInInnings(), is(0));
        assertThat(over.striker().getPlayer(), is(opener1));
        assertThat(over.nonStriker().getPlayer(), is(opener2));
        assertThat(over.balls().size(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.spell().spellNumber(), is(1));
        assertThat(over.spell().balls().size(), is(0));
        assertThat(over.spell().bowlerInnings().balls().size(), is(0));
        assertThat(over.spell().bowlerInnings().bowler(), is(aus.players.get(10)));

        assertThat(innings.currentOver(), is(Optional.of(over)));
        assertThat(innings.allOut(), is(false));
        assertThat(innings.numberOfBallsRemaining(), is(300));
        assertThat(innings.balls().size(), is(0));
        assertThat(innings.inningsNumber(), is(1));
        assertThat(innings.batterInningsList(), contains(firstInnings.currentStriker(), firstInnings.currentNonStriker()));
        assertThat(innings.battingTeam(), sameInstance(nz));
        assertThat(innings.bowlingTeam(), sameInstance(aus));
        assertThat(innings.currentPartnership().balls().size(), is(0));
        assertThat(innings.currentPartnership().firstBatter(), sameInstance(firstInnings.currentStriker()));
        assertThat(innings.currentPartnership().secondBatter(), sameInstance(firstInnings.currentNonStriker()));
        assertThat(innings.partnerships(), contains(firstInnings.currentPartnership()));
        assertThat(innings.wicketsRemaining(), is(10));
        assertThat(innings.bowlerInningsList().isEmpty(), is(true));
    }

    @Test
    public void ballsAreAdded() {

        Innings innings = firstInnings.onEvent(new OverStartingEvent.Builder()
            .withBowler(aus.players.get(10))
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withBallsInOver(6)
            .build())
            .onEvent(new BallCompleteEvent.Builder()
                .withBowler(aus.players.get(10))
                .withStriker(opener1)
                .withNonStriker(opener2)
                .withRunsScored(ScoreBuilder.score().setSingles(1).setBalls(1).setScored(1).build())
                .withPlayersCrossed(true)
                .build());

        assertThat(innings.balls().size(), is(1));
        assertThat(innings.balls().score().totalRuns(), is(1));
        assertThat(innings.currentStriker().getPlayer(), is(opener2));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener1));
        assertThat(innings.currentPartnership().totalRuns(), is(1));
        assertThat(innings.currentPartnership().endTime(), is(nullValue()));
        assertThat(innings.currentPartnership().firstBatterContribution().size(), is(1));
        assertThat(innings.currentPartnership().firstBatterContribution().score().totalRuns(), is(1));
        assertThat(innings.currentPartnership().secondBatterContribution().size(), is(0));
        assertThat(innings.currentPartnership().secondBatterContribution().score().totalRuns(), is(0));

    }

}