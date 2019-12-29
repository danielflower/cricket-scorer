package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static com.danielflower.crickam.scorer.Crictils.toOptional;
import static java.util.Objects.requireNonNull;

public final class InningsStartingEvent implements MatchEvent {

    private final LineUp battingTeam;
    private final LineUp bowlingTeam;
    private final Instant time;
    private final ImmutableList<Player> openers;
    private final int inningsNumberForMatch;
    private final int inningsNumberForBattingTeam;
    private final boolean isFinalInnings;
    private final Integer maxBalls;
    private final Integer target;
    private final boolean isFollowingOn;
    private final Integer maxOvers;

    private InningsStartingEvent(LineUp battingTeam, LineUp bowlingTeam, Instant time, ImmutableList<Player> openers, Integer maxBalls, Integer maxOvers, Integer target, boolean isFollowingOn, int inningsNumberForMatch, int inningsNumberForBattingTeam, boolean isFinalInnings) {
        this.battingTeam = requireNonNull(battingTeam, "battingTeam");
        this.bowlingTeam = requireNonNull(bowlingTeam, "bowlingTeam");
        this.time = time;
        this.openers = requireNonNull(openers);
        this.maxOvers = maxOvers;
        this.inningsNumberForMatch = requireInRange("inningsNumberForMatch", inningsNumberForMatch, isFinalInnings ? 2 : 1);
        this.inningsNumberForBattingTeam = requireInRange("inningsNumberForBattingTeam", inningsNumberForBattingTeam, 1);
        this.isFinalInnings = isFinalInnings;
        if (openers.size() != 2) throw new IllegalArgumentException("There must be 2 openers");
        this.maxBalls = requireInRange("numberOfBalls", maxBalls, 1, Integer.MAX_VALUE);
        if (isFinalInnings) {
            requireNonNull(target, "The target cannot be null for the final innings");
        } else if (target != null) {
            throw new IllegalArgumentException("The " + Crictils.withOrdinal(inningsNumberForMatch) + " innings cannot have a target as it is not the last scheduled innings.");
        }
        this.target = requireInRange("target", target, 1, Integer.MAX_VALUE);
        this.isFollowingOn = isFollowingOn;
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
     * @return The number of the innings in the match, starting at 1
     */
    public int inningsNumberForMatch() {
        return inningsNumberForMatch;
    }

    /**
     * @return The number of the innings that this team has batted for in this match, starting at 1
     */
    public int inningsNumberForBattingTeam() {
        return inningsNumberForBattingTeam;
    }

    /**
     * @return true if this is the last scheduled innings of the match
     */
    public boolean finalInnings() {
        return isFinalInnings;
    }

    /**
     * @return The max number of balls allowed in this innings, or empty if there is no limit
     */
    public OptionalInt maxBalls() {
        return toOptional(maxBalls);
    }

    public OptionalInt maxOvers() {
        return Crictils.toOptional(maxOvers);
    }

    /**
     * @return The target runs for the batting team to reach in order to win the match or empty if it is not the last innings.
     */
    public OptionalInt target() {
        return toOptional(target);
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
        private Integer maxBalls;
        private Integer target;
        private Boolean isFollowingOn;
        private Integer maxOvers;

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
         * <p>If only one of this or {@link #withMaxOvers(Integer)} is specified then
         * one will be derived from the other. If neither are set then the match defaults will be used.</p>
         *
         * @param maxBalls The maximum number of deliveries allowed in this innings
         * @return This builder
         */
        public Builder withMaxBalls(Integer maxBalls) {
            this.maxBalls = maxBalls;
            return this;
        }

        /**
         * Sets the limit of the number of overs. This can be left unset to use the match default, so should generally
         * be used in rain-affected matches where the innings will have a reduced number of overs from the outset.
         * <p>If only one of {@link #withMaxBalls(Integer)} or this is specified then
         * one will be derived from the other. If neither are set then the match defaults will be used.</p>
         *
         * @param maxOvers The maximum number of deliveries allowed in this innings
         * @return This builder
         */
        public Builder withMaxOvers(Integer maxOvers) {
            this.maxOvers = maxOvers;
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

        public InningsStartingEvent build(Match match) {

            Optional<Innings> last = match.inningsList().last();
            if (battingTeam == null && bowlingTeam == null) {
                if (match.inningsList().isEmpty()) {
                    throw new NullPointerException("At least the batting team or bowling team must be set for the first innings of the match");
                }
                battingTeam = last.get().bowlingTeam();
                bowlingTeam = last.get().battingTeam();
            } else if (battingTeam == null) {
                battingTeam = match.otherTeam(bowlingTeam);
            } else if (bowlingTeam == null) {
                bowlingTeam = match.otherTeam(battingTeam);
            }

            boolean followOn;
            if (last.isPresent() && last.get().battingTeam().team().equals(battingTeam.team())) {
                followOn = true;
            } else {
                followOn = isFollowingOn != null && isFollowingOn;
            }

            openers = openers == null ? battingTeam.battingOrder().subList(0, 1) : this.openers;
            for (Player opener : openers) {
                if (!battingTeam.battingOrder().contains(opener)) {
                    throw new IllegalStateException("The opener " + opener + " is not in the batting team " + battingTeam);
                }
            }

            if (maxOvers == null && maxBalls == null) {
                maxOvers = Crictils.toInteger(match.oversPerInnings());
                maxBalls = Crictils.toInteger(match.ballsPerInnings());
            } else if (maxOvers == null) {
                maxOvers = maxBalls / 6;
            } else if (maxBalls == null) {
                maxBalls = maxOvers * 6;
            }

            int inningsNumber = match.inningsList().size() + 1;
            int battingInningsNumber = 1;
            boolean finalInnings = false;
            InningsStartingEvent.Builder builder = this;
            match.ballsPerInnings().ifPresent(builder::withMaxBalls);
            if (last.isPresent()) {
                Innings prev = last.get();
                LineUp battingTeam = prev.bowlingTeam();
                LineUp bowlingTeam = prev.battingTeam();
                builder.withBattingTeam(battingTeam)
                    .withBowlingTeam(bowlingTeam);
                battingInningsNumber = (int) match.inningsList().stream().filter(i -> i.battingTeam().team().equals(battingTeam.team())).count() + 1;
                finalInnings = inningsNumber == (2 * match.numberOfInningsPerTeam());
                if (finalInnings) {
                    int battingScore = match.scoredByTeam(battingTeam).teamRuns();
                    int bowlingScore = match.scoredByTeam(bowlingTeam).teamRuns();
                    int target = (bowlingScore - battingScore) + 1;
                    builder.withTarget(target);
                }
            }

            return new InningsStartingEvent(battingTeam, bowlingTeam, time, openers, maxBalls, maxOvers, target, followOn,
                inningsNumber, battingInningsNumber, finalInnings);
        }
    }
}
