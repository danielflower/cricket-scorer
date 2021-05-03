package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.BatterInnings;
import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

@Immutable
public final class OverStartingEvent extends BaseMatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final int ballsInOver;
    private final int overNumber;
    private final int inningsNumber;

    private OverStartingEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint, Player bowler, Player striker, Player nonStriker, @Nonnegative int ballsInOver, @Nonnegative int overNumber, @Nonnegative int inningsNumber) {
        super(id, time, customData, undoPoint);
        this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker, "striker");
        this.nonStriker = requireNonNull(nonStriker, "nonStriker");
        this.ballsInOver = requireInRange("ballsInOver", ballsInOver, 1);
        this.overNumber = overNumber;
        this.inningsNumber = inningsNumber;
    }

    public @Nonnull Player bowler() {
        return bowler;
    }

    public @Nonnull Player striker() {
        return striker;
    }

    public @Nonnull Player nonStriker() {
        return nonStriker;
    }

    public @Nonnegative int ballsInOver() {
        return ballsInOver;
    }

    /**
     * @return The zero-based index of this over in the current innings (i.e. the first over in an innings is 0)
     */
    public @Nonnegative int overNumber() {
        return overNumber;
    }

    public @Nonnegative int inningsNumber() {
        return inningsNumber;
    }

    @Override
    public @Nonnull
    Builder newBuilder() {
        return baseBuilder(new Builder())
            .withBowler(bowler())
            .withStriker(striker())
            .withNonStriker(nonStriker())
            .withBallsInOver(ballsInOver())
            .withNumberInInnings(overNumber())
            .withInningsNumber(inningsNumber())
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OverStartingEvent that = (OverStartingEvent) o;
        return ballsInOver == that.ballsInOver && overNumber == that.overNumber && inningsNumber == that.inningsNumber && Objects.equals(bowler, that.bowler) && Objects.equals(striker, that.striker) && Objects.equals(nonStriker, that.nonStriker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bowler, striker, nonStriker, ballsInOver, overNumber, inningsNumber);
    }

    @Override
    public String toString() {
        return "OverStartingEvent{" +
            "bowler=" + bowler +
            ", striker=" + striker +
            ", nonStriker=" + nonStriker +
            ", ballsInOver=" + ballsInOver +
            ", overNumber=" + overNumber +
            ", inningsNumber=" + inningsNumber +
            '}';
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverStartingEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Integer ballsInOver;
        private Integer numberInInnings;
        private Integer inningsNumber;

        public @Nullable Player bowler() {
            return bowler;
        }

        public @Nullable Player striker() {
            return striker;
        }

        public @Nullable Player nonStriker() {
            return nonStriker;
        }

        public Integer ballsInOver() {
            return ballsInOver;
        }

        public @Nullable Integer numberInInnings() {
            return numberInInnings;
        }

        public @Nullable Integer inningsNumber() {
            return inningsNumber;
        }

        /**
         * @param bowler Specifies the bowler of this over. This must be set.
         * @return This builder
         */
        public @Nonnull Builder withBowler(@Nullable Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param ballsInOver The default number of balls per over. Defaults to 6 if unset.
         * @return This builder
         */
        public @Nonnull Builder withBallsInOver(@Nullable @Nonnegative Integer ballsInOver) {
            this.ballsInOver = ballsInOver;
            return this;
        }

        /**
         * @param inningsNumber The current innings number.
         * @return This builder
         */
        public @Nonnull Builder withInningsNumber(@Nonnegative @Nullable Integer inningsNumber) {
            this.inningsNumber = inningsNumber;
            return this;
        }

        /**
         * @param numberInInnings The number in the innings of this ball.
         * @return This builder
         */
        public @Nonnull
        Builder withNumberInInnings(@Nonnegative @Nullable Integer numberInInnings) {
            this.numberInInnings = numberInInnings;
            return this;
        }

        /**
         * @param striker The player who is on strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public @Nonnull
        Builder withStriker(@Nullable Player striker) {
            this.striker = striker;
            return this;
        }

        /**
         * @param nonStriker The player who is off strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public @Nonnull
        Builder withNonStriker(@Nullable Player nonStriker) {
            this.nonStriker = nonStriker;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null)
                throw new IllegalStateException("An over cannot start when there is no current innings");
            if (innings.currentStriker() == null || innings.currentNonStriker() == null) {
                throw new IllegalStateException("An over can only start with two batters at the crease. Did you miss sending a " + BatterInningsStartingEvent.class.getSimpleName() + " event?");
            }
            if (ballsInOver == null) ballsInOver = 6;
            boolean isFirst = innings.overs().last() == null;
            if (striker == null) striker = playerOrNull(isFirst ? innings.currentStriker() : innings.currentNonStriker());
            if (nonStriker == null) nonStriker = playerOrNull(isFirst ? innings.currentNonStriker() : innings.currentStriker());
            if (inningsNumber == null) inningsNumber = innings.inningsNumber();
            if (numberInInnings == null) numberInInnings = innings.overs().size();
            return this;
        }

        @Nonnull
        public OverStartingEvent build() {
            return new OverStartingEvent(id(), time(), customData(), undoPoint(), bowler, striker, nonStriker, ballsInOver, numberInInnings, inningsNumber);
        }

        private static @Nullable
        Player playerOrNull(@Nullable BatterInnings batterInnings) {
            return batterInnings != null ? batterInnings.player() : null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return ballsInOver == builder.ballsInOver && Objects.equals(bowler, builder.bowler) && Objects.equals(striker, builder.striker) && Objects.equals(nonStriker, builder.nonStriker) && Objects.equals(numberInInnings, builder.numberInInnings) && Objects.equals(inningsNumber, builder.inningsNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), bowler, striker, nonStriker, ballsInOver, numberInInnings, inningsNumber);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "bowler=" + bowler +
                ", striker=" + striker +
                ", nonStriker=" + nonStriker +
                ", ballsInOver=" + ballsInOver +
                ", numberInInnings=" + numberInInnings +
                ", inningsNumber=" + inningsNumber +
                '}';
        }
    }
}
