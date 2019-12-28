package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class BallCompletedEvent implements MatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final Score runsScored;
    private final boolean playersCrossed;
    private final DismissalType dismissal;
    private final Delivery delivery;
    private final Swing swing;
    private final Trajectory trajectoryAtImpact;
    private final Player fielder;
    private final Instant time;
    private final Player dismissedBatter;

    private BallCompletedEvent(@Nullable Player bowler, @Nullable Player striker, @Nullable Player nonStriker, Score runsScored, boolean playersCrossed, DismissalType dismissal, @Nullable Player dismissedBatter, @Nullable Delivery delivery, @Nullable Swing swing, @Nullable Trajectory trajectoryAtImpact, @Nullable Player fielder, Instant time) {
        if (striker != null && striker.equals(nonStriker)) {
            throw new IllegalStateException("The striker and non striker were the same person: " + striker);
        }
        this.bowler = bowler;
        this.striker = striker;
        this.nonStriker = nonStriker;
        this.runsScored = requireNonNull(runsScored);
        this.playersCrossed = playersCrossed;
        this.dismissal = dismissal;
        this.dismissedBatter = dismissedBatter;
        this.delivery = delivery;
        this.swing = swing;
        this.trajectoryAtImpact = trajectoryAtImpact;
        this.fielder = fielder;
        this.time = time;
    }

    public Optional<Player> bowler() {
        return Optional.ofNullable(bowler);
    }

    public Optional<Player> striker() {
        return Optional.ofNullable(striker);
    }

    public Optional<Player> nonStriker() {
        return Optional.ofNullable(nonStriker);
    }

    public Score runsScored() {
        return runsScored;
    }

    public boolean playersCrossed() {
        return playersCrossed;
    }

    public Optional<DismissalType> dismissal() {
        return Optional.ofNullable(dismissal);
    }

    public Optional<Player> dismissedBatter() {
        return Optional.ofNullable(dismissedBatter);
    }

    public Optional<Delivery> delivery() {
        return Optional.ofNullable(delivery);
    }

    public Optional<Swing> swing() {
        return Optional.ofNullable(swing);
    }

    public Optional<Trajectory> trajectoryAtImpact() {
        return Optional.ofNullable(trajectoryAtImpact);
    }

    public Optional<Player> fielder() {
        return Optional.ofNullable(fielder);
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    /**
     * @return A builder with no values set.
     */
    public static Builder ballCompleted() {
        return new Builder();
    }

    /**
     * Creates a ball based on a text score such as &quot;1&quot;, &quot;W&quot etc
     * <p>For the case where a wicket was taken, call {@link Builder#withDismissal(DismissalType)}
     * on the returned builder.</p>
     * @param scoreText a string allowed by {@link Score#parse(String)}
     * @return A BallCompletedEvent builder
     * @throws IllegalArgumentException if {@code scoreText} was not a valid score
     */
    public static Builder ballCompleted(String scoreText) {
        Score score = Score.parse(scoreText).orElseThrow(() -> new IllegalArgumentException("Unknown score: " + scoreText));
        return ballCompleted()
            .withRunsScored(score);
    }

    public final static class Builder implements MatchEventBuilder<BallCompletedEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Score runsScored;
        private Boolean playersCrossed;
        private Delivery delivery;
        private Swing swing;
        private Trajectory trajectoryAtImpact;
        private Player fielder;
        private Instant dateCompleted;
        private DismissalType dismissalType;
        private Player dismissedBatter;

        /**
         * @param bowler The bowler bowling the ball. If null, then the bowler will be the bowler of the over.
         * @return This builder
         */
        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param striker The facing batter. If null, then the batter will be based on the result of the previous ball.
         * @return This builder
         */
        public Builder withStriker(Player striker) {
            this.striker = striker;
            return this;
        }

        /**
         * @param nonStriker The non-facing batter. If null, then the batter will be based on the result of the previous ball.
         * @return This builder
         */
        public Builder withNonStriker(Player nonStriker) {
            this.nonStriker = nonStriker;
            return this;
        }

        public Builder withRunsScored(Score runsScored) {
            this.runsScored = runsScored;
            return this;
        }

        /**
         * Specifies whether or not the players crossed. If not set, then it is inferred from the
         * score (which is sometimes not possible to know, so may be a guess).
         * @param playersCrossed true if they crossed; false if not; or null infer from the score
         * @return This builder
         */
        public Builder withPlayersCrossed(Boolean playersCrossed) {
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
        public Builder withDismissal(@NotNull DismissalType type) {
            this.dismissalType = requireNonNull(type);
            if (runsScored == null) {
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
        public Builder withDismissedBatter(Player dismissedBatter) {
            this.dismissedBatter = dismissedBatter;
            return this;
        }

        public Builder withDelivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public Builder withSwing(Swing swing) {
            this.swing = swing;
            return this;
        }

        public Builder withTrajectoryAtImpact(Trajectory trajectoryAtImpact) {
            this.trajectoryAtImpact = trajectoryAtImpact;
            return this;
        }

        /**
         * Specifies the fielder
         * @param fielder The main fielder who picked up the ball, or the fielder who effected a run out, or who caught the ball
         * @return This builder
         */
        public Builder withFielder(Player fielder) {
            this.fielder = fielder;
            return this;
        }

        public Builder withDateCompleted(Instant dateCompleted) {
            this.dateCompleted = dateCompleted;
            return this;
        }

        public BallCompletedEvent build() {
            requireNonNull(runsScored, "A score must be set with the withRunsScored(Score) method");
            if (runsScored.wickets() > 0 && dismissalType == null) {
                throw new IllegalStateException("A wicket was taken but the method of dismissal was not set with the withDismissal(DismissalType, Player) method");
            }
            boolean playersCrossed = this.playersCrossed == null ? guessIfCrossed(runsScored) : this.playersCrossed;
            return new BallCompletedEvent(bowler, striker, nonStriker, runsScored, playersCrossed, dismissalType, dismissedBatter, delivery, swing, trajectoryAtImpact, fielder, dateCompleted);
        }

        private boolean guessIfCrossed(Score score) {
            if (score.batterRuns() == 0 && score.wides() > 0) {
                return score.wides() % 2 == 0;
            }

            return (score.batterRuns() + score.legByes() + score.byes()) % 2 == 1;
        }
    }

}
