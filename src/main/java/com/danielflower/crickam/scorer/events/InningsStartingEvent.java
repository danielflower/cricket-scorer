package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.LineUp;
import com.danielflower.crickam.scorer.MatchEvent;
import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public final class InningsStartingEvent implements MatchEvent {

    private final LineUp battingTeam;
    private final LineUp bowlingTeam;
    private final Instant startTime;
    private final ImmutableList<Player> openers;

    private InningsStartingEvent(LineUp battingTeam, LineUp bowlingTeam, Instant startTime, ImmutableList<Player> openers) {
        this.battingTeam = requireNonNull(battingTeam);
        this.bowlingTeam = requireNonNull(bowlingTeam);
        this.startTime = requireNonNull(startTime);
        this.openers = requireNonNull(openers);
        if (openers.size() != 2) throw new IllegalArgumentException("There must be 2 openers");
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

    public ImmutableList<Player> openers() {
        return openers;
    }

    public static Builder inningsStarting() {
        return new Builder();
    }

    public static final class Builder implements MatchEventBuilder<InningsStartingEvent> {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private Instant startTime = Instant.now();
        private ImmutableList<Player> openers;

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

        public Builder withOpeners(ImmutableList<Player> openers) {
            this.openers = openers;
            return this;
        }

        public Builder withOpeners(Player first, Player second) {
            this.openers = ImmutableList.of(first, second);
            return this;
        }

        public InningsStartingEvent build() {
            return new InningsStartingEvent(battingTeam, bowlingTeam, startTime, openers);
        }
    }
}
