package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class BatterInningsStartingEvent implements MatchEvent {

    private final Instant time;
    private final Player batter;

    private BatterInningsStartingEvent(@Nullable Instant time, @NotNull Player batter) {
        this.time = time;
        this.batter = Objects.requireNonNull(batter);
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public Player batter() {
        return batter;
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

            return new BatterInningsStartingEvent(time, batter);
        }
    }
}
