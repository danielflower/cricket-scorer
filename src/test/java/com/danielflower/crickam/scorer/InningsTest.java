package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.ScoreBuilder.SINGLE;
import static com.danielflower.crickam.scorer.events.BallCompletedEvent.ballCompleted;
import static com.danielflower.crickam.scorer.events.InningsCompletedEvent.inningsCompleted;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
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
        assertThat(firstInnings.state(), is(Innings.State.NOT_STARTED));
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

        assertThat(innings.state(), is(Innings.State.IN_PROGRESS));

        assertThat(innings.currentStriker().getPlayer(), sameInstance(nz.getPlayers().get(0)));
        assertThat(innings.currentNonStriker().getPlayer(), sameInstance(nz.getPlayers().get(1)));
        assertThat(innings.yetToBat(), contains(nz.getPlayers().get(2), nz.getPlayers().get(3), nz.getPlayers().get(4), nz.getPlayers().get(5), nz.getPlayers().get(6), nz.getPlayers().get(7), nz.getPlayers().get(8), nz.getPlayers().get(9), nz.getPlayers().get(10)));
        assertThat(innings.numberOfScheduledOvers(), is(50));
        assertThat(innings.overs().size(), is(1));
        Over over = innings.overs().get(0);
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.runs(), is(0));
        assertThat(over.isMaiden(), is(false));
        assertThat(over.isComplete(), is(false));
        assertThat(over.legalBalls(), is(0));
        assertThat(over.numberInInnings(), is(0));
        assertThat(over.striker().getPlayer(), is(opener1));
        assertThat(over.nonStriker().getPlayer(), is(opener2));
        assertThat(over.balls().size(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.bowler(), is(aus.players.get(10)));

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
        assertThat(innings.bowlerInningsList().isEmpty(), is(false));
    }

    @Test
    public void ballsAreAdded() {

        Innings innings = firstInnings
            .onEvent(overStarting()
                .withBowler(aus.players.get(10))
                .withStriker(opener1)
                .withNonStriker(opener2)
                .withBallsInOver(6)
                .build());

        assertThat(innings.currentOver().get().numberInInnings(), is(0));

        innings = innings.onEvent(ballCompleted()
            .withBowler(aus.players.get(10))
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withRunsScored(SINGLE)
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

        innings = innings.onEvent(ballCompleted()
            .withRunsScored(ScoreBuilder.TWO)
            .build());
        assertThat(innings.currentStriker().getPlayer(), is(opener2));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener1));
        assertThat(innings.currentPartnership().totalRuns(), is(3));

        innings = innings.onEvent(ballCompleted()
            .withRunsScored(ScoreBuilder.THREE)
            .withPlayersCrossed(true)
            .build());
        assertThat(innings.currentStriker().getPlayer(), is(opener1));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener2));
        assertThat(innings.currentPartnership().totalRuns(), is(6));

        innings = innings
            .onEvent(dotBall())
            .onEvent(dotBall())
            .onEvent(ballCompleted()
                .withRunsScored(ScoreBuilder.WIDE)
                .build());

        assertThat(innings.currentOver().get().isComplete(), is(false));
        assertThat(innings.currentOver().get().remainingBalls(), is(1));

        innings = innings
            .onEvent(ballCompleted()
                .withDismissal(DismissalType.Bowled)
                .build());
        Over over = innings.currentOver().get();
        assertThat(over.isComplete(), is(true));
        assertThat(over.remainingBalls(), is(0));
        assertThat(over.balls().score().totalRuns(), is(7));
        assertThat(over.balls().score().wickets(), is(1));
        assertThat(innings.state(), is(Innings.State.IN_PROGRESS));

        assertThat(innings.currentPartnership().totalRuns(), is(7));
        assertThat(innings.currentPartnership().endTime(), is(not(nullValue())));
        assertThat(innings.currentPartnership().firstBatterContribution().size(), is(5));
        assertThat(innings.currentPartnership().firstBatterContribution().score().balls(), is(4));
        assertThat(innings.currentPartnership().firstBatterContribution().score().totalRuns(), is(2));
        assertThat(innings.currentPartnership().firstBatterContribution().score().scoredFromBat(), is(1));
        assertThat(innings.currentPartnership().secondBatterContribution().size(), is(2));
        assertThat(innings.currentPartnership().secondBatterContribution().score().totalRuns(), is(5));


        innings = innings.onEvent(overCompleted().build());
        assertThat(innings.state(), is(Innings.State.BETWEEN_OVERS));
        assertThat(innings.currentOver(), is(Optional.empty()));

        innings = innings
            .onEvent(overStarting().withStriker(opener1).withNonStriker(opener2).withBowler(aus.players.get(9)).build())
            .onEvent(ballCompleted().withRunsScored(SINGLE).build())
            .onEvent(inningsCompleted().build());

        assertThat(innings.currentOver(), is(Optional.empty()));
        assertThat(innings.state(), is(Innings.State.COMPLETED));
    }

    @Test
    public void ifBattersNotSpecifiedOnOverStartingThenItIsAssumed() {
        Innings innings = firstInnings.onEvent(oneBallOverStarting(aus.players.get(10)));
        assertThat(innings.currentStriker().getPlayer(), is(opener1));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener2));
        innings = innings.onEvent(dotBall());
        assertThat(innings.currentStriker().getPlayer(), is(opener1));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener2));
        innings = innings.onEvent(overCompleted().build());
        assertThat(innings.currentStriker().getPlayer(), is(opener1));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener2));
        innings = innings.onEvent(oneBallOverStarting(aus.players.get(9)));
        assertThat(innings.currentStriker().getPlayer(), is(opener2));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener1));
        innings = innings.onEvent(single());
        assertThat(innings.currentStriker().getPlayer(), is(opener1));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener2));
        innings = innings.onEvent(overCompleted().build()).onEvent(oneBallOverStarting(aus.players.get(10)));
        assertThat(innings.currentStriker().getPlayer(), is(opener2));
        assertThat(innings.currentNonStriker().getPlayer(), is(opener1));
    }

    @Test
    public void oversBowledOneOverApartByTheSameBowlerAreConsideredInTheSameSpell() {
        Player bowler1 = aus.players.get(10);
        Player bowler2 = aus.players.get(9);
        Player bowler3 = aus.players.get(8);

        Innings innings = firstInnings.onEvent(oneBallOverStarting(bowler1));
        assertThat(innings.bowlerInningsList().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(0));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));

        innings = innings.onEvent(dotBall());
        assertThat(innings.bowlerInningsList().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));

        innings = innings.onEvent(overCompleted().build());
        assertThat(innings.bowlerInningsList().size(), is(1));

        innings = innings.onEvent(oneBallOverStarting(bowler2));
        assertThat(innings.bowlerInningsList().size(), is(2));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings.bowlerInningsList().get(1).balls().size(), is(0));
        assertThat(innings.bowlerInningsList().get(1).bowler(), is(bowler2));

        innings = innings.onEvent(single());
        assertThat(innings.bowlerInningsList().size(), is(2));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings.bowlerInningsList().get(1).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(1).bowler(), is(bowler2));

        innings = innings.onEvent(overCompleted().build())
            .onEvent(oneBallOverStarting(bowler1))
            .onEvent(single());
        assertThat(innings.bowlerInningsList().size(), is(2));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(2));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings.bowlerInningsList().get(0).spells().size(), is(1));
        assertThat(innings.bowlerInningsList().get(0).spells().get(0).spellNumber(), is(1));
        assertThat(innings.bowlerInningsList().get(1).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(1).bowler(), is(bowler2));
        assertThat(innings.bowlerInningsList().get(1).spells().size(), is(1));
        assertThat(innings.bowlerInningsList().get(1).spells().get(0).spellNumber(), is(1));

        innings = innings.onEvent(overCompleted().build())
            .onEvent(oneBallOverStarting(bowler3))
            .onEvent(single())
            .onEvent(overCompleted().build())
            .onEvent(oneBallOverStarting(bowler1))
            .onEvent(single())
            .onEvent(overCompleted().build())
            .onEvent(oneBallOverStarting(bowler2))
            .onEvent(single())
            .onEvent(overCompleted().build());

        assertThat(innings.bowlerInningsList().size(), is(3));
        assertThat(innings.bowlerInningsList().get(0).balls().size(), is(3));
        assertThat(innings.bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings.bowlerInningsList().get(0).spells().size(), is(1));
        assertThat(innings.bowlerInningsList().get(1).balls().size(), is(2));
        assertThat(innings.bowlerInningsList().get(1).bowler(), is(bowler2));
        assertThat(innings.bowlerInningsList().get(1).spells().size(), is(2));
        assertThat(innings.bowlerInningsList().get(1).spells().get(0).spellNumber(), is(1));
        assertThat(innings.bowlerInningsList().get(1).spells().get(1).spellNumber(), is(2));
        assertThat(innings.bowlerInningsList().get(2).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(2).bowler(), is(bowler3));
        assertThat(innings.bowlerInningsList().get(2).spells().size(), is(1));
        assertThat(innings.bowlerInningsList().get(2).spells().get(0).spellNumber(), is(1));

    }

    private BallCompletedEvent single() {
        return ballCompleted().withRunsScored(SINGLE).withPlayersCrossed(true).build();
    }

    private BallCompletedEvent dotBall() {
        return ballCompleted().withRunsScored(ScoreBuilder.DOT_BALL).build();
    }

    private OverStartingEvent oneBallOverStarting(Player bowler1) {
        return overStarting().withBowler(bowler1).withBallsInOver(1).build();
    }

}