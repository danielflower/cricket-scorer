package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Immutable
public final class BatterInningsStartingEvent extends BaseMatchEvent {

    private final Player batter;

    private BatterInningsStartingEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint, Player batter) {
        super(id, time, customData, undoPoint);
        this.batter = Objects.requireNonNull(batter);
    }

    public Player batter() {
        return batter;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return baseBuilder(new Builder())
            .withBatter(batter)
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BatterInningsStartingEvent that = (BatterInningsStartingEvent) o;
        return Objects.equals(batter, that.batter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batter);
    }

    @Override
    public String toString() {
        return "BatterInningsStartingEvent{" +
            "batter=" + batter +
            '}';
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, BatterInningsStartingEvent> {
        private Player batter;

        public Player batter() {
            return batter;
        }

        /**
         * Specifies the next batter. Leave null to go with the next batter in the line up.
         *
         * @param batter The batter to go in next, or null to continue with the next batter in the line up.
         * @return This builder
         */
        public @Nonnull Builder withBatter(@Nullable Player batter) {
            this.batter = batter;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null)
                throw new IllegalStateException("A batter innings cannot be started if there is no team innings in progress");
            if (batter == null) {
                batter = innings.yetToBat().first();
                if (batter == null) throw new IllegalStateException("There are no batters left to send in next");
            }

            if (!innings.battingTeam().battingOrder().contains(batter)) {
                throw new IllegalStateException("The player " + batter + " is not in the batting team " + innings.battingTeam());
            }

            return this;
        }

        @Nonnull
        public BatterInningsStartingEvent build() {
            return new BatterInningsStartingEvent(id(), time(), customData(), undoPoint(), this.batter);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(batter, builder.batter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), batter);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "batter=" + batter +
                "} " + super.toString();
        }
    }
}
