package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Optional;

public final class BatterInningsStartingEvent implements MatchEvent {

    private final Instant time;
    private final Player batter;

    private BatterInningsStartingEvent(Instant time, Player batter) {
        this.time = time;
        this.batter = batter;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public Player batter() {
        return batter;
    }

    public static Builder batterInningsStarting() {
        return new Builder();
    }

    public static final class Builder implements MatchEventBuilder<BatterInningsStartingEvent> {
        private Instant time;
        private Player batter;

        public Builder withTime(Instant startTime) {
            this.time = startTime;
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

        public BatterInningsStartingEvent build(Match match) {
            return new BatterInningsStartingEvent(time, batter);
        }
    }
}
