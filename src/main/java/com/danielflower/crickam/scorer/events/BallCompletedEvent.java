package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

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
public final class BallCompletedEvent extends BaseMatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final Score runsScored;
    private final boolean playersCrossed;
    private final Dismissal dismissal;
    private final Delivery delivery;
    private final Swing swing;
    private final Trajectory trajectoryAtImpact;
    private final Player fielder;
    private final int overNumber;
    private final int numberInOver;
    private final int numberInMatch;

    private BallCompletedEvent(UUID id, @Nullable Instant time, @Nullable Object customData, @Nullable UUID transactionID, Player bowler, Player striker, Player nonStriker, Score runsScored,
                               boolean playersCrossed, @Nullable Dismissal dismissal, @Nullable Delivery delivery, @Nullable Swing swing,
                               @Nullable Trajectory trajectoryAtImpact, @Nullable Player fielder, @Nonnegative int overNumber,
                               @Nonnegative int numberInOver, @Nonnegative int numberInMatch) {
        super(id, time, customData, transactionID);
        this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker, "striker");
        this.nonStriker = requireNonNull(nonStriker, "nonStriker");
        this.runsScored = requireNonNull(runsScored, "runsScored");
        this.overNumber = requireInRange("overNumber", overNumber, 0);
        this.numberInOver = requireInRange("numberInOver", numberInOver, 0);
        this.numberInMatch = requireInRange("numberInMatch", numberInMatch, 0);
        if (striker.samePlayer(nonStriker)) {
            throw new IllegalStateException("The striker and non striker were the same person: " + striker);
        }
        this.playersCrossed = playersCrossed;
        this.dismissal = dismissal;
        this.delivery = delivery;
        this.swing = swing;
        this.trajectoryAtImpact = trajectoryAtImpact;
        this.fielder = fielder;
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

    public @Nonnull Score runsScored() {
        return runsScored;
    }

    public boolean playersCrossed() {
        return playersCrossed;
    }

    public @Nullable Dismissal dismissal() {
        return dismissal;
    }

    public @Nullable Delivery delivery() {
        return delivery;
    }

    public @Nullable Swing swing() {
        return swing;
    }

    public @Nullable Trajectory trajectoryAtImpact() {
        return trajectoryAtImpact;
    }

    public @Nullable Player fielder() {
        return fielder;
    }

    public @Nonnegative int overNumber() {
        return overNumber;
    }

    public @Nonnegative int numberInOver() {
        return numberInOver;
    }

    public @Nonnegative int numberInMatch() {
        return numberInMatch;
    }

    public @Nonnull Score score() {
        return runsScored;
    }

    /**
     * @return True if this is a valid, or legal delivery (i.e. a ball that was not a wide or no-ball)
     */
    public boolean isValid() {
        return score().validDeliveries() > 0;
    }

    /**
     * Gets the number of the this ball as a string in the format <em>over.ball</em>, for example &quot;0.1&quot;
     *
     * @return The number of the last ball
     */
    public @Nonnull String overDotBallString() {
        return overNumber() + "." + numberInOver();
    }

    public @Nonnull String toString() {
        String out = dismissal != null ? " OUT " + dismissal.type().fullName() : "";
        return overDotBallString() + " " + bowler().scorecardName() + " to " + striker().scorecardName() + " "
            + runsScored().teamRuns() + " runs " + out;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        Builder builder = baseBuilder(new Builder())
            .withBowler(bowler())
            .withStriker(striker())
            .withNonStriker(nonStriker())
            .withRunsScored(runsScored())
            .withPlayersCrossed(playersCrossed())
            .withDelivery(delivery())
            .withSwing(swing())
            .withTrajectoryAtImpact(trajectoryAtImpact())
            .withFielder(fielder())
            .withNumberInMatch(numberInMatch())
            .withNumberInOver(numberInOver())
            .withOverNumber(overNumber());
        if (dismissal() != null) {
            builder.withDismissal(dismissal().type())
                .withDismissedBatter(dismissal().batter());
        }
        return builder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BallCompletedEvent that = (BallCompletedEvent) o;
        return playersCrossed == that.playersCrossed && overNumber == that.overNumber && numberInOver == that.numberInOver && numberInMatch == that.numberInMatch && Objects.equals(bowler, that.bowler) && Objects.equals(striker, that.striker) && Objects.equals(nonStriker, that.nonStriker) && Objects.equals(runsScored, that.runsScored) && Objects.equals(dismissal, that.dismissal) && Objects.equals(delivery, that.delivery) && Objects.equals(swing, that.swing) && Objects.equals(trajectoryAtImpact, that.trajectoryAtImpact) && Objects.equals(fielder, that.fielder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bowler, striker, nonStriker, runsScored, playersCrossed, dismissal, delivery, swing, trajectoryAtImpact, fielder, overNumber, numberInOver, numberInMatch);
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, BallCompletedEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Score runsScored;
        private Boolean playersCrossed;
        private Delivery delivery;
        private Swing swing;
        private Trajectory trajectoryAtImpact;
        private Player fielder;
        private DismissalType dismissalType;
        private Player dismissedBatter;
        private Integer overNumber;
        private Integer numberInOver;
        private Integer numberInMatch;

        public @Nullable Player bowler() {
            return bowler;
        }

        public @Nullable Player striker() {
            return striker;
        }

        public @Nullable Player nonStriker() {
            return nonStriker;
        }

        public @Nonnull Score runsScored() {
            return runsScored;
        }

        public @Nullable Boolean playersCrossed() {
            return playersCrossed;
        }

        public @Nullable Delivery delivery() {
            return delivery;
        }

        public @Nullable Swing swing() {
            return swing;
        }

        public @Nullable Trajectory trajectoryAtImpact() {
            return trajectoryAtImpact;
        }

        public @Nullable Player fielder() {
            return fielder;
        }

        public @Nullable DismissalType dismissal() {
            return dismissalType;
        }

        public @Nullable Player dismissedBatter() {
            return dismissedBatter;
        }

        public @Nullable Integer overNumber() {
            return overNumber;
        }

        public @Nullable Integer numberInOver() {
            return numberInOver;
        }

        public @Nullable Integer numberInMatch() {
            return numberInMatch;
        }

        /**
         * @param bowler The bowler bowling the ball. If null, then the bowler will be the bowler of the over.
         * @return This builder
         */
        public @Nonnull Builder withBowler(@Nullable Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param striker The facing batter. If null, then the batter will be based on the result of the previous ball.
         * @return This builder
         */
        public @Nonnull Builder withStriker(@Nullable Player striker) {
            this.striker = striker;
            return this;
        }

        /**
         * @param nonStriker The non-facing batter. If null, then the batter will be based on the result of the previous ball.
         * @return This builder
         */
        public @Nonnull Builder withNonStriker(@Nullable Player nonStriker) {
            this.nonStriker = nonStriker;
            return this;
        }

        public @Nonnull Builder withRunsScored(@Nonnull Score runsScored) {
            this.runsScored = runsScored;
            return this;
        }

        /**
         * Specifies whether or not the players crossed. If not set, then it is inferred from the
         * score (which is sometimes not possible to know, so may be a guess).
         *
         * @param playersCrossed true if they crossed; false if not; or null infer from the score
         * @return This builder
         */
        public @Nonnull Builder withPlayersCrossed(@Nullable Boolean playersCrossed) {
            this.playersCrossed = playersCrossed;
            return this;
        }

        /**
         * Indicates that this delivery got the batter out. If the score has not been set, then a wicket score with
         * no runs is used. If runs are scored then create a {@link Score} using {@link Score.Builder}'s {@link Score.Builder#withWickets(int)}
         * method and the number of runs set.
         *
         * @param type The type of dismissal
         * @return This builder
         */
        public @Nonnull Builder withDismissal(@Nullable DismissalType type) {
            this.dismissalType = type;
            if (runsScored == null && dismissalType != null) {
                runsScored = Score.WICKET;
            }
            return this;
        }

        /**
         * Indicates the player that was dismissed if {@link #withDismissal(DismissalType)} has been called. As this
         * defaults to the current striker, it can be left unset unless the non-striker was dismissed.
         *
         * @param dismissedBatter The batter who was dismissed (or null to indicate the current striker)
         * @return This builder
         */
        public @Nonnull Builder withDismissedBatter(@Nullable Player dismissedBatter) {
            this.dismissedBatter = dismissedBatter;
            return this;
        }

        public @Nonnull Builder withDelivery(@Nullable Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public @Nonnull Builder withSwing(@Nullable Swing swing) {
            this.swing = swing;
            return this;
        }

        public @Nonnull Builder withTrajectoryAtImpact(@Nullable Trajectory trajectoryAtImpact) {
            this.trajectoryAtImpact = trajectoryAtImpact;
            return this;
        }

        /**
         * Specifies the fielder
         *
         * @param fielder The main fielder who picked up the ball, or the fielder who effected a run out, or who caught the ball
         * @return This builder
         */
        public @Nonnull Builder withFielder(@Nullable Player fielder) {
            this.fielder = fielder;
            return this;
        }

        public Builder withOverNumber(@Nullable Integer overNumber) {
            this.overNumber = overNumber;
            return this;
        }

        public Builder withNumberInOver(@Nullable Integer numberInOver) {
            this.numberInOver = numberInOver;
            return this;
        }

        public Builder withNumberInMatch(@Nullable Integer numberInMatch) {
            this.numberInMatch = numberInMatch;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null)
                throw new IllegalStateException("A ball cannot be bowled when there is no current innings");
            BatterInnings strikerInnings = innings.currentStriker();
            if (strikerInnings == null)
                throw new IllegalStateException("Cannot bowl a ball without a batter on strike");
            BatterInnings nonStrikerInnings = innings.currentNonStriker();
            if (nonStrikerInnings == null)
                throw new IllegalStateException("Cannot bowl a ball without a batter at the non-striker's end");
            Over over = innings.currentOver();
            if (over == null)
                throw new IllegalStateException("There is no current over. Raise an OverStartingEvent event before the ball completion event");

            if (bowler == null) bowler = over.bowler();
            if (striker == null) striker = strikerInnings.player();
            if (nonStriker == null) nonStriker = nonStrikerInnings.player();

            playersCrossed = this.playersCrossed == null ? guessIfCrossed(runsScored) : this.playersCrossed;
            if (overNumber == null) overNumber = over.overNumber();
            if (numberInOver == null) numberInOver = over.validDeliveries() + 1;
            if (numberInMatch == null) numberInMatch = match.balls().size();
            return this;
        }

        @Nonnull
        public BallCompletedEvent build() {
            requireNonNull(runsScored, "A score must be set with the withRunsScored(Score) method");
            if (runsScored.wickets() > 0 && dismissalType == null) {
                throw new IllegalStateException("A wicket was taken but the method of dismissal was not set with the withDismissal(DismissalType, Player) method");
            } else if (runsScored.wickets() == 0 && dismissalType != null) {
                throw new IllegalStateException("A dismissal was specified, but the runsScored.wickets() was 0");
            }
            Dismissal dismissal = dismissalType == null ? null :
                new Dismissal.Builder().withType(dismissalType).withBatter(dismissedBatter != null ? dismissedBatter : striker).withBowler(dismissalType.creditedToBowler() ? bowler : null).withFielder(fielder).build();

            return new BallCompletedEvent(id(), time(), customData(), transactionID(), bowler, striker, nonStriker, runsScored, playersCrossed, dismissal,
                delivery, swing, trajectoryAtImpact, fielder, overNumber, numberInOver, numberInMatch);
        }

        private static boolean guessIfCrossed(Score score) {
            if (score.batterRuns() == 0 && score.wides() > 0) {
                return score.wides() % 2 == 0;
            }
            return (score.batterRuns() + score.legByes() + score.byes()) % 2 == 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(bowler, builder.bowler) && Objects.equals(striker, builder.striker) && Objects.equals(nonStriker, builder.nonStriker) && Objects.equals(runsScored, builder.runsScored) && Objects.equals(playersCrossed, builder.playersCrossed) && Objects.equals(delivery, builder.delivery) && Objects.equals(swing, builder.swing) && Objects.equals(trajectoryAtImpact, builder.trajectoryAtImpact) && Objects.equals(fielder, builder.fielder) && dismissalType == builder.dismissalType && Objects.equals(dismissedBatter, builder.dismissedBatter) && Objects.equals(overNumber, builder.overNumber) && Objects.equals(numberInOver, builder.numberInOver) && Objects.equals(numberInMatch, builder.numberInMatch);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), bowler, striker, nonStriker, runsScored, playersCrossed, delivery, swing, trajectoryAtImpact, fielder, dismissalType, dismissedBatter, overNumber, numberInOver, numberInMatch);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "bowler=" + bowler +
                ", striker=" + striker +
                ", nonStriker=" + nonStriker +
                ", runsScored=" + runsScored +
                ", playersCrossed=" + playersCrossed +
                ", delivery=" + delivery +
                ", swing=" + swing +
                ", trajectoryAtImpact=" + trajectoryAtImpact +
                ", fielder=" + fielder +
                ", dismissalType=" + dismissalType +
                ", dismissedBatter=" + dismissedBatter +
                ", overNumber=" + overNumber +
                ", numberInOver=" + numberInOver +
                ", numberInMatch=" + numberInMatch +
                '}';
        }
    }

}
