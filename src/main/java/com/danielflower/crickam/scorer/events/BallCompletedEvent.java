package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public final class BallCompletedEvent implements MatchEvent {

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
    private final Instant time;
    private final int overNumber;
    private final int numberInOver;
    private final int numberInMatch;
    private final ImmutableList<MatchEventBuilder<?>> generatedEvents;

    private BallCompletedEvent(Player bowler, Player striker, Player nonStriker, Score runsScored,
                               boolean playersCrossed, Dismissal dismissal, Delivery delivery, Swing swing,
                               Trajectory trajectoryAtImpact, Player fielder, Instant time, int overNumber,
                               int numberInOver, int numberInMatch, ImmutableList<MatchEventBuilder<?>> generatedEvents) {
        this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker, "striker");
        this.nonStriker = requireNonNull(nonStriker, "nonStriker");
        this.runsScored = requireNonNull(runsScored, "runsScored");
        this.overNumber = requireInRange("overNumber", overNumber, 0);
        this.numberInOver = requireInRange("numberInOver", numberInOver, 0);
        this.numberInMatch = requireInRange("numberInMatch", numberInMatch, 0);
        this.generatedEvents = requireNonNull(generatedEvents, "generatedEvents");
        if (striker.equals(nonStriker)) {
            throw new IllegalStateException("The striker and non striker were the same person: " + striker);
        }
        this.playersCrossed = playersCrossed;
        this.dismissal = dismissal;
        this.delivery = delivery;
        this.swing = swing;
        this.trajectoryAtImpact = trajectoryAtImpact;
        this.fielder = fielder;
        this.time = time;
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

    public Optional<Dismissal> dismissal() {
        return Optional.ofNullable(dismissal);
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

    public int overNumber() {
        return overNumber;
    }

    public int numberInOver() {
        return numberInOver;
    }

    public int numberInMatch() {
        return numberInMatch;
    }

    public Score score() {
        return runsScored;
    }

    /**
     * @return True if this is a valid, or legal delivery (i.e. a ball that was not a wide or no-ball)
     */
    public boolean isValid() {
        return score().validDeliveries() > 0;
    }

    @Override
    public ImmutableList<MatchEventBuilder<?>> generatedEvents() {
        return generatedEvents;
    }

    /**
     * Gets the number of the this ball as a string in the format <em>over.ball</em>, for example &quot;0.1&quot;
     *
     * @return The number of the last ball
     */
    public String overDotBallString() {
        return overNumber() + "." + numberInOver();
    }

    public String toString() {
        String out = dismissal != null ? " OUT " + dismissal.type().fullName() : "";
        return overDotBallString() + " " + bowler().familyName() + " to " + striker().familyName() + " "
            + runsScored().teamRuns() + " runs " + out;
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
        public Builder withDismissal(DismissalType type) {
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

        public BallCompletedEvent build(Match match) {
            requireNonNull(runsScored, "A score must be set with the withRunsScored(Score) method");
            if (runsScored.wickets() > 0 && dismissalType == null) {
                throw new IllegalStateException("A wicket was taken but the method of dismissal was not set with the withDismissal(DismissalType, Player) method");
            } else if (runsScored.wickets() == 0 && dismissalType != null) {
                throw new IllegalStateException("A dismissal was specified, but the runsScored.wickets() was 0");
            }
            Innings innings = match.currentInnings().orElseThrow(() -> new IllegalStateException("A ball cannot be bowled when there is no current innings"));
            BatterInnings strikerInnings = innings.currentStriker().orElseThrow(() -> new IllegalStateException("Cannot bowl a ball without a batter on strike"));
            BatterInnings nonStrikerInnings = innings.currentNonStriker().orElseThrow(() -> new IllegalStateException("Cannot bowl a ball without a batter at the non-striker's end"));

            Over over = innings.currentOver().orElseThrow(() -> new IllegalStateException("There is no current over. Raise an OverStartingEvent event before the ball completion event"));
            Player bowler = requireNonNullElseGet(this.bowler, over::bowler);
            Player striker = requireNonNullElseGet(this.striker, strikerInnings::player);
            Player nonStriker = requireNonNullElseGet(this.nonStriker, nonStrikerInnings::player);

            Dismissal dismissal = dismissalType == null ? null :
                new Dismissal.Builder().withType(dismissalType).withBatter(dismissedBatter != null ? dismissedBatter : striker).withBowler(dismissalType.creditedToBowler() ? bowler : null).withFielder(fielder).build();

            boolean playersCrossed = this.playersCrossed == null ? guessIfCrossed(runsScored) : this.playersCrossed;
            int overNumber = over.overNumber();
            int numberInOver = over.validDeliveries() + 1;
            int numberInMatch = match.balls().size();
            ImmutableList<MatchEventBuilder<?>> generatedEvents = ImmutableList.emptyList();
            if (dismissal != null) {
                generatedEvents = generatedEvents.add(MatchEvents.batterInningsCompleted(BattingState.DISMISSED)
                    .withTime(dateCompleted)
                    .withBatter(dismissal.batter())
                    .withDismissal(dismissal)
                );
            }
            return new BallCompletedEvent(bowler, striker, nonStriker, runsScored, playersCrossed, dismissal,
                delivery, swing, trajectoryAtImpact, fielder, dateCompleted, overNumber, numberInOver, numberInMatch, generatedEvents);
        }

        private static boolean guessIfCrossed(Score score) {
            if (score.batterRuns() == 0 && score.wides() > 0) {
                return score.wides() % 2 == 0;
            }
            return (score.batterRuns() + score.legByes() + score.byes()) % 2 == 1;
        }
    }

}
