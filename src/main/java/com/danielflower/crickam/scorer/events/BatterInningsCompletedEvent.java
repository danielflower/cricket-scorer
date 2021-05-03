package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.danielflower.crickam.scorer.BattingState.DISMISSED;
import static com.danielflower.crickam.scorer.BattingState.RETIRED_OUT;
import static java.util.Objects.requireNonNull;

/**
 * Indicates that a batter's innings has ended due to dismissal, retirement, or that the team innings ended.
 */
@Immutable
public final class BatterInningsCompletedEvent extends BaseMatchEvent {

    private final Player batter;
    private final BattingState reason;
    private final Dismissal dismissal;
    private final Score score;

    private BatterInningsCompletedEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint, Player batter, BattingState reason, @Nullable Dismissal dismissal, Score score) {
        super(id, time, customData, undoPoint);
        this.batter = requireNonNull(batter, "batter");
        this.reason = requireNonNull(reason, "reason");
        this.score = requireNonNull(score, "score");
        if (reason == BattingState.IN_PROGRESS) {
            throw new IllegalArgumentException("The reason for a batter innings ending cannot be that it is " + reason);
        }
        if (dismissal != null && reason != DISMISSED) {
            throw new IllegalArgumentException("The reason for a batter innings must be " + DISMISSED + " as a dismissal was supplied");
        }
        if (reason == DISMISSED && dismissal == null) {
            throw new NullPointerException("A dismissal must be provided when the reason is " + reason);
        }
        this.dismissal = dismissal;
    }

    public @Nonnull Score score() {
        return score;
    }

    public @Nonnull Player batter() {
        return batter;
    }

    public boolean notOut() {
        return reason != DISMISSED && reason != RETIRED_OUT;
    }

    /**
     * @return The reason the innings is ended
     */
    public @Nonnull BattingState reason() {
        return reason;
    }

    /**
     * @return The dismissal information, or null if {@link #reason()} is not {@link BattingState#DISMISSED}
     */
    public @Nullable Dismissal dismissal() {
        return dismissal;
    }

    @Override public @Nonnull Builder newBuilder() {
        return baseBuilder(new Builder())
            .withBatter(batter())
            .withReason(reason())
            .withDismissal(dismissal())
            .withScore(score())
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BatterInningsCompletedEvent that = (BatterInningsCompletedEvent) o;
        return Objects.equals(batter, that.batter) && reason == that.reason && Objects.equals(dismissal, that.dismissal) && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), batter, reason, dismissal, score);
    }

    @Override
    public String toString() {
        return "BatterInningsCompletedEvent{" +
            "batter=" + batter +
            ", reason=" + reason +
            ", dismissal=" + dismissal +
            ", score=" + score +
            '}';
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, BatterInningsCompletedEvent> {
        private Player batter;
        private BattingState reason;
        private Dismissal dismissal;
        private Score score;

        public @Nullable
        Player batter() {
            return batter;
        }

        public @Nullable
        BattingState reason() {
            return reason;
        }

        public @Nullable
        Dismissal dismissal() {
            return dismissal;
        }

        public @Nullable
        Score score() {
            return score;
        }

        /**
         * @param batter the batter who's innings has ended. Can be left unset if dismissal is set with a batter set.
         * @return This builder
         */
        public @Nonnull
        Builder withBatter(@Nullable Player batter) {
            this.batter = batter;
            return this;
        }

        /**
         * @param reason The reason that the innings ended
         * @return This builder
         */
        public @Nonnull
        Builder withReason(@Nullable BattingState reason) {
            this.reason = reason;
            return this;
        }

        /**
         * @param dismissal The description of the dismissal, if the reason is {@link BattingState#DISMISSED}
         * @return This builder
         */
        public @Nonnull
        Builder withDismissal(@Nullable Dismissal dismissal) {
            this.dismissal = dismissal;
            return this;
        }

        /**
         * @param score The batter's score
         * @return This builder
         */
        public Builder withScore(@Nullable Score score) {
            this.score = score;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null) {
                throw new IllegalStateException("Batter innings cannot be complete if there is no current innings");
            }

            BallCompletedEvent last = match.balls().last();
            if (last != null && (dismissal != null || last.dismissal() != null) && reason == null) {
                if (dismissal == null) dismissal = last.dismissal();
                reason = DISMISSED;
                if (batter == null) batter = dismissal.batter();
                if (time() == null) withTime(last.time());
            }
            if (batter == null && dismissal != null) {
                batter = dismissal.batter();
            }
            if (batter == null) {
                BatterInnings bi = innings.currentStriker() != null ? innings.currentStriker() : innings.currentNonStriker();
                if (bi == null) {
                    throw new NullPointerException("Batter was null");
                }
                batter = bi.player();
            }
            if (score == null) {
                score = innings.batterInnings(batter).score();
            }
            return this;
        }

        public @Nonnull
        BatterInningsCompletedEvent build() {
            return new BatterInningsCompletedEvent(id(), time(), customData(), undoPoint(), this.batter, this.reason, this.dismissal, score);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(batter, builder.batter) &&
                reason == builder.reason &&
                Objects.equals(dismissal, builder.dismissal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), batter, reason, dismissal);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "batter=" + batter +
                ", reason=" + reason +
                ", dismissal=" + dismissal +
                "} " + super.toString();
        }
    }
}
