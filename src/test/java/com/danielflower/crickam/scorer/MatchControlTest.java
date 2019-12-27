package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.data.England.MAHMOOD;
import static com.danielflower.crickam.scorer.data.England.TOM_CURRAN;
import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.BatterInningsStartingEvent.batterInningsStarting;
import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static scaffolding.BatterInningsMatcher.withBatter;

class MatchControlTest {

    private final LineUp nz = LineUp.lineUp()
        .withCaptain(KANE_WILLIAMSON)
        .withWicketKeeper(TOM_LATHAM)
        .withTeam(team().build())
        .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR, TOM_LATHAM,
            JAMES_NEESHAM, COLIN_DE_GRANDHOMME, MITCH_SANTNER, MATT_HENRY, TRENT_BOULT, LOCKIE_FERGUSON)).build();
    private final LineUp aus = Australia.oneDayLineUp().build();

    private MatchControl control = MatchControl.newMatch(
        MatchTest.aMatch()
            .withNumberOfInningsPerTeam(1)
            .withOversPerInnings(50)
            .withTeams(ImmutableList.of(nz, aus))
            .build()
    );

    private Match match = control.onEvent(inningsStarting()
        .withBattingTeam(nz)
        .withBowlingTeam(aus)
        .withOpeners(nz.battingOrder().get(0), nz.battingOrder().get(1))
        .withTime(Instant.now())
    );

    @Test
    public void eventsAreAdded() {
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
        assertThat(over.striker(), is(sameInstance(nz.battingOrder().get(0))));
        assertThat(over.nonStriker(), is(sameInstance(nz.battingOrder().get(1))));
        assertThat(over.runs(), is(0));
    }

    @Test
    public void batterInningsChangeAfterWickets() {
        // batting order: MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR
        Player bowler1 = aus.battingOrder().get(10);
        Player bowler2 = aus.battingOrder().get(9);

        control.onEvent(overStarting().withBowler(bowler1));
        control.onEvent(BallCompletedEvent.ballCompleted("1lb"));
        control.onEvent(BallCompletedEvent.ballCompleted("1"));
        control.onEvent(BallCompletedEvent.ballCompleted("0"));
        control.onEvent(BallCompletedEvent.ballCompleted("4"));
        control.onEvent(BallCompletedEvent.ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(TOM_CURRAN));

        assertThat(curInnings().currentStriker(), is(Optional.empty()));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));

        control.onEvent(batterInningsStarting());
        assertThat(curInnings().currentStriker().get(), is(withBatter(KANE_WILLIAMSON)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));
        control.onEvent(BallCompletedEvent.ballCompleted("0"));
        control.onEvent(overCompleted());

        assertThat(curInnings().currentStriker().get(), is(withBatter(KANE_WILLIAMSON)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));

        control.onEvent(overStarting().withBowler(bowler2));
        assertThat(curInnings().currentStriker().get(), is(withBatter(HENRY_NICHOLLS)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(KANE_WILLIAMSON)));

        control.onEvent(BallCompletedEvent.ballCompleted("1"));
        control.onEvent(BallCompletedEvent.ballCompleted("1"));
        control.onEvent(BallCompletedEvent.ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(MAHMOOD));
        control.onEvent(batterInningsStarting());
        control.onEvent(BallCompletedEvent.ballCompleted("0"));
        control.onEvent(BallCompletedEvent.ballCompleted("4"));
        control.onEvent(BallCompletedEvent.ballCompleted("1w"));
        control.onEvent(BallCompletedEvent.ballCompleted("0"));
        control.onEvent(overCompleted());


        System.out.println("innings.currentStriker() = " + curInnings().currentStriker());
        System.out.println("innings.currentNonStriker() = " + curInnings().currentNonStriker());
        System.out.println(curInnings().batterInningsList());
    }

    @NotNull
    private Innings curInnings() {
        return control.current().currentInnings().get();
    }

}