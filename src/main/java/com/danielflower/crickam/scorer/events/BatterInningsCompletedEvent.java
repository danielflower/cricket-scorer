package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.BattingState;
import com.danielflower.crickam.scorer.Dismissal;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.danielflower.crickam.scorer.BattingState.DISMISSED;
import static java.util.Objects.requireNonNull;

/**
 * Indicates that a batter's innings has ended due to dismissal, retirement, or that the team innings ended.
 */
public final class BatterInningsCompletedEvent extends BaseMatchEvent {

    private final Player batter;
    private final BattingState reason;
    private final Dismissal dismissal;

    private BatterInningsCompletedEvent(String id, Instant time, String generatedBy, Player batter, BattingState reason, Dismissal dismissal) {
        super(id, time, generatedBy);
        this.batter = requireNonNull(batter, "batter");
        this.reason = requireNonNull(reason, "reason");
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

    public Player batter() {
        return batter;
    }

    /**
     * @return The reason the innings is ended
     */
    public BattingState reason() {
        return reason;
    }

    /**
     * @return The dismissal information, or empty if {@link #reason()} is not {@link BattingState#DISMISSED}
     */
    public Optional<Dismissal> dismissal() {
        return Optional.ofNullable(dismissal);
    }

    @Override
    public Builder newBuilder() {
        return new Builder()
            .withBatter(batter)
            .withReason(reason)
            .withDismissal(dismissal)
            .withID(id())
            .withTime(time().orElse(null))
            .withGeneratedBy(generatedBy().orElse(null))
            ;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, BatterInningsCompletedEvent> {
        private Player batter;
        private BattingState reason;
        private Dismissal dismissal;

        public Player batter() {
            return batter;
        }

        public BattingState reason() {
            return reason;
        }

        public Dismissal dismissal() {
            return dismissal;
        }

        /**
         * @param batter the batter who's innings has ended. Can be left unset if dismissal is set with a batter set.
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
        public Builder withReason(BattingState reason) {
            this.reason = reason;
            return this;
        }

        /**
         * @param dismissal The description of the dismissal, if the reason is {@link BattingState#DISMISSED}
         * @return This builder
         */
        public Builder withDismissal(Dismissal dismissal) {
            this.dismissal = dismissal;
            return this;
        }

        public BatterInningsCompletedEvent build(Match match) {
            Player batter = this.batter;
            if (batter == null && dismissal != null && dismissal.batter() != null) {
                batter = dismissal.batter();
            }
            return new BatterInningsCompletedEvent(id(), time(), generatedBy(), batter, reason, dismissal);
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
