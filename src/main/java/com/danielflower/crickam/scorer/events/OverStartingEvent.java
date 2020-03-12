package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.BatterInnings;
import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

public final class OverStartingEvent extends BaseMatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final int ballsInOver;
    private final int overNumber;
    private final int inningsNumber;

    private OverStartingEvent(String id, Instant time, String generatedBy, Player bowler, Player striker, Player nonStriker, int ballsInOver, int overNumber, int inningsNumber) {
        super(id, time, generatedBy);
        this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker, "striker");
        this.nonStriker = requireNonNull(nonStriker, "nonStriker");
        this.ballsInOver = requireInRange("ballsInOver", ballsInOver, 1);
        this.overNumber = overNumber;
        this.inningsNumber = inningsNumber;
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

    public int ballsInOver() {
        return ballsInOver;
    }

    /**
     * @return The zero-based index of this over in the current innings (i.e. the first over in an innings is 0)
     */
    public int overNumber() {
        return overNumber;
    }

    public int inningsNumber() {
        return inningsNumber;
    }

    @Override
    public Builder newBuilder() {
        return new Builder()
            .withBowler(bowler)
            .withStriker(striker)
            .withNonStriker(nonStriker)
            .withBallsInOver(ballsInOver)
            .withID(id())
            .withTime(time().orElse(null))
            .withGeneratedBy(generatedBy().orElse(null))
            ;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverStartingEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private int ballsInOver = 6;

        public Player bowler() {
            return bowler;
        }

        public Player striker() {
            return striker;
        }

        public Player nonStriker() {
            return nonStriker;
        }

        public int ballsInOver() {
            return ballsInOver;
        }

        /**
         * @param bowler Specifies the bowler of this over. This must be set.
         * @return This builder
         */
        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param ballsInOver The default number of balls per over. Defaults to 6 if unset.
         * @return This builder
         */
        public Builder withBallsInOver(int ballsInOver) {
            this.ballsInOver = ballsInOver;
            return this;
        }

        /**
         * @param striker The player who is on strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public Builder withStriker(Player striker) {
            this.striker = striker;
            return this;
        }

        /**
         * @param nonStriker The player who is off strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public Builder withNonStriker(Player nonStriker) {
            this.nonStriker = nonStriker;
            return this;
        }

        public OverStartingEvent build(Match match) {
            Innings innings = match.currentInnings().orElseThrow(() -> new IllegalStateException("An over cannot start when there is no current innings"));
            if (innings.currentStriker().isEmpty() && innings.currentNonStriker().isEmpty()) {
                throw new IllegalStateException("An over can only start with two batters at the crease. Did you miss sending a " + BatterInningsStartingEvent.class.getSimpleName() + " event?");
            }
            boolean isFirst = innings.overs().last().isEmpty();
            Player strikerPlayer = Objects.requireNonNullElse(striker, playerOrNull(isFirst ? innings.currentStriker() : innings.currentNonStriker()));
            Player nonStrikerPlayer = Objects.requireNonNullElse(nonStriker, playerOrNull(isFirst ? innings.currentNonStriker() : innings.currentStriker()));
            int numberInInnings = innings.overs().size();
            return new OverStartingEvent(id(), time(), generatedBy(), bowler, strikerPlayer, nonStrikerPlayer, ballsInOver, numberInInnings, innings.inningsNumber());
        }

        private static Player playerOrNull(Optional<BatterInnings> batterInnings) {
            return batterInnings.isPresent() ? batterInnings.get().player() : null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return ballsInOver == builder.ballsInOver &&
                Objects.equals(bowler, builder.bowler) &&
                Objects.equals(striker, builder.striker) &&
                Objects.equals(nonStriker, builder.nonStriker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), bowler, striker, nonStriker, ballsInOver);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "bowler=" + bowler +
                ", striker=" + striker +
                ", nonStriker=" + nonStriker +
                ", ballsInOver=" + ballsInOver +
                "} " + super.toString();
        }
    }
}
