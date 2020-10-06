package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.LineUp;
import com.danielflower.crickam.scorer.MatchControl;
import com.danielflower.crickam.scorer.MatchType;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class InningsStartingEventTest {

    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final LineUp aus = Australia.oneDayLineUp().build();
    private MatchControl control = MatchControl.newMatch(
        MatchEvents.matchStarting(MatchType.ODI).withTeamLineUps(ImmutableList.of(nz, aus))
    );

    @Test
    public void batterInningsEventsAreGenerated() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz));
        assertThat(control.match().currentInnings().batterInningsList().size(), is(2));
        assertThat(control.history().stream().map(MatchControl::event).collect(toList()),
            contains(instanceOf(MatchStartingEvent.class), instanceOf(InningsStartingEvent.class),
                instanceOf(BatterInningsStartingEvent.class), instanceOf(BatterInningsStartingEvent.class)));
    }

    @Test
    public void parentEventIsFirstOpenerEvent() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz));
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
    public void lastUserGeneratedEventIsInningsStartingEvent() {
        assertThat(control.atLastUserGeneratedEvent().event(), is(instanceOf(MatchStartingEvent.class)));
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz));
        MatchControl lastUserEvent = control.atLastUserGeneratedEvent();
        assertThat(lastUserEvent.event(), is(instanceOf(InningsStartingEvent.class)));
    }

    @Test
    public void undoAfterNewInningsEventIsMatchStartingEvent() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz));
        MatchControl lastUserEvent = control.undo();
        assertThat(lastUserEvent.event(), is(instanceOf(MatchStartingEvent.class)));
    }

    @Test
    public void undoCanEndOnGeneratedEvent() {
        control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz))
            .onEvent(MatchEvents.overStarting(aus.battingOrder().get(10)));
        MatchControl lastUserEvent = control.undo();
        assertThat(lastUserEvent.event(), is(instanceOf(BatterInningsStartingEvent.class)));
        assertThat(((BatterInningsStartingEvent)lastUserEvent.event()).batter(), is(nz.battingOrder().get(1)));
    }


}