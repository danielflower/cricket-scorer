package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MatchEventListenerTest {
    private final LineUp aus = Australia.oneDayLineUp().build();
    private final LineUp nz = NewZealand.oneDayLineUp().build();
    private final Player opener1 = nz.battingOrder().get(0);
    private final Player bowler1 = aus.battingOrder().get(10);

    private final CollectingListener collectingListener = new CollectingListener();

    @Test
    public void eventsAreRaisedToListeners() {
        MatchControl control = MatchControl.newMatch(
            MatchEvents.matchStarting(MatchType.ODI).withTeamLineUps(ImmutableList.of(aus, nz))
                .withEventListeners(data -> null, collectingListener)
        );
        assertThat(collectingListener.size(), is(1));
        assertThat(collectingListener.get(0).event(), instanceOf(MatchStartingEvent.class));

        control.onEvent(inningsStarting().withBattingTeam(nz));

        assertThat(collectingListener.size(), is(4));
        assertThat(collectingListener.get(0).event(), instanceOf(MatchStartingEvent.class));
        assertThat(collectingListener.get(1).event(), instanceOf(InningsStartingEvent.class));
        assertThat(collectingListener.get(2).event(), instanceOf(BatterInningsStartingEvent.class));
        assertThat(collectingListener.get(3).event(), instanceOf(BatterInningsStartingEvent.class));
    }


    @Test
    public void customEventsCanBeRaised() {

        class OffTheMarkEventRaiser implements MatchEventListener {

            @Override
            public ImmutableList<MatchEventBuilder<?, ?>> onEvent(MatchEventData data) {
                OffTheMarkEvent.Builder offTheMarkEvent = data.eventAs(BallCompletedEvent.class).map(ball -> {
                    int runs = ball.score().batterRuns();
                    if (runs > 0) {
                        Player batter = ball.striker();
                        Innings innings = data.match().currentInnings().orElseThrow();
                        if (innings.batterInnings(batter).score().batterRuns() == runs) {
                            return new OffTheMarkEvent.Builder().withPlayer(batter);
                        }
                    }

                    return null;
                }).orElse(null);

                return offTheMarkEvent == null ? null : ImmutableList.of(offTheMarkEvent);
            }
        }

        CollectingListener afterListener = new CollectingListener();

        MatchControl control = MatchControl.newMatch(
            MatchEvents.matchStarting(MatchType.ODI).withTeamLineUps(ImmutableList.of(aus, nz))
                .withEventListeners(collectingListener, new OffTheMarkEventRaiser(), afterListener)
        ).onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting(bowler1))
            .onEvent(ballCompleted("0"));

        assertThat(collectingListener.events(), not(hasItem(instanceOf(OffTheMarkEvent.class))));

        control.onEvent(ballCompleted("4"));

        assertThat(collectingListener.last().event(), instanceOf(OffTheMarkEvent.class));
        assertThat(collectingListener.last().eventAs(OffTheMarkEvent.class).orElseThrow().player, is(opener1));

        assertThat(collectingListener.events(), equalTo(afterListener.events()));
    }

    private static class OffTheMarkEvent extends BaseMatchEvent {

        final Player player;

        protected OffTheMarkEvent(String id, Instant time, String generatedBy, Player player) {
            super(id, time, generatedBy);
            this.player = player;
        }

        @Override
        public Builder newBuilder() {
            return new Builder()
                .withPlayer(player)
                .withID(id())
                .withTime(time().orElse(null))
                .withGeneratedBy(generatedBy().orElse(null));
        }

        static class Builder extends BaseMatchEventBuilder<OffTheMarkEvent.Builder, OffTheMarkEvent> {

            private Player player;

            @Override
            public OffTheMarkEvent build(Match match) {
                return new OffTheMarkEvent(id(), time(), generatedBy(), player);
            }

            public Builder withPlayer(Player striker) {
                this.player = striker;
                return this;
            }
        }
    }

    private static class CollectingListener implements MatchEventListener {

        private final List<MatchEventData> received = new ArrayList<>();

        @Override
        public ImmutableList<MatchEventBuilder<?, ?>> onEvent(MatchEventData data) throws Exception {
            received.add(data);
            return null;
        }

        public MatchEventData get(int index) {
            return received.get(index);
        }

        public int size() {
            return received.size();
        }

        public List<MatchEvent> events() {
            return received.stream().map(MatchEventData::event).collect(Collectors.toList());
        }

        public MatchEventData last() {
            return received.get(received.size() - 1);
        }
    }

}