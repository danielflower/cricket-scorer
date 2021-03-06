package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.danielflower.crickam.scorer.Score.SINGLE;
import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InningsTest {

    private final SimpleLineUp aus = Australia.oneDayLineUp().build();
    private final SimpleLineUp nz = NewZealand.oneDayLineUp().build();
    private final Player opener1 = nz.battingOrder().get(0);
    private final Player opener2 = nz.battingOrder().get(1);
    private final Player number3 = nz.battingOrder().get(2);
    private MatchControl control = MatchControl.newMatch(
        MatchEvents.matchStarting(1, 50).withTeamLineUps(ImmutableList.of(aus, nz)).build()
    )
        .onEvent(inningsStarting().withBattingTeam(nz))
        .onEvent(batterInningsStarting())
        .onEvent(batterInningsStarting());

    private final Player bowler1 = aus.battingOrder().get(10);
    private final Player bowler2 = aus.battingOrder().get(9);
    private final Player bowler3 = aus.battingOrder().get(8);

    private Innings innings() {
        return control.currentInnings();
    }

    @Test
    void beforeAnyEventsThingsAreEmpty() {
        assertThat(innings().state(), is(Innings.State.NOT_STARTED));
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        assertThat(innings().yetToBat(), contains(nz.battingOrder().get(2), nz.battingOrder().get(3), nz.battingOrder().get(4), nz.battingOrder().get(5), nz.battingOrder().get(6), nz.battingOrder().get(7), nz.battingOrder().get(8), nz.battingOrder().get(9), nz.battingOrder().get(10)));
        assertThat(innings().originalMaxOvers(), is(Integer.valueOf(50)));
        assertThat(innings().overs().isEmpty(), is(true));
        assertThat(innings().currentOver(), is(nullValue()));
        assertThat(innings().allOut(), is(false));
        assertThat(innings().numberOfBallsRemaining(), is(Integer.valueOf(300)));
        assertThat(innings().balls().size(), is(0));
        assertThat(innings().inningsNumber(), is(1));
        assertThat(innings().batterInningsList().size(), is(2));
        assertThat(innings().battingTeam(), sameInstance(nz));
        assertThat(innings().bowlingTeam(), sameInstance(aus));
        assertThat(innings().currentPartnership().balls().size(), is(0));
        assertThat(innings().partnerships(), contains(innings().currentPartnership()));
        assertThat(innings().wicketsRemaining(), is(10));
        assertThat(innings().bowlerInningsList().isEmpty(), is(true));
    }

    @Test
    void aNewOverEventAddsToTheOverList() {
        control = control.onEvent(overStarting()
            .withBowler(bowler1)
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withBallsInOver(6)
        );

        assertThat(innings().state(), is(Innings.State.IN_PROGRESS));
        assertThat(innings().currentStriker().player(), sameInstance(nz.battingOrder().get(0)));
        assertThat(innings().currentNonStriker().player(), sameInstance(nz.battingOrder().get(1)));
        assertThat(innings().yetToBat(), contains(nz.battingOrder().get(2), nz.battingOrder().get(3), nz.battingOrder().get(4), nz.battingOrder().get(5), nz.battingOrder().get(6), nz.battingOrder().get(7), nz.battingOrder().get(8), nz.battingOrder().get(9), nz.battingOrder().get(10)));
        assertThat(innings().originalMaxOvers(), is(Integer.valueOf(50)));
        assertThat(innings().overs().size(), is(1));
        Over over = innings().overs().get(0);
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.teamRuns(), is(0));
        assertThat(over.isMaiden(), is(false));
        assertThat(over.isComplete(), is(false));
        assertThat(over.validDeliveries(), is(0));
        assertThat(over.overNumber(), is(0));
        assertThat(over.striker(), is(opener1));
        assertThat(over.nonStriker(), is(opener2));
        assertThat(over.balls().size(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.bowler(), is(bowler1));

        assertThat(innings().currentOver(), is(over));
        assertThat(innings().allOut(), is(false));
        assertThat(innings().numberOfBallsRemaining(), is(Integer.valueOf(300)));
        assertThat(innings().balls().size(), is(0));
        assertThat(innings().inningsNumber(), is(1));
        assertThat(innings().batterInningsList(), contains(innings().currentStriker(), innings().currentNonStriker()));
        assertThat(innings().battingTeam(), sameInstance(nz));
        assertThat(innings().bowlingTeam(), sameInstance(aus));
        assertThat(innings().currentPartnership().balls().size(), is(0));
        assertThat(innings().currentPartnership().firstBatter(), sameInstance(innings().currentStriker().player()));
        assertThat(innings().currentPartnership().secondBatter(), sameInstance(innings().currentNonStriker().player()));
        assertThat(innings().partnerships(), contains(innings().currentPartnership()));
        assertThat(innings().wicketsRemaining(), is(10));
        assertThat(innings().bowlerInningsList().isEmpty(), is(false));
    }

    @Test
    public void ballsAreAdded() {

        control = control.onEvent(overStarting()
            .withBowler(bowler1)
            .withStriker(opener1)
            .withNonStriker(opener2));

        assertThat(innings().currentOver().overNumber(), is(0));

        control = control.onEvent(ballCompleted()
            .withBowler(bowler1)
            .withStriker(opener1)
            .withNonStriker(opener2)
            .withRunsScored(SINGLE)
            .withPlayersCrossed(true)
        );

        assertThat(innings().balls().size(), is(1));
        assertThat(innings().balls().score().teamRuns(), is(1));
        assertThat(innings().currentStriker().player(), is(opener2));
        assertThat(innings().currentNonStriker().player(), is(opener1));
        assertThat(innings().currentPartnership().score().teamRuns(), is(1));
        assertThat(innings().currentPartnership().endTime(), is(nullValue()));
        assertThat(innings().currentPartnership().firstBatterContribution().size(), is(1));
        assertThat(innings().currentPartnership().firstBatterContribution().score().teamRuns(), is(1));
        assertThat(innings().currentPartnership().secondBatterContribution().size(), is(0));
        assertThat(innings().currentPartnership().secondBatterContribution().score().teamRuns(), is(0));

        control = control.onEvent(ballCompleted("2"));
        assertThat(innings().currentStriker().player(), is(opener2));
        assertThat(innings().currentNonStriker().player(), is(opener1));
        assertThat(innings().currentPartnership().score().teamRuns(), is(3));

        control = control.onEvent(ballCompleted("3"));
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        assertThat(innings().currentPartnership().score().teamRuns(), is(6));

        control = control
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("1w"));

        assertThat(innings().currentOver().isComplete(), is(false));
        assertThat(innings().currentOver().remainingBalls(), is(1));

        control = control.onEvent(ballCompleted()
            .withDismissal(DismissalType.BOWLED)
            .withTime(Instant.now()))
            .onEvent(batterInningsCompleted());
        Over over = innings().currentOver();
        assertThat(over.isComplete(), is(true));
        assertThat(over.remainingBalls(), is(0));
        assertThat(over.balls().score().teamRuns(), is(7));
        assertThat(over.balls().score().wickets(), is(1));
        assertThat(innings().state(), is(Innings.State.IN_PROGRESS));

        assertThat(innings().currentPartnership(), is(nullValue()));
        assertThat(innings().partnerships().last().score().teamRuns(), is(7));
        assertThat(innings().partnerships().last().endTime(), not(nullValue()));
        assertThat(innings().partnerships().last().firstBatterContribution().size(), is(5));
        assertThat(innings().partnerships().last().firstBatterContribution().score().validDeliveries(), is(4));
        assertThat(innings().partnerships().last().firstBatterContribution().score().teamRuns(), is(2));
        assertThat(innings().partnerships().last().firstBatterContribution().score().batterRuns(), is(1));
        assertThat(innings().partnerships().last().secondBatterContribution().size(), is(2));
        assertThat(innings().partnerships().last().secondBatterContribution().score().teamRuns(), is(5));

        control = control.onEvent(overCompleted());
        assertThat(innings().state(), is(Innings.State.BETWEEN_OVERS));
        assertThat(innings().currentOver(), is(nullValue()));

        control = control
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(bowler2))
            .onEvent(ballCompleted().withRunsScored(SINGLE))
            .onEvent(inningsCompleted());

        assertThrows(IllegalStateException.class, this::innings);
        Innings last = control.match().completedInningsList().last();
        assertThat(last.currentOver().validDeliveries(), is(1));
        assertThat(last.state(), is(Innings.State.COMPLETED));
    }

    @Test
    public void ifBattersNotSpecifiedOnOverStartingThenItIsAssumed() {
        control = control.onEvent(oneBallOverStarting(bowler1));
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        control = control.onEvent(ballCompleted("0"));
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        control = control.onEvent(overCompleted());
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        control = control.onEvent(oneBallOverStarting(bowler2)); // players cross at beginning of an over
        assertThat(innings().currentStriker().player(), is(opener2));
        assertThat(innings().currentNonStriker().player(), is(opener1));
        control = control.onEvent(ballCompleted("1"));
        assertThat(innings().currentStriker().player(), is(opener1));
        assertThat(innings().currentNonStriker().player(), is(opener2));
        control = control.onEvent(overCompleted())
            .onEvent(oneBallOverStarting(bowler1));
        assertThat(innings().currentStriker().player(), is(opener2));
        assertThat(innings().currentNonStriker().player(), is(opener1));
    }

    @Test
    public void oversBowledOneOverApartByTheSameBowlerAreConsideredInTheSameSpell() {
        control = control.onEvent(oneBallOverStarting(bowler1));
        assertThat(innings().bowlerInningsList().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(0));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));

        control = control.onEvent(ballCompleted("0"));
        assertThat(innings().bowlerInningsList().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));

        control = control.onEvent(overCompleted());
        assertThat(innings().bowlerInningsList().size(), is(1));

        control = control.onEvent(oneBallOverStarting(bowler2));
        assertThat(innings().bowlerInningsList().size(), is(2));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings().bowlerInningsList().get(1).balls().size(), is(0));
        assertThat(innings().bowlerInningsList().get(1).bowler(), is(bowler2));

        control = control.onEvent(ballCompleted("1"));
        assertThat(innings().bowlerInningsList().size(), is(2));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings().bowlerInningsList().get(1).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(1).bowler(), is(bowler2));

        control = control.onEvent(overCompleted())
            .onEvent(oneBallOverStarting(bowler1))
            .onEvent(ballCompleted("1"));
        assertThat(innings().bowlerInningsList().size(), is(2));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(2));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings().bowlerInningsList().get(0).spells().size(), is(1));
        assertThat(innings().bowlerInningsList().get(0).spells().get(0).spellNumber(), is(1));
        assertThat(innings().bowlerInningsList().get(1).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(1).bowler(), is(bowler2));
        assertThat(innings().bowlerInningsList().get(1).spells().size(), is(1));
        assertThat(innings().bowlerInningsList().get(1).spells().get(0).spellNumber(), is(1));

        control = control.onEvent(overCompleted())
            .onEvent(oneBallOverStarting(bowler3))
            .onEvent(ballCompleted("1"))
            .onEvent(overCompleted())
            .onEvent(oneBallOverStarting(bowler1))
            .onEvent(ballCompleted("1"))
            .onEvent(overCompleted())
            .onEvent(oneBallOverStarting(bowler2))
            .onEvent(ballCompleted("1"))
            .onEvent(overCompleted());

        assertThat(innings().bowlerInningsList().size(), is(3));
        assertThat(innings().bowlerInningsList().get(0).balls().size(), is(3));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings().bowlerInningsList().get(0).spells().size(), is(1));
        assertThat(innings().bowlerInningsList().get(1).balls().size(), is(2));
        assertThat(innings().bowlerInningsList().get(1).bowler(), is(bowler2));
        assertThat(innings().bowlerInningsList().get(1).spells().size(), is(2));
        assertThat(innings().bowlerInningsList().get(1).overs().size(), is(2));
        assertThat(innings().bowlerInningsList().get(1).spells().get(0).spellNumber(), is(1));
        assertThat(innings().bowlerInningsList().get(1).spells().get(1).spellNumber(), is(2));
        assertThat(innings().bowlerInningsList().get(2).balls().size(), is(1));
        assertThat(innings().bowlerInningsList().get(2).bowler(), is(bowler3));
        assertThat(innings().bowlerInningsList().get(2).spells().size(), is(1));
        assertThat(innings().bowlerInningsList().get(2).spells().get(0).spellNumber(), is(1));
        assertThat(innings().maidens(), is(1));
        assertThat(innings().overs().size(), is(6));
    }

    @Test
    public void oneBowlerCanTakeOverAnotherDuringAnOver() {

        control = control.onEvent(overStarting(bowler1).withBallsInOver(3))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("4").withBowler(bowler2));
        assertThat(innings().bowlerInningsList().size(), is(2));
        BowlerInnings bowler2Innings = innings().bowlerInningsList().get(1);
        BowlerInnings bowler1Innings = innings().bowlerInningsList().get(0);
        assertThat(bowler1Innings.balls().size(), is(2));
        assertThat(bowler2Innings.balls().size(), is(1));
        assertThat(bowler1Innings.spells().get(0).overs().get(0).overNumber(), is(bowler2Innings.spells().get(0).overs().get(0).overNumber()));
        assertThat(innings().overs(), contains(bowler2Innings.spells().get(0).overs().get(0)));

        assertThat(bowler1Innings.balls().score().runsPerOver().toString(), is("3.0"));
        assertThat(bowler2Innings.balls().score().runsPerOver().toString(), is("24.0"));

    }

    @Test
    void aSharedOverWithNoRunsHasTheMaidenAscribedToTheTeamButNotTheBowlers() {
        control = control.onEvent(overStarting(bowler1).withBallsInOver(3))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted().withRunsScored(Score.DOT_BALL).withBowler(bowler2))
            .onEvent(overCompleted());
        assertThat(innings().maidens(), is(1));
        assertThat(innings().bowlerInningsList().get(0).bowler(), is(bowler1));
        assertThat(innings().bowlerInningsList().get(0).maidens(), is(0));
        assertThat(innings().bowlerInningsList().get(0).spells().get(0).maidens(), is(0));
        assertThat(innings().bowlerInningsList().get(1).bowler(), is(bowler2));
        assertThat(innings().bowlerInningsList().get(1).maidens(), is(0));
        assertThat(innings().bowlerInningsList().get(1).spells().get(0).maidens(), is(0));

        control = control.onEvent(overStarting(bowler3).withBallsInOver(2))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted())
            .onEvent(overStarting(bowler2).withBallsInOver(2))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted());
        assertThat(innings().maidens(), is(3));
        assertThat(innings().bowlerInningsList().get(0).maidens(), is(0));
        assertThat(innings().bowlerInningsList().get(0).spells().get(0).maidens(), is(0));
        assertThat(innings().bowlerInningsList().get(1).maidens(), is(1));
        assertThat(innings().bowlerInningsList().get(1).spells().get(0).maidens(), is(1));
        assertThat(innings().bowlerInningsList().get(2).maidens(), is(1));
        assertThat(innings().bowlerInningsList().get(2).spells().get(0).maidens(), is(1));
    }

    @Test
    void runsAreAscribedToTheFacingBatter() {
        control = control.onEvent(overStarting(bowler1))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("1"));
        assertThat(innings().batterInningsList().size(), is(2));
        assertThat(innings().currentStriker().player(), is(opener2));
        assertThat(innings().currentNonStriker().player(), is(opener1));

        BatterInnings o1i = innings().batterInningsList().get(0);
        assertThat(o1i.player(), is(sameInstance(opener1)));
        assertThat(o1i.state(), is(BattingState.IN_PROGRESS));
        assertThat(o1i.numberCameIn(), is(1));
        assertThat(o1i.balls().size(), is(2));
        assertThat(o1i.balls().score().batterRuns(), is(2));

        BatterInnings o2i = innings().batterInningsList().get(1);
        assertThat(o2i.player(), is(sameInstance(opener2)));
        assertThat(o2i.state(), is(BattingState.IN_PROGRESS));
        assertThat(o2i.numberCameIn(), is(2));
        assertThat(o2i.balls().size(), is(1));
        assertThat(o2i.balls().score().batterRuns(), is(1));
    }

    @Test
    void exceptionThrownIfBallIsBowledWithoutBatterComingIn() {
        assertThat(innings().partnerships().size(), is(1));
        assertThat(innings().partnerships().get(0).brokenByWicket(), is(false));
        assertThat(innings().partnerships().get(0).wicketNumber(), is(1));
        control = control.onEvent(overStarting(bowler1))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsCompleted())
        ;

        assertThrows(IllegalStateException.class, () -> control.onEvent(ballCompleted("1")));
        assertThat(innings().currentStriker(), is(nullValue()));
        assertThat(innings().currentNonStriker().player(), is(opener1));
    }

    @Test
    void whenBattersGetOutNewPartnershipsAreMade() {

        assertThat(innings().partnerships().size(), is(1));
        assertThat(innings().partnerships().get(0).wicketNumber(), is(1));
        assertThat(innings().partnerships().get(0).startTime(), is(nullValue()));
        control = control.onEvent(overStarting(bowler1))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsCompleted())
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("4"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted());
        ImmutableList<BatterInnings> batters = innings().batterInningsList();
        assertThat(batters.size(), is(3));
        assertThat(batters.get(0).player(), is(sameInstance(opener1)));
        assertThat(batters.get(1).player(), is(sameInstance(opener2)));
        assertThat(batters.get(2).player(), is(sameInstance(number3)));
        assertThat(innings().yetToBat(), equalTo(nz.battingOrder().subList(3, 10)));
        assertThat(innings().partnerships().size(), is(2));
        assertThat(innings().partnerships().get(0).brokenByWicket(), is(true));
        assertThat(innings().partnerships().get(1).brokenByWicket(), is(false));
    }

    @Test
    void theCurrentAndPreviousBowlersAreKnown() {
        assertThat(innings().currentBowler(), nullValue());
        assertThat(innings().previousBowler(), nullValue());
        control = control.onEvent(overStarting(bowler1));
        assertThat(innings().currentBowler().bowler(), is(bowler1));
        assertThat(innings().previousBowler(), nullValue());

        control = control.onEvent(ballCompleted("1"));
        assertThat(innings().currentBowler().bowler(), is(bowler1));
        assertThat(innings().previousBowler(), nullValue());

        control = control.onEvent(overCompleted());
        assertThat(innings().currentBowler(), nullValue());
        assertThat(innings().previousBowler().bowler(), is(bowler1));

        control = control.onEvent(overStarting(bowler2));
        assertThat(innings().currentBowler().bowler(), is(bowler2));
        assertThat(innings().previousBowler().bowler(), is(bowler1));

    }

    private OverStartingEvent.Builder oneBallOverStarting(Player bowler1) {
        return overStarting().withBowler(bowler1).withBallsInOver(1);
    }

}