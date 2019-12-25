package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Objects;

public final class BatterInningsStartingEvent implements MatchEvent {

    private final Instant startTime;
    private final Player batter;

    private BatterInningsStartingEvent(Instant startTime, Player batter) {
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.batter = batter;
    }

    public Instant startTime() {
        return startTime;
    }

    public Player batter() {
        return batter;
    }

    public static Builder batterInningsStarting() {
        return new Builder();
    }

    public static final class Builder implements MatchEventBuilder<BatterInningsStartingEvent> {
        private Instant startTime = Instant.now();
        private Player batter;

        public Builder withStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        /**
         * Specifies the next batter. Leave null to go with the next batter in the line up.
         * @param batter The batter to go in next, or null to continue with the next batter in the line up.
         * @return This builder
         */
        public Builder withBatter(Player batter) {
            this.batter = batter;
            return this;
        }

        public BatterInningsStartingEvent build() {
            return new BatterInningsStartingEvent(startTime, batter);
        }
    }
}
