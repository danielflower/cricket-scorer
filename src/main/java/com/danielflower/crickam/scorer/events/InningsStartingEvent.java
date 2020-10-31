package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

@Immutable
public final class InningsStartingEvent extends BaseMatchEvent {

    private final Score startingScore;
    private final LineUp battingTeam;
    private final LineUp bowlingTeam;
    private final ImmutableList<Player> openers;
    private final int inningsNumberForMatch;
    private final int inningsNumberForBattingTeam;
    private final boolean isFinalInnings;
    private final Integer maxBalls;
    private final Integer target;
    private final boolean isFollowingOn;
    private final Integer maxOvers;

    private InningsStartingEvent(UUID id, @Nullable UUID generatedBy, @Nullable Instant time, Score startingScore, LineUp battingTeam, LineUp bowlingTeam, ImmutableList<Player> openers, @Nullable Integer maxBalls, @Nullable Integer maxOvers, @Nullable Integer target, boolean isFollowingOn, @Nonnegative int inningsNumberForMatch, @Nonnegative int inningsNumberForBattingTeam, boolean isFinalInnings, ImmutableList<MatchEventBuilder<?, ?>> generatedEvents, @Nullable Object customData) {
        super(id, time, generatedBy, customData, generatedEvents);
        this.startingScore = requireNonNull(startingScore, "startingScore");
        this.battingTeam = requireNonNull(battingTeam, "battingTeam");
        this.bowlingTeam = requireNonNull(bowlingTeam, "bowlingTeam");
        this.openers = requireNonNull(openers, "openers");
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

    /**
     * @return The score the innings started at, which is almost always {@link Score#EMPTY}
     */
    public Score startingScore() {
        return startingScore;
    }

    public LineUp battingTeam() {
        return battingTeam;
    }

    public LineUp bowlingTeam() {
        return bowlingTeam;
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
     * @return The max number of (valid) balls allowed in this innings, or null if there is no limit
     */
    public Integer maxBalls() {
        return maxBalls;
    }

    /**
     * @return The max number of overs allowed in this innings, or null if there is no limit
     */
    public Integer maxOvers() {
        return maxOvers;
    }

    /**
     * @return The target runs for the batting team to reach in order to win the match or null if it is not the last innings.
     */
    public Integer target() {
        return target;
    }

    /**
     * @return Returns true if this innings was started after the opposition enforced the follow-on
     */
    public boolean followingOn() {
        return isFollowingOn;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return new Builder()
            .withBattingTeam(battingTeam)
            .withBowlingTeam(bowlingTeam)
            .withOpeners(openers)
            .withMaxBalls(maxBalls)
            .withTarget(target)
            .withFollowingOn(isFollowingOn)
            .withMaxOvers(maxOvers)
            .withStartingScore(startingScore)
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, InningsStartingEvent> {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private ImmutableList<Player> openers;
        private Integer maxBalls;
        private Integer target;
        private Boolean isFollowingOn;
        private Integer maxOvers;
        private Score startingScore;

        public @Nullable LineUp battingTeam() {
            return battingTeam;
        }
        public @Nullable LineUp bowlingTeam() {
            return bowlingTeam;
        }
        public @Nullable ImmutableList<Player> openers() {
            return openers;
        }
        public @Nullable Integer maxBalls() {
            return maxBalls;
        }
        public @Nullable Integer target() {
            return target;
        }
        public @Nullable Boolean followingOn() {
            return isFollowingOn;
        }
        public @Nullable Integer maxOvers() {
            return maxOvers;
        }
        public @Nullable Score startingScore() {
            return startingScore;
        }

        /**
         * This sets the starting score of the batting team in this innings. This is almost also {@link Score#EMPTY}
         * except on the rare occasions where the team was awarded penalties in the previous innings while they were
         * bowling.
         *
         * @param startingScore The score the innings starts at, which defaults to {@link Score#EMPTY}
         * @return This builder
         */
        public @Nonnull Builder withStartingScore(Score startingScore) {
            this.startingScore = startingScore;
            return this;
        }

        /**
         * @param battingTeam The batting team. This must be set.
         * @return This builder
         */
        public @Nonnull Builder withBattingTeam(LineUp battingTeam) {
            this.battingTeam = battingTeam;
            return this;
        }

        /**
         * @param bowlingTeam The bowling team. This can be left unset.
         * @return This builder
         */
        public @Nonnull Builder withBowlingTeam(LineUp bowlingTeam) {
            this.bowlingTeam = bowlingTeam;
            return this;
        }

        /**
         * @param openers The two openers for the batting team
         * @return This builder
         */
        public @Nonnull Builder withOpeners(ImmutableList<Player> openers) {
            this.openers = openers;
            return this;
        }

        /**
         * @param first  The opener who will face the first ball
         * @param second The opener who will be at the non-striker's end
         * @return This builder
         */
        public @Nonnull Builder withOpeners(Player first, Player second) {
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
        public @Nonnull Builder withMaxBalls(Integer maxBalls) {
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
        public @Nonnull Builder withMaxOvers(Integer maxOvers) {
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
        public @Nonnull Builder withTarget(Integer target) {
            this.target = target;
            return this;
        }

        /**
         * @param followingOn true if the batting team is following on from their previous innings
         * @return This builder
         */
        public @Nonnull Builder withFollowingOn(boolean followingOn) {
            isFollowingOn = followingOn;
            return this;
        }

        @Nonnull
        public InningsStartingEvent build(Match match) {

            Innings last = match.inningsList().last();
            if (battingTeam == null && bowlingTeam == null) {
                if (last == null) {
                    throw new NullPointerException("At least the batting team or bowling team must be set for the first innings of the match");
                }
                battingTeam = last.bowlingTeam();
                bowlingTeam = last.battingTeam();
            } else if (battingTeam == null) {
                battingTeam = match.otherTeam(bowlingTeam);
            } else if (bowlingTeam == null) {
                bowlingTeam = match.otherTeam(battingTeam);
            }

            boolean followOn;
            if (last != null && last.battingTeam().sameTeam(battingTeam)) {
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
                maxOvers = match.oversPerInnings();
                maxBalls = match.ballsPerInnings();
            } else if (maxOvers == null) {
                maxOvers = maxBalls / 6;
            } else if (maxBalls == null) {
                maxBalls = maxOvers * 6;
            }

            int inningsNumber = match.inningsList().size() + 1;
            int battingInningsNumber = 1;
            boolean finalInnings = false;
            InningsStartingEvent.Builder builder = this;
            if (match.ballsPerInnings() != null) {
                builder.withMaxBalls(match.ballsPerInnings());
            }
            if (last != null) {
                if (battingTeam == null || bowlingTeam == null) {
                    builder.withBattingTeam(last.bowlingTeam())
                        .withBowlingTeam(last.battingTeam());
                }

                battingInningsNumber = (int) match.inningsList().stream().filter(i -> i.battingTeam().sameTeam(battingTeam)).count() + 1;
                finalInnings = inningsNumber == (2 * match.numberOfInningsPerTeam());
                if (finalInnings) {
                    int battingScore = match.scoredByTeam(battingTeam).teamRuns();
                    int bowlingScore = match.scoredByTeam(bowlingTeam).teamRuns();
                    int target = (bowlingScore - battingScore) + 1;
                    builder.withTarget(target);
                }
            }

            ImmutableList<MatchEventBuilder<?, ?>> generatedEvents = openers.stream()
                .map(player -> MatchEvents.batterInningsStarting().withBatter(player).withTime(time()))
                .collect(ImmutableList.toImmutableList());
            Score startingScore = this.startingScore == null ? Score.EMPTY : this.startingScore;
            return new InningsStartingEvent(id(), generatedBy(), time(), startingScore, battingTeam, bowlingTeam, openers, maxBalls, maxOvers, target, followOn,
                inningsNumber, battingInningsNumber, finalInnings, generatedEvents, customData());
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(battingTeam, builder.battingTeam) &&
                Objects.equals(bowlingTeam, builder.bowlingTeam) &&
                Objects.equals(openers, builder.openers) &&
                Objects.equals(maxBalls, builder.maxBalls) &&
                Objects.equals(target, builder.target) &&
                Objects.equals(isFollowingOn, builder.isFollowingOn) &&
                Objects.equals(maxOvers, builder.maxOvers) &&
                Objects.equals(startingScore, builder.startingScore);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), battingTeam, bowlingTeam, openers, maxBalls, target, isFollowingOn, maxOvers, startingScore);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "battingTeam=" + battingTeam +
                ", bowlingTeam=" + bowlingTeam +
                ", openers=" + openers +
                ", maxBalls=" + maxBalls +
                ", target=" + target +
                ", isFollowingOn=" + isFollowingOn +
                ", maxOvers=" + maxOvers +
                ", startingScore=" + startingScore +
                "} " + super.toString();
        }
    }
}
