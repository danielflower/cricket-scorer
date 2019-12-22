package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;
import com.danielflower.crickam.scorer.Player;

import java.util.Objects;

public class OverStartingEvent implements MatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final int ballsInOver;

    private OverStartingEvent(Player bowler, Player striker, Player nonStriker, int ballsInOver) {
        this.bowler = Objects.requireNonNull(bowler);
        this.striker = striker;
        this.nonStriker = nonStriker;
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

    public static class Builder {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
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
            return new OverStartingEvent(bowler, striker, nonStriker, ballsInOver);
        }
    }
}
