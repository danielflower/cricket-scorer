package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.LineUp;
import com.danielflower.crickam.scorer.Player;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.toOptional;
import static java.util.Objects.requireNonNull;

public final class InningsStartingEvent implements MatchEvent {

    private final LineUp battingTeam;
    private final LineUp bowlingTeam;
    private final Instant time;
    private final ImmutableList<Player> openers;
    private final Integer numberOfBalls;

    private InningsStartingEvent(LineUp battingTeam, LineUp bowlingTeam, Instant time, ImmutableList<Player> openers, Integer numberOfBalls) {
        this.battingTeam = requireNonNull(battingTeam);
        this.bowlingTeam = requireNonNull(bowlingTeam);
        this.time = time;
        this.openers = requireNonNull(openers);
        this.numberOfBalls = numberOfBalls;
        if (openers.size() != 2) throw new IllegalArgumentException("There must be 2 openers");
    }

    public LineUp battingTeam() {
        return battingTeam;
    }

    public LineUp bowlingTeam() {
        return bowlingTeam;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public ImmutableList<Player> openers() {
        return openers;
    }

    /**
     * @return The max number of balls allowed in this innings, or empty to get it from the match object
     */
    public OptionalInt numberOfBalls() {
        return toOptional(numberOfBalls);
    }

    public static Builder inningsStarting() {
        return new Builder();
    }

    public static final class Builder implements MatchEventBuilder<InningsStartingEvent> {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private Instant time;
        private ImmutableList<Player> openers;
        private Integer numberOfBalls;

        public Builder withBattingTeam(LineUp battingTeam) {
            this.battingTeam = battingTeam;
            return this;
        }

        public Builder withBowlingTeam(LineUp bowlingTeam) {
            this.bowlingTeam = bowlingTeam;
            return this;
        }

        public Builder withTime(Instant startTime) {
            this.time = startTime;
            return this;
        }

        public Builder withOpeners(ImmutableList<Player> openers) {
            this.openers = openers;
            return this;
        }

        public Builder withOpeners(Player first, Player second) {
            return withOpeners(ImmutableList.of(first, second));
        }

        /**
         * Sets the limit of the number of balls. This can be left unset to use the match default, so should generally
         * be used in rain-affected matches where the innings will have a reduced number of balls from the outset.
         * @param numberOfBalls The maximum number of deliveries allowed in this innings
         * @return This builder
         */
        public Builder withNumberOfBalls(Integer numberOfBalls) {
            this.numberOfBalls = numberOfBalls;
            return this;
        }

        public InningsStartingEvent build() {
            requireNonNull(battingTeam, "battingTeam");
            requireNonNull(bowlingTeam, "bowlingTeam");
            ImmutableList<Player> openers = this.openers == null ? battingTeam.battingOrder().view(0, 1) : this.openers;
            return new InningsStartingEvent(battingTeam, bowlingTeam, time, openers, numberOfBalls);
        }
    }
}
