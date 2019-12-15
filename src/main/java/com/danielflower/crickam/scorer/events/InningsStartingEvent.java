package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.LineUp;
import com.danielflower.crickam.scorer.MatchEvent;

import java.time.Instant;

public class InningsStartingEvent implements MatchEvent {

    private final LineUp battingTeam;
    private final LineUp bowlingTeam;
    private final Instant startTime;

    public InningsStartingEvent(LineUp battingTeam, LineUp bowlingTeam, Instant startTime) {
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.startTime = startTime;
    }

    public LineUp battingTeam() {
        return battingTeam;
    }

    public LineUp bowlingTeam() {
        return bowlingTeam;
    }

    public Instant startTime() {
        return startTime;
    }

    public static class Builder {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private Instant startTime;

        public Builder withBattingTeam(LineUp battingTeam) {
            this.battingTeam = battingTeam;
            return this;
        }

        public Builder withBowlingTeam(LineUp bowlingTeam) {
            this.bowlingTeam = bowlingTeam;
            return this;
        }

        public Builder withStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public InningsStartingEvent build() {
            return new InningsStartingEvent(battingTeam, bowlingTeam, startTime);
        }
    }
}
