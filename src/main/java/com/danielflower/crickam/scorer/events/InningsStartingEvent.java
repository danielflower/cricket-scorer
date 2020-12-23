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
    private final int inningsNumberForMatch;
    private final int inningsNumberForBattingTeam;
    private final boolean isFinalInnings;
    private final Integer maxBalls;
    private final Integer target;
    private final boolean isFollowingOn;
    private final Integer maxOvers;

    private InningsStartingEvent(UUID id, @Nullable Instant time, @Nullable Object customData, @Nullable UUID transactionID, Score startingScore, LineUp battingTeam, LineUp bowlingTeam, @Nullable Integer maxBalls, @Nullable Integer maxOvers, @Nullable Integer target, boolean isFollowingOn, @Nonnegative int inningsNumberForMatch, @Nonnegative int inningsNumberForBattingTeam, boolean isFinalInnings) {
        super(id, time, customData, transactionID);
        this.startingScore = requireNonNull(startingScore, "startingScore");
        this.battingTeam = requireNonNull(battingTeam, "battingTeam");
        this.bowlingTeam = requireNonNull(bowlingTeam, "bowlingTeam");
        this.maxOvers = maxOvers;
        this.inningsNumberForMatch = requireInRange("inningsNumberForMatch", inningsNumberForMatch, isFinalInnings ? 2 : 1);
        this.inningsNumberForBattingTeam = requireInRange("inningsNumberForBattingTeam", inningsNumberForBattingTeam, 1);
        this.isFinalInnings = isFinalInnings;
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
    public @Nullable Integer maxBalls() {
        return maxBalls;
    }

    /**
     * @return The max number of overs allowed in this innings, or null if there is no limit
     */
    public @Nullable Integer maxOvers() {
        return maxOvers;
    }

    /**
     * @return The target runs for the batting team to reach in order to win the match or null if it is not the last innings.
     */
    public @Nullable Integer target() {
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
        return baseBuilder(new Builder())
            .withBattingTeam(battingTeam())
            .withBowlingTeam(bowlingTeam())
            .withMaxBalls(maxBalls())
            .withTarget(target())
            .withFollowingOn(followingOn())
            .withMaxOvers(maxOvers())
            .withStartingScore(startingScore())
            .withInningsNumberForMatch(inningsNumberForMatch())
            .withInningsNumberForBattingTeam(inningsNumberForBattingTeam())
            .withFinalInnings(finalInnings())
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InningsStartingEvent that = (InningsStartingEvent) o;
        return inningsNumberForMatch == that.inningsNumberForMatch && inningsNumberForBattingTeam == that.inningsNumberForBattingTeam && isFinalInnings == that.isFinalInnings && isFollowingOn == that.isFollowingOn && Objects.equals(startingScore, that.startingScore) && Objects.equals(battingTeam, that.battingTeam) && Objects.equals(bowlingTeam, that.bowlingTeam) && Objects.equals(maxBalls, that.maxBalls) && Objects.equals(target, that.target) && Objects.equals(maxOvers, that.maxOvers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingScore, battingTeam, bowlingTeam, inningsNumberForMatch, inningsNumberForBattingTeam, isFinalInnings, maxBalls, target, isFollowingOn, maxOvers);
    }

    @Override
    public String toString() {
        return "InningsStartingEvent{" +
            "startingScore=" + startingScore +
            ", battingTeam=" + battingTeam +
            ", bowlingTeam=" + bowlingTeam +
            ", inningsNumberForMatch=" + inningsNumberForMatch +
            ", inningsNumberForBattingTeam=" + inningsNumberForBattingTeam +
            ", isFinalInnings=" + isFinalInnings +
            ", maxBalls=" + maxBalls +
            ", target=" + target +
            ", isFollowingOn=" + isFollowingOn +
            ", maxOvers=" + maxOvers +
            '}';
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, InningsStartingEvent> {

        private LineUp battingTeam;
        private LineUp bowlingTeam;
        private Integer maxBalls;
        private Integer target;
        private Boolean isFollowingOn;
        private Integer maxOvers;
        private Score startingScore;
        private Integer inningsNumberForMatch;
        private Integer inningsNumberForBattingTeam;
        private Boolean finalInnings;

        public @Nullable LineUp battingTeam() {
            return battingTeam;
        }
        public @Nullable LineUp bowlingTeam() {
            return bowlingTeam;
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
        public @Nullable Integer inningsNumberForMatch() { return inningsNumberForMatch; }
        public @Nullable Integer inningsNumberForBattingTeam() { return inningsNumberForBattingTeam; }
        public @Nullable Boolean finalInnings() { return finalInnings; }

        /**
         * @param inningsNumberForMatch The innings number of the match, where the first innings is 1
         * @return This builder
         */
        public @Nonnull Builder withInningsNumberForMatch(@Nullable Integer inningsNumberForMatch) {
            this.inningsNumberForMatch = inningsNumberForMatch;
            return this;
        }

        /**
         * @param inningsNumberForBattingTeam  The innings number of the current batting team, where the first innings is 1
         * @return This builder
         */
        public @Nonnull Builder withInningsNumberForBattingTeam(@Nullable Integer inningsNumberForBattingTeam) {
            this.inningsNumberForBattingTeam = inningsNumberForBattingTeam;
            return this;
        }

        /**
         * @param finalInnings True if this is the expected final innings of the match
         * @return This builder
         */
        public @Nonnull Builder withFinalInnings(@Nullable Boolean finalInnings) {
            this.finalInnings = finalInnings;
            return this;
        }

        /**
         * This sets the starting score of the batting team in this innings. This is almost also {@link Score#EMPTY}
         * except on the rare occasions where the team was awarded penalties in the previous innings while they were
         * bowling.
         *
         * @param startingScore The score the innings starts at, which defaults to {@link Score#EMPTY}
         * @return This builder
         */
        public @Nonnull Builder withStartingScore(@Nullable Score startingScore) {
            this.startingScore = startingScore;
            return this;
        }

        /**
         * @param battingTeam The batting team. This must be set.
         * @return This builder
         */
        public @Nonnull Builder withBattingTeam(@Nullable LineUp battingTeam) {
            this.battingTeam = battingTeam;
            return this;
        }

        /**
         * @param bowlingTeam The bowling team. This can be left unset.
         * @return This builder
         */
        public @Nonnull Builder withBowlingTeam(@Nullable LineUp bowlingTeam) {
            this.bowlingTeam = bowlingTeam;
            return this;
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
        public @Nonnull Builder withMaxBalls(@Nullable Integer maxBalls) {
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
        public @Nonnull Builder withMaxOvers(@Nullable Integer maxOvers) {
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
        public @Nonnull Builder withTarget(@Nullable Integer target) {
            this.target = target;
            return this;
        }

        /**
         * @param followingOn true if the batting team is following on from their previous innings
         * @return This builder
         */
        public @Nonnull Builder withFollowingOn(@Nullable Boolean followingOn) {
            isFollowingOn = followingOn;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings last = match.inningsList().last();
            if (battingTeam == null && bowlingTeam == null) {
                if (last != null) {
                    battingTeam = last.bowlingTeam();
                    bowlingTeam = last.battingTeam();
                }
            } else if (battingTeam == null) {
                battingTeam = match.otherTeam(bowlingTeam);
            } else if (bowlingTeam == null) {
                bowlingTeam = match.otherTeam(battingTeam);
            }
            if (isFollowingOn == null) {
                isFollowingOn = last != null && last.battingTeam().sameTeam(battingTeam);
            }

            if (maxOvers == null && maxBalls == null) {
                maxOvers = match.oversPerInnings();
                maxBalls = match.ballsPerInnings();
            } else if (maxOvers == null) {
                maxOvers = maxBalls / 6;
            } else if (maxBalls == null) {
                maxBalls = maxOvers * 6;
            }

            if (inningsNumberForMatch == null) {
                inningsNumberForMatch = match.inningsList().size() + 1;
            }

            if (maxBalls == null && match.ballsPerInnings() != null) {
                maxBalls = match.ballsPerInnings();
            }

            if (last == null) {
                if (inningsNumberForBattingTeam == null) inningsNumberForBattingTeam = 1;
                if (finalInnings == null) finalInnings = false;
            } else {
                if (inningsNumberForBattingTeam == null) {
                    inningsNumberForBattingTeam = (int) match.inningsList().stream().filter(i -> i.battingTeam().sameTeam(battingTeam)).count() + 1;
                }
                if (finalInnings == null) {
                    finalInnings = (inningsNumberForMatch == (2 * match.numberOfInningsPerTeam()));
                }
                if (finalInnings && target == null) {
                    int battingScore = match.scoredByTeam(battingTeam).teamRuns();
                    int bowlingScore = match.scoredByTeam(bowlingTeam).teamRuns();
                    target = (bowlingScore - battingScore) + 1;
                }
            }
            return this;
        }

        @Nonnull
        public InningsStartingEvent build() {
            Score startingScore = this.startingScore == null ? Score.EMPTY : this.startingScore;
            return new InningsStartingEvent(id(), time(), customData(), transactionID(), startingScore, battingTeam, bowlingTeam, maxBalls, maxOvers, target, isFollowingOn,
                inningsNumberForMatch, inningsNumberForBattingTeam, finalInnings);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(battingTeam, builder.battingTeam) && Objects.equals(bowlingTeam, builder.bowlingTeam) && Objects.equals(maxBalls, builder.maxBalls) && Objects.equals(target, builder.target) && Objects.equals(isFollowingOn, builder.isFollowingOn) && Objects.equals(maxOvers, builder.maxOvers) && Objects.equals(startingScore, builder.startingScore) && Objects.equals(inningsNumberForMatch, builder.inningsNumberForMatch) && Objects.equals(inningsNumberForBattingTeam, builder.inningsNumberForBattingTeam) && Objects.equals(finalInnings, builder.finalInnings);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), battingTeam, bowlingTeam, maxBalls, target, isFollowingOn, maxOvers, startingScore, inningsNumberForMatch, inningsNumberForBattingTeam, finalInnings);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "battingTeam=" + battingTeam +
                ", bowlingTeam=" + bowlingTeam +
                ", maxBalls=" + maxBalls +
                ", target=" + target +
                ", isFollowingOn=" + isFollowingOn +
                ", maxOvers=" + maxOvers +
                ", startingScore=" + startingScore +
                ", matchInningsNumber=" + inningsNumberForMatch +
                ", battingInningsNumber=" + inningsNumberForBattingTeam +
                ", finalInnings=" + finalInnings +
                '}';
        }
    }
}
