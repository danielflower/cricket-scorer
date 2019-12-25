package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import com.danielflower.crickam.scorer.utils.ImmutableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.Score.*;
import static com.danielflower.crickam.scorer.events.BallCompletedEvent.ballCompleted;
import static com.danielflower.crickam.scorer.events.BatterInningsStartingEvent.batterInningsStarting;
import static com.danielflower.crickam.scorer.events.InningsCompletedEvent.inningsCompleted;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InningsTest {

    private final LineUp aus = Australia.oneDayLineUp().build();
    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final Player opener1 = nz.players.get(0);
    private final Player opener2 = nz.players.get(1);
    private final Player number3 = nz.players.get(2);
    private final Innings firstInnings = Innings.newInnings(MatchTest.aMatch().build(), nz, aus, nz.getPlayers().view(0, 1), 1, Instant.now(), 300);
    private final Player bowler1 = aus.players.get(10);
    private final Player bowler2 = aus.players.get(9);
    private final Player bowler3 = aus.players.get(8);

    @Test
    void beforeAnyEventsThingsAreEmpty() {
        assertThat(firstInnings.state(), is(Innings.State.NOT_STARTED));
        assertThat(firstInnings.currentStriker().get().player(), is(opener1));
        assertThat(firstInnings.currentNonStriker().get().player(), is(opener2));
        assertThat(firstInnings.yetToBat(), contains(nz.getPlayers().get(2), nz.getPlayers().get(3), nz.getPlayers().get(4), nz.getPlayers().get(5), nz.getPlayers().get(6), nz.getPlayers().get(7), nz.getPlayers().get(8), nz.getPlayers().get(9), nz.getPlayers().get(10)));
        assertThat(firstInnings.originalNumberOfScheduledOvers(), is(Optional.of(50)));
        assertThat(firstInnings.overs().isEmpty(), is(true));
        assertThat(firstInnings.currentOver(), is(Optional.empty()));
        assertThat(firstInnings.allOut(), is(false));
        assertThat(firstInnings.numberOfBallsRemaining(), is(Optional.of(300)));
        assertThat(firstInnings.balls().size(), is(0));
        assertThat(firstInnings.inningsNumber(), is(1));
        assertThat(firstInnings.batterInningsList().size(), is(2));
        assertThat(firstInnings.battingTeam(), sameInstance(nz));
        assertThat(firstInnings.bowlingTeam(), sameInstance(aus));
        assertThat(firstInnings.currentPartnership().get().balls().size(), is(0));
        assertThat(firstInnings.partnerships(), contains(firstInnings.currentPartnership().get()));
        assertThat(firstInnings.wicketsRemaining(), is(10));
        assertThat(firstInnings.bowlerInningsList().isEmpty(), is(true));
    }

    @Test
    void aNewOverEventAddsToTheOverList() {
        Innings innings = firstInnings.onEvent(new OverStartingEvent.Builder()
            .withBowler(bowler1)
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withBallsInOver(6)
            .build());

        assertThat(innings.state(), is(Innings.State.IN_PROGRESS));

        assertThat(innings.currentStriker().get().player(), sameInstance(nz.getPlayers().get(0)));
        assertThat(innings.currentNonStriker().get().player(), sameInstance(nz.getPlayers().get(1)));
        assertThat(innings.yetToBat(), contains(nz.getPlayers().get(2), nz.getPlayers().get(3), nz.getPlayers().get(4), nz.getPlayers().get(5), nz.getPlayers().get(6), nz.getPlayers().get(7), nz.getPlayers().get(8), nz.getPlayers().get(9), nz.getPlayers().get(10)));
        assertThat(innings.originalNumberOfScheduledOvers(), is(Optional.of(50)));
        assertThat(innings.overs().size(), is(1));
        Over over = innings.overs().get(0);
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.runs(), is(0));
        assertThat(over.isMaiden(), is(false));
        assertThat(over.isComplete(), is(false));
        assertThat(over.validDeliveries(), is(0));
        assertThat(over.numberInInnings(), is(0));
        assertThat(over.striker().player(), is(opener1));
        assertThat(over.nonStriker().player(), is(opener2));
        assertThat(over.balls().size(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.bowler(), is(bowler1));

        assertThat(innings.currentOver(), is(Optional.of(over)));
        assertThat(innings.allOut(), is(false));
        assertThat(innings.numberOfBallsRemaining(), is(Optional.of(300)));
        assertThat(innings.balls().size(), is(0));
        assertThat(innings.inningsNumber(), is(1));
        assertThat(innings.batterInningsList(), contains(innings.currentStriker().get(), innings.currentNonStriker().get()));
        assertThat(innings.battingTeam(), sameInstance(nz));
        assertThat(innings.bowlingTeam(), sameInstance(aus));
        assertThat(innings.currentPartnership().get().balls().size(), is(0));
        assertThat(innings.currentPartnership().get().firstBatter(), sameInstance(innings.currentStriker().get().player()));
        assertThat(innings.currentPartnership().get().secondBatter(), sameInstance(innings.currentNonStriker().get().player()));
        assertThat(innings.partnerships(), contains(innings.currentPartnership().get()));
        assertThat(innings.wicketsRemaining(), is(10));
        assertThat(innings.bowlerInningsList().isEmpty(), is(false));
    }

    @Test
    public void ballsAreAdded() {

        Innings innings = firstInnings
            .onEvent(overStarting()
                .withBowler(bowler1)
                .withStriker(opener1)
                .withNonStriker(opener2)
                .withBallsInOver(6)
                .build());

        assertThat(innings.currentOver().get().numberInInnings(), is(0));

        innings = innings.onEvent(ballCompleted()
            .withBowler(bowler1)
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withRunsScored(SINGLE)
            .withPlayersCrossed(true)
            .build());

        assertThat(innings.balls().size(), is(1));
        assertThat(innings.balls().score().teamRuns(), is(1));
        assertThat(innings.currentStriker().get().player(), is(opener2));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));
        assertThat(innings.currentPartnership().get().score().teamRuns(), is(1));
        assertThat(innings.currentPartnership().get().endTime(), is(Optional.empty()));
        assertThat(innings.currentPartnership().get().firstBatterContribution().size(), is(1));
        assertThat(innings.currentPartnership().get().firstBatterContribution().score().teamRuns(), is(1));
        assertThat(innings.currentPartnership().get().secondBatterContribution().size(), is(0));
        assertThat(innings.currentPartnership().get().secondBatterContribution().score().teamRuns(), is(0));

        innings = innings.onEvent(ballCompleted()
            .withRunsScored(Score.TWO)
            .build());
        assertThat(innings.currentStriker().get().player(), is(opener2));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));
        assertThat(innings.currentPartnership().get().score().teamRuns(), is(3));

        innings = innings.onEvent(ballCompleted()
            .withRunsScored(Score.THREE)
            .withPlayersCrossed(true)
            .build());
        assertThat(innings.currentStriker().get().player(), is(opener1));
        assertThat(innings.currentNonStriker().get().player(), is(opener2));
        assertThat(innings.currentPartnership().get().score().teamRuns(), is(6));

        innings = innings
            .onEvent(dotBall())
            .onEvent(dotBall())
            .onEvent(ballCompleted()
                .withRunsScored(Score.WIDE)
                .build());

        assertThat(innings.currentOver().get().isComplete(), is(false));
        assertThat(innings.currentOver().get().remainingBalls(), is(1));

        innings = innings
            .onEvent(ballCompleted()
                .withDismissal(DismissalType.Bowled, null)
                .build());
        Over over = innings.currentOver().get();
        assertThat(over.isComplete(), is(true));
        assertThat(over.remainingBalls(), is(0));
        assertThat(over.balls().score().teamRuns(), is(7));
        assertThat(over.balls().score().wickets(), is(1));
        assertThat(innings.state(), is(Innings.State.IN_PROGRESS));

        assertThat(innings.currentPartnership(), is(Optional.empty()));
        assertThat(innings.partnerships().last().get().score().teamRuns(), is(7));
        assertThat(innings.partnerships().last().get().endTime().isPresent(), is(true));
        assertThat(innings.partnerships().last().get().firstBatterContribution().size(), is(5));
        assertThat(innings.partnerships().last().get().firstBatterContribution().score().validDeliveries(), is(4));
        assertThat(innings.partnerships().last().get().firstBatterContribution().score().teamRuns(), is(2));
        assertThat(innings.partnerships().last().get().firstBatterContribution().score().batterRuns(), is(1));
        assertThat(innings.partnerships().last().get().secondBatterContribution().size(), is(2));
        assertThat(innings.partnerships().last().get().secondBatterContribution().score().teamRuns(), is(5));


        innings = innings.onEvent(overCompleted().build());
        assertThat(innings.state(), is(Innings.State.BETWEEN_OVERS));
        assertThat(innings.currentOver(), is(Optional.empty()));

        innings = innings
            .onEvent(batterInningsStarting().build())
            .onEvent(overStarting().withBowler(bowler2).build())
            .onEvent(ballCompleted().withRunsScored(SINGLE).build())
            .onEvent(inningsCompleted().build());

        assertThat(innings.currentOver(), is(Optional.empty()));
        assertThat(innings.state(), is(Innings.State.COMPLETED));
    }

    @Test
    public void ifBattersNotSpecifiedOnOverStartingThenItIsAssumed() {
        Innings innings = firstInnings.onEvent(oneBallOverStarting(bowler1));
        assertThat(innings.currentStriker().get().player(), is(opener1));
        assertThat(innings.currentNonStriker().get().player(), is(opener2));
        innings = innings.onEvent(dotBall());
        assertThat(innings.currentStriker().get().player(), is(opener1));
        assertThat(innings.currentNonStriker().get().player(), is(opener2));
        innings = innings.onEvent(overCompleted().build());
        assertThat(innings.currentStriker().get().player(), is(opener1));
        assertThat(innings.currentNonStriker().get().player(), is(opener2));
        innings = innings.onEvent(oneBallOverStarting(bowler2)); // players cross at beginning of an over
        assertThat(innings.currentStriker().get().player(), is(opener2));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));
        innings = innings.onEvent(single());
        assertThat(innings.currentStriker().get().player(), is(opener1));
        assertThat(innings.currentNonStriker().get().player(), is(opener2));
        innings = innings.onEvent(overCompleted().build()).onEvent(oneBallOverStarting(bowler1));
        assertThat(innings.currentStriker().get().player(), is(opener2));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));
    }

    @Test
    public void oversBowledOneOverApartByTheSameBowlerAreConsideredInTheSameSpell() {
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
        assertThat(innings.bowlerInningsList().get(1).overs().size(), is(2));
        assertThat(innings.bowlerInningsList().get(1).spells().get(0).spellNumber(), is(1));
        assertThat(innings.bowlerInningsList().get(1).spells().get(1).spellNumber(), is(2));
        assertThat(innings.bowlerInningsList().get(2).balls().size(), is(1));
        assertThat(innings.bowlerInningsList().get(2).bowler(), is(bowler3));
        assertThat(innings.bowlerInningsList().get(2).spells().size(), is(1));
        assertThat(innings.bowlerInningsList().get(2).spells().get(0).spellNumber(), is(1));

        assertThat(innings.maidens(), is(1));
        assertThat(innings.overs().size(), is(6));
    }

    @Test
    public void oneBowlerCanTakeOverAnotherDuringAnOver() {

        Innings innings = firstInnings.onEvent(overStarting().withBowler(bowler1).withBallsInOver(3).build())
            .onEvent(single())
            .onEvent(dotBall())
            .onEvent(ballCompleted().withRunsScored(FOUR).withBowler(bowler2).build());
        assertThat(innings.bowlerInningsList().size(), is(2));
        BowlerInnings bowler1Innings = innings.bowlerInningsList().get(0);
        BowlerInnings bowler2Innings = innings.bowlerInningsList().get(1);
        assertThat(bowler1Innings.balls().size(), is(2));
        assertThat(bowler2Innings.balls().size(), is(1));
        assertThat(bowler1Innings.spells().get(0).overs().get(0).numberInInnings(), is(bowler2Innings.spells().get(0).overs().get(0).numberInInnings()));
        assertThat(innings.overs(), contains(bowler2Innings.spells().get(0).overs().get(0)));

        assertThat(bowler1Innings.balls().score().runsPerOver().toString(), is("3.0"));
        assertThat(bowler2Innings.balls().score().runsPerOver().toString(), is("24.0"));

    }

    @Test
    void aSharedOverWithNoRunsHasTheMaidenAscribedToTheTeamButNotTheBowlers() {
        Innings innings = firstInnings.onEvent(overStarting().withBowler(bowler1).withBallsInOver(3).build())
            .onEvent(dotBall())
            .onEvent(dotBall())
            .onEvent(ballCompleted().withRunsScored(Score.DOT_BALL).withBowler(bowler2).build())
            .onEvent(overCompleted().build());
        assertThat(innings.maidens(), is(1));
        assertThat(innings.bowlerInningsList().get(0).maidens(), is(0));
        assertThat(innings.bowlerInningsList().get(0).spells().get(0).maidens(), is(0));
        assertThat(innings.bowlerInningsList().get(1).maidens(), is(0));
        assertThat(innings.bowlerInningsList().get(1).spells().get(0).maidens(), is(0));

        innings = innings.onEvent(overStarting().withBowler(bowler3).withBallsInOver(2).build())
            .onEvent(dotBall())
            .onEvent(dotBall())
            .onEvent(overCompleted().build())
            .onEvent(overStarting().withBowler(bowler2).withBallsInOver(2).build())
            .onEvent(dotBall())
            .onEvent(dotBall())
            .onEvent(overCompleted().build());
        assertThat(innings.maidens(), is(3));
        assertThat(innings.bowlerInningsList().get(0).maidens(), is(0));
        assertThat(innings.bowlerInningsList().get(0).spells().get(0).maidens(), is(0));
        assertThat(innings.bowlerInningsList().get(1).maidens(), is(1));
        assertThat(innings.bowlerInningsList().get(1).spells().get(0).maidens(), is(1));
        assertThat(innings.bowlerInningsList().get(2).maidens(), is(1));
        assertThat(innings.bowlerInningsList().get(2).spells().get(0).maidens(), is(1));
    }

    @Test
    void runsAreAscribedToTheFacingBatter() {
        Innings innings = firstInnings.onEvent(overStarting().withBowler(bowler1).withBallsInOver(6).build())
            .onEvent(single())
            .onEvent(single())
            .onEvent(single());
        assertThat(innings.batterInningsList().size(), is(2));

        assertThat(innings.currentStriker().get().player(), is(opener2));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));

        BatterInnings o1i = innings.batterInningsList().get(0);
        assertThat(o1i.player(), is(sameInstance(opener1)));
        assertThat(o1i.isNotOut(), is(true));
        assertThat(o1i.isOut(), is(false));
        assertThat(o1i.numberCameIn(), is(1));
        assertThat(o1i.balls().size(), is(2));
        assertThat(o1i.balls().score().batterRuns(), is(2));

        BatterInnings o2i = innings.batterInningsList().get(1);
        assertThat(o2i.player(), is(sameInstance(opener2)));
        assertThat(o2i.isNotOut(), is(true));
        assertThat(o2i.isOut(), is(false));
        assertThat(o2i.numberCameIn(), is(2));
        assertThat(o2i.balls().size(), is(1));
        assertThat(o2i.balls().score().batterRuns(), is(1));
    }

    @Test
    void exceptionThrownIfBallIsBowledWithoutBatterComingIn() {
        assertThat(firstInnings.partnerships().size(), is(1));
        assertThat(firstInnings.partnerships().get(0).brokenByWicket(), is(false));
        assertThat(firstInnings.partnerships().get(0).wicketNumber(), is(1));
        Innings innings = firstInnings.onEvent(overStarting().withBowler(bowler1).withBallsInOver(6).build())
            .onEvent(single())
            .onEvent(wicket());

        assertThrows(IllegalStateException.class, () -> innings.onEvent(single()));
        assertThat(innings.currentStriker(), is(Optional.empty()));
        assertThat(innings.currentNonStriker().get().player(), is(opener1));
    }

    @Test
    void whenBattersGetOutNewPartnershipsAreMade() {

        assertThat(firstInnings.partnerships().size(), is(1));
        assertThat(firstInnings.partnerships().get(0).wicketNumber(), is(1));
        assertThat(firstInnings.partnerships().get(0).startTime(), is(not(nullValue())));
        Innings innings = firstInnings.onEvent(overStarting().withBowler(bowler1).withBallsInOver(6).build())
            .onEvent(single())
            .onEvent(single())
            .onEvent(single())
            .onEvent(wicket())
            .onEvent(batterInningsStarting().build())
            .onEvent(four())
            .onEvent(dotBall())
            .onEvent(overCompleted().build());
        ImmutableList<BatterInnings> batters = innings.batterInningsList();
        assertThat(batters.size(), is(3));
        assertThat(batters.get(0).player(), is(sameInstance(opener1)));
        assertThat(batters.get(1).player(), is(sameInstance(opener2)));
        assertThat(batters.get(2).player(), is(sameInstance(number3)));
        assertThat(innings.yetToBat(), equalTo(nz.getPlayers().view(3, 10)));
        assertThat(innings.partnerships().size(), is(2));
        assertThat(innings.partnerships().get(0).brokenByWicket(), is(true));
        assertThat(innings.partnerships().get(1).brokenByWicket(), is(false));
    }

    private BallCompletedEvent wicket() {
        return ballCompleted().withRunsScored(WICKET).withDismissal(DismissalType.Bowled, null).build();
    }

    private BallCompletedEvent four() {
        return ballCompleted().withRunsScored(FOUR).build();
    }

    private BallCompletedEvent single() {
        return ballCompleted().withRunsScored(SINGLE).withPlayersCrossed(true).build();
    }

    private BallCompletedEvent dotBall() {
        return ballCompleted().withRunsScored(Score.DOT_BALL).build();
    }

    private OverStartingEvent oneBallOverStarting(Player bowler1) {
        return overStarting().withBowler(bowler1).withBallsInOver(1).build();
    }

}