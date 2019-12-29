package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.events.MatchEvents;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TimeZone;

import static com.danielflower.crickam.scorer.data.England.MAHMOOD;
import static com.danielflower.crickam.scorer.data.England.TOM_CURRAN;
import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static scaffolding.BatterInningsMatcher.withBatter;

class MatchControlTest {

    private final LineUp nz = LineUp.lineUp()
        .withCaptain(KANE_WILLIAMSON)
        .withWicketKeeper(TOM_LATHAM)
        .withTeam(team().build())
        .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR, TOM_LATHAM,
            JAMES_NEESHAM, COLIN_DE_GRANDHOMME, MITCHELL_SANTNER, MATT_HENRY, TRENT_BOULT, LOCKIE_FERGUSON)).build();
    private final LineUp aus = Australia.oneDayLineUp().build();

    private MatchControl control;
    @BeforeEach
    public void setup() {
        control = MatchControl.newMatch(
            MatchTest.aMatch()
                .withNumberOfInningsPerTeam(1)
                .withOversPerInnings(50)
                .withTeams(ImmutableList.of(nz, aus))
                .build()
        );
        control = control.onEvent(inningsStarting()
            .withBattingTeam(nz)
            .withTime(Instant.now())
        );
    }

    private Match match() {
        return control.match();
    }

    @Test
    public void eventsAreAdded() {
        
        assertThat(match().oversPerInnings(), is(OptionalInt.of(50)));
        assertThat(match().ballsPerInnings(), is(OptionalInt.of(300)));
        assertThat(match().currentInnings().isPresent(), is(true));

        Innings innings = match().currentInnings().get();
        assertThat(innings.inningsNumber(), is(1));
        assertThat(innings.yetToBat(), equalTo(nz.battingOrder().subList(2, 10)));

        control = control.onEvent(overStarting().withBowler(aus.battingOrder().last().get()));

        Over over = match().currentInnings().get().currentOver().get();
        assertThat(over.balls().size(), is(0));
        assertThat(over.bowler(), sameInstance(aus.battingOrder().last().get()));
        assertThat(over.numberInInnings(), is(0));
        assertThat(over.validDeliveries(), is(0));
        assertThat(over.remainingBalls(), is(6));
        assertThat(over.isComplete(), is(false));
        assertThat(over.isMaiden(), is(false));
        assertThat(over.striker(), is(sameInstance(nz.battingOrder().get(0))));
        assertThat(over.nonStriker(), is(sameInstance(nz.battingOrder().get(1))));
        assertThat(over.teamRuns(), is(0));
    }

    @Test
    public void batterInningsChangeAfterWickets() {
        // batting order: MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR
        Player bowler1 = aus.battingOrder().get(10);
        Player bowler2 = aus.battingOrder().get(9);

        control = control.onEvent(overStarting().withBowler(bowler1));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(TOM_CURRAN));

        assertThat(curInnings().currentStriker(), is(Optional.empty()));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));

        control = control.onEvent(batterInningsStarting());
        assertThat(curInnings().currentStriker().get(), is(withBatter(KANE_WILLIAMSON)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        assertThat(curInnings().currentStriker().get(), is(withBatter(KANE_WILLIAMSON)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(HENRY_NICHOLLS)));

        control = control.onEvent(overStarting().withBowler(bowler2));
        assertThat(curInnings().currentStriker().get(), is(withBatter(HENRY_NICHOLLS)));
        assertThat(curInnings().currentNonStriker().get(), is(withBatter(KANE_WILLIAMSON)));

        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(MAHMOOD));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1w"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());


        System.out.println("innings.currentStriker() = " + curInnings().currentStriker());
        System.out.println("innings.currentNonStriker() = " + curInnings().currentNonStriker());
        System.out.println(curInnings().batterInningsList());
    }

    @NotNull
    private Innings curInnings() {
        return control.match().currentInnings().get();
    }

    @Test
    public void canFigureOutInstantsBasedOnLocalTimesAndLastEventTimes() {
        TimeZone nz = TimeZone.getTimeZone("Pacific/Auckland");
        MatchControl control = MatchControl.newMatch(
            MatchEvents.matchStarting(MatchType.TEST)
                .withTeams(ImmutableList.of(this.nz, aus))
                .withTime(Crictils.localTime(nz, 2019, 9, 27, 10, 0))
                .withTimeZone(nz)
                .build()
        );
        control = control.onEvent(inningsStarting().withBattingTeam(this.nz));
        control = control.onEvent(overStarting().withBowler(aus.battingOrder().last().get()).withBallsInOver(100));

        assertThat(control.localTime(10, 30, 0).toString(), is("2019-09-26T22:30:00Z"));
        assertThat(control.localTime(14, 30, 0).toString(), is("2019-09-27T02:30:00Z"));

        // go to the next day
        control = control.onEvent(ballCompleted("0").withDateCompleted(Crictils.localTime(nz, 2019, 9, 28, 10, 0)));
        assertThat(control.localTime(10, 30, 0).toString(), is("2019-09-27T22:30:00Z"));

        // go to the next day, which happens to be the day after daylight savings ended
        control = control.onEvent(ballCompleted("0").withDateCompleted(Crictils.localTime(nz, 2019, 9, 29, 10, 0)));
        assertThat(control.localTime(10, 30, 0).toString(), is("2019-09-28T21:30:00Z"));
    }

}