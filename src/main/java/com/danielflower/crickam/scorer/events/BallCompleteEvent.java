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
    private final Dismissal dismissal;
    private final Delivery delivery;
    private final Swing swing;
    private final Trajectory trajectoryAtImpact;
    private final Player fielder;
    private final Instant dateCompleted;

    private BallCompleteEvent(Player bowler, Player striker, Player nonStriker, Score runsScored, boolean playersCrossed, @Nullable Dismissal dismissal, @Nullable Delivery delivery, @Nullable Swing swing, @Nullable Trajectory trajectoryAtImpact, @Nullable Player fielder, Instant dateCompleted) {
        this.bowler = requireNonNull(bowler);
        this.striker = requireNonNull(striker);
        this.nonStriker = requireNonNull(nonStriker);
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

    public Dismissal dismissal() {
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
        private Dismissal dismissal;
        private Delivery delivery;
        private Swing swing;
        private Trajectory trajectoryAtImpact;
        private Player fielder;
        private Instant dateCompleted;

        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        public Builder withStriker(Player striker) {
            this.striker = striker;
            return this;
        }

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

        public Builder withDismissal(Dismissal dismissal) {
            this.dismissal = dismissal;
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
            return new BallCompleteEvent(bowler, striker, nonStriker, runsScored, playersCrossed, dismissal, delivery, swing, trajectoryAtImpact, fielder, dc);
        }
    }
}
