package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class BallCompleteEvent implements MatchEvent {

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

    private BallCompleteEvent(@Nullable Player bowler, @Nullable Player striker, @Nullable Player nonStriker, Score runsScored, boolean playersCrossed, DismissalType dismissal, @Nullable Delivery delivery, @Nullable Swing swing, @Nullable Trajectory trajectoryAtImpact, @Nullable Player fielder, Instant dateCompleted) {
        if (striker != null && striker.equals(nonStriker)) {
            throw new IllegalStateException("The striker and non striker were the same person: " + striker);
        }
        this.bowler = bowler;
        this.striker = striker;
        this.nonStriker = nonStriker;
        this.runsScored = requireNonNull(runsScored);
        this.playersCrossed = playersCrossed;
        this.dismissal = dismissal;
        this.delivery = delivery;
        this.swing = swing;
        this.trajectoryAtImpact = trajectoryAtImpact;
        this.fielder = fielder;
        this.dateCompleted = requireNonNull(dateCompleted);
    }

    public Player bowler() {
        return bowler;
    }

    public Player striker() {
        return striker;
    }

    public Player nonStriker() {
        return nonStriker;
    }

    public Score runsScored() {
        return runsScored;
    }

    public boolean playersCrossed() {
        return playersCrossed;
    }

    public DismissalType dismissal() {
        return dismissal;
    }

    public Delivery delivery() {
        return delivery;
    }

    public Swing swing() {
        return swing;
    }

    public Trajectory trajectoryAtImpact() {
        return trajectoryAtImpact;
    }

    public Player fielder() {
        return fielder;
    }

    public Instant dateCompleted() {
        return dateCompleted;
    }

    public static class Builder {

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
         * Indicates that this delivery got the batsman out. If the score has not been set, then a wicket score with
         * no runs is used. If runs are scored then create a {@link Score} using {@link ScoreBuilder}'s {@link ScoreBuilder#setWickets(int)}
         * method and the number of runs set.
         * @param type The type of dismissal
         * @return This builder
         */
        public Builder withDismissal(DismissalType type) {
            this.dismissalType = type;
            if (runsScored == null) {
                runsScored = ScoreBuilder.WICKET;
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

        public Builder withFielder(Player fielder) {
            this.fielder = fielder;
            return this;
        }

        public Builder withDateCompleted(Instant dateCompleted) {
            this.dateCompleted = dateCompleted;
            return this;
        }

        public BallCompleteEvent build() {
            Instant dc = Objects.requireNonNullElse(this.dateCompleted, Instant.now());
            return new BallCompleteEvent(bowler, striker, nonStriker, runsScored, playersCrossed, dismissalType, delivery, swing, trajectoryAtImpact, fielder, dc);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
