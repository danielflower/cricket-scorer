package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.MatchControl;
import com.danielflower.crickam.scorer.Score;
import com.danielflower.crickam.scorer.SimpleLineUp;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.events.MatchEvents.batterInningsStarting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class InningsStartingEventTest {

    private final SimpleLineUp nz = NewZealand.oneDayLineUp().build();
    private final SimpleLineUp aus = Australia.oneDayLineUp().build();
    private MatchControl control = MatchControl.newMatch(
        MatchEvents.matchStarting(1, 50).withTeamLineUps(ImmutableList.of(nz, aus)).build()
    );

    @Test
    void canBeRebuilt() {
        InningsStartingEvent original = new InningsStartingEvent.Builder()
            .withFinalInnings(true)
            .withBowlingTeam(nz)
            .withBattingTeam(aus)
            .withInningsNumberForBattingTeam(2)
            .withStartingScore(Score.FOUR)
            .withInningsNumberForMatch(4)
            .withMaxBalls(300)
            .withMaxOvers(50)
            .withTarget(222)
            .withFollowingOn(true)
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

    @Test
    public void parentEventIsFirstOpenerEvent() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
        ;
        MatchEvent parent = control.parent().event();
        assertThat(parent, is(instanceOf(BatterInningsStartingEvent.class)));
        assertThat(((BatterInningsStartingEvent) parent).batter(), is(nz.battingOrder().get(0)));
    }

    @Test
    public void hasParentIsTrueExceptForFirstEvent() {
        assertThat(control.hasParent(), is(false));
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz));
        assertThat(control.hasParent(), is(true));
    }


    @Test
    public void canUndo() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(MatchEvents.overStarting(aus.battingOrder().get(10)));
        MatchControl lastEvent = control.parent();
        assertThat(lastEvent.event(), is(instanceOf(BatterInningsStartingEvent.class)));
        assertThat(((BatterInningsStartingEvent)lastEvent.event()).batter(), is(nz.battingOrder().get(1)));
    }


}