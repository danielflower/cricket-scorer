package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.BatterInnings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Optional;

/**
 * Indicates that a batter's innings has ended due to dismissal, retirement, or that the team innings ended.
 */
public final class BatterInningsEndedEvent implements MatchEvent {

    private final Instant time;
    private final Player batter;
    private final BatterInnings.State reason;

    private BatterInningsEndedEvent(Instant time, Player batter, BatterInnings.State reason) {
        this.time = time;
        this.batter = batter;
        this.reason = reason;
        if (reason == BatterInnings.State.IN_PROGRESS) {
            throw new IllegalArgumentException("The reason for a batter innings ending cannot be that it is " + reason);
        }
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public Player batter() {
        return batter;
    }

    /**
     * @return The reason the innings is ended
     */
    public BatterInnings.State reason() {
        return reason;
    }

    public static final class Builder implements MatchEventBuilder<BatterInningsEndedEvent> {
        private Instant time;
        private Player batter;
        private BatterInnings.State reason;

        /**
         * @param startTime The time the innings ended
         * @return This builder
         */
        public Builder withTime(Instant startTime) {
            this.time = startTime;
            return this;
        }

        /**
         * Specifies the next batter. Leave null to go with the next batter in the line up.
         *
         * @param batter The batter to go in next, or null to continue with the next batter in the line up.
         * @return This builder
         */
        public Builder withBatter(Player batter) {
            this.batter = batter;
            return this;
        }

        /**
         * @param reason The reason that the innings ended
         * @return This builder
         */
        public Builder withReason(BatterInnings.State reason) {
            this.reason = reason;
            return this;
        }

        public BatterInningsEndedEvent build(Match match) {
            return new BatterInningsEndedEvent(time, batter, reason);
        }
    }
}
