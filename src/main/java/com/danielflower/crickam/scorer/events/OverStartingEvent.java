package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class OverStartingEvent implements MatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final Instant time;
    private final int ballsInOver;

    private OverStartingEvent(Player bowler, Player striker, Player nonStriker, Instant time, int ballsInOver) {
        this.bowler = requireNonNull(bowler, "The bowler of an over must be set");
        this.striker = striker;
        this.nonStriker = nonStriker;
        this.time = time;
        this.ballsInOver = ballsInOver;
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

    public static Builder overStarting() {
        return new Builder();
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public static final class Builder implements MatchEventBuilder<OverStartingEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Instant time;
        private int ballsInOver = 6;

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

        /**
         * @param time The time that the over is starting (this may be when the bowler gets the ball, before the first ball is started).
         * @return This builder
         */
        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        public OverStartingEvent build() {
            return new OverStartingEvent(bowler, striker, nonStriker, time, ballsInOver);
        }
    }
}
