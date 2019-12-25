package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
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
    private final Instant dateCompleted;
    private final Player dismissedBatter;

    private BallCompletedEvent(@Nullable Player bowler, @Nullable Player striker, @Nullable Player nonStriker, Score runsScored, boolean playersCrossed, DismissalType dismissal, @Nullable Player dismissedBatter, @Nullable Delivery delivery, @Nullable Swing swing, @Nullable Trajectory trajectoryAtImpact, @Nullable Player fielder, Instant dateCompleted) {
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
        this.dateCompleted = requireNonNull(dateCompleted);
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

    public Instant dateCompleted() {
        return dateCompleted;
    }

    public final static class Builder implements MatchEventBuilder<BallCompletedEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Score runsScored;
        private boolean playersCrossed;
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

        public Builder withPlayersCrossed(boolean playersCrossed) {
            this.playersCrossed = playersCrossed;
            return this;
        }

        /**
         * Indicates that this delivery got the batter out. If the score has not been set, then a wicket score with
         * no runs is used. If runs are scored then create a {@link Score} using {@link Score.Builder}'s {@link Score.Builder#withWickets(int)}
         * method and the number of runs set.
         *
         * @param type The type of dismissal
         * @param dismissedBatter The batter who was dismissed (or null to indicate the current striker)
         * @return This builder
         */
        public Builder withDismissal(@NotNull DismissalType type, @Nullable Player dismissedBatter) {
            this.dismissalType = requireNonNull(type);
            this.dismissedBatter = dismissedBatter;
            if (runsScored == null) {
                runsScored = Score.WICKET;
            }
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
            Instant dc = Objects.requireNonNullElse(this.dateCompleted, Instant.now());
            return new BallCompletedEvent(bowler, striker, nonStriker, runsScored, playersCrossed, dismissalType, dismissedBatter, delivery, swing, trajectoryAtImpact, fielder, dc);
        }
    }

    public static Builder ballCompleted() {
        return new Builder();
    }
}
