package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Objects;

public final class BatterInningsStartingEvent extends BaseMatchEvent {

    private final Player batter;

    private BatterInningsStartingEvent(String id, MatchEvent generatedBy, Instant time, Player batter) {
        super(id, time, generatedBy);
        this.batter = Objects.requireNonNull(batter);
    }

    public Player batter() {
        return batter;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, BatterInningsStartingEvent> {
        private Player batter;

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

        public BatterInningsStartingEvent build(Match match) {
            Player batter = this.batter;
            Innings innings = match.currentInnings()
                .orElseThrow(() -> new IllegalStateException("A batter innings cannot be started if there is no team innings in progress"));
            if (batter == null) {
                batter = innings
                    .yetToBat().first()
                    .orElseThrow(() -> new IllegalStateException("There are no batters left to send in next"));
            }

            if (!innings.battingTeam().battingOrder().contains(batter)){
                throw new IllegalStateException("The player " + batter + " is not in the batting team " + innings.battingTeam());
            }

            return new BatterInningsStartingEvent(id(), generatedBy(), time(), batter);
        }
    }
}
