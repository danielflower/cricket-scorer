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
    private final Integer target;
    private final boolean isFollowingOn;

    private InningsStartingEvent(LineUp battingTeam, LineUp bowlingTeam, Instant time, ImmutableList<Player> openers, Integer numberOfBalls, Integer target, boolean isFollowingOn) {
        this.battingTeam = requireNonNull(battingTeam);
        this.bowlingTeam = bowlingTeam;
        this.time = time;
        this.openers = requireNonNull(openers);
        this.numberOfBalls = numberOfBalls;
        this.target = target;
        this.isFollowingOn = isFollowingOn;
        if (openers.size() != 2) throw new IllegalArgumentException("There must be 2 openers");
    }

    public LineUp battingTeam() {
        return battingTeam;
    }

    public Optional<LineUp> bowlingTeam() {
        return Optional.ofNullable(bowlingTeam);
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

    /**
     * @return The target runs for the batting team to reach in order to win the match. Only applies to the final
     * innings in a match, and can be unset to have a calculated value based on the match state.
     */
    public OptionalInt target() {
        return toOptional(target);
    }

    public static Builder inningsStarting() {
        return new Builder();
    }

    /**
     * @return Returns true if this innings was started after the opposition enforced the follow-on
     */
    public boolean followingOn() {
        return isFollowingOn;
    }

    public static final class Builder implements MatchEventBuilder<InningsStartingEvent> {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private Instant time;
        private ImmutableList<Player> openers;
        private Integer numberOfBalls;
        private Integer target;
        private boolean isFollowingOn;

        /**
         * @param battingTeam The batting team. This must be set.
         * @return This builder
         */
        public Builder withBattingTeam(LineUp battingTeam) {
            this.battingTeam = battingTeam;
            return this;
        }

        /**
         * @param bowlingTeam The bowling team. This can be left unset.
         * @return This builder
         */
        public Builder withBowlingTeam(LineUp bowlingTeam) {
            this.bowlingTeam = bowlingTeam;
            return this;
        }

        /**
         * @param startTime The time the innings is deemed to have started
         * @return This builder
         */
        public Builder withTime(Instant startTime) {
            this.time = startTime;
            return this;
        }

        /**
         * @param openers The two openers for the batting team
         * @return This builder
         */
        public Builder withOpeners(ImmutableList<Player> openers) {
            this.openers = openers;
            return this;
        }

        /**
         * @param first  The opener who will face the first ball
         * @param second The opener who will be at the non-striker's end
         * @return This builder
         */
        public Builder withOpeners(Player first, Player second) {
            return withOpeners(ImmutableList.of(first, second));
        }

        /**
         * Sets the limit of the number of balls. This can be left unset to use the match default, so should generally
         * be used in rain-affected matches where the innings will have a reduced number of balls from the outset.
         *
         * @param numberOfBalls The maximum number of deliveries allowed in this innings
         * @return This builder
         */
        public Builder withNumberOfBalls(Integer numberOfBalls) {
            this.numberOfBalls = numberOfBalls;
            return this;
        }

        /**
         * Sets the target score for this innings. Only applicable to the final innings in match. Leave unset in order
         * to have a calculated value.
         *
         * @param target The target runs for the batting team to reach in order to win the match
         * @return This builder
         */
        public Builder withTarget(Integer target) {
            this.target = target;
            return this;
        }

        /**
         * @param followingOn true if the batting team is following on from their previous innings
         * @return This builder
         */
        public Builder withFollowingOn(boolean followingOn) {
            isFollowingOn = followingOn;
            return this;
        }

        public InningsStartingEvent build() {
            requireNonNull(battingTeam, "battingTeam");
            ImmutableList<Player> openers = this.openers == null ? battingTeam.battingOrder().subList(0, 1) : this.openers;
            return new InningsStartingEvent(battingTeam, bowlingTeam, time, openers, numberOfBalls, target, isFollowingOn);
        }
    }
}
