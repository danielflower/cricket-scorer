package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public final class OverStartingEvent implements MatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final Instant startTime;
    private final int ballsInOver;

    private OverStartingEvent(Player bowler, Player striker, Player nonStriker, Instant startTime, int ballsInOver) {
        this.bowler = requireNonNull(bowler);
        this.striker = striker;
        this.nonStriker = nonStriker;
        this.startTime = requireNonNull(startTime);
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

    public Instant startTime() {
        return startTime;
    }

    public static final class Builder implements MatchEventBuilder<OverStartingEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Instant startTime = Instant.now();
        private int ballsInOver = 6;

        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        public Builder withBallsInOver(int ballsInOver) {
            this.ballsInOver = ballsInOver;
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

        public OverStartingEvent build() {
            return new OverStartingEvent(bowler, striker, nonStriker, startTime, ballsInOver);
        }
    }
}
