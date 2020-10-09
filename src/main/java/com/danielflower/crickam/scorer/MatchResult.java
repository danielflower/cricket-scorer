package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchCompletedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The result of a match
 */
@Immutable
public final class MatchResult {

    /**
     * Describes how a match was won, drawn, tied or otherwise concluded
     */
    public enum ResultType {WON, TIED, DRAWN, NO_RESULT, ABANDONED, AWARDED, CONCEDED}

    /**
     * Specifies the measure used to determine the winning team
     */
    public enum Measure {
        WICKETS(false, "wicket", "wickets"),
        RUNS(false, "run", "runs"),
        INNINGS_AND_RUNS(false, "run", "runs"),
        WICKET_COUNT(true, "in wicket count (tie breaker)", null),
        BOUNDARY_COUNT(true, "boundaries in boundary count (tie breaker)", null),
        WICKETS_IN_BOWL_OUT(true, "wickets in bowl out (tie breaker)", null);

        private final boolean isTieBreaker;
        private final String singular;
        private final String plural;

        Measure(boolean isTieBreaker, String singular, @Nullable String plural) {
            this.isTieBreaker = isTieBreaker;
            this.singular = singular;
            this.plural = plural;
        }

        public String toString(int count) {
            String prefix = (this == INNINGS_AND_RUNS) ? "an innings and " : "";
            return prefix + count + " " + ((count == 1) ? singular : requireNonNull(plural, singular));
        }

        /**
         * @return True if the match was tied and then the winner was decided on some other measure
         */
        public boolean tieBreakerUsed() {
            return isTieBreaker;
        }
    }

    private final ResultType resultType;
    private final LineUp winningTeam;
    private final Measure wonBy;
    private final Integer wonByAmount;
    private final boolean duckworthLewisApplied;

    private MatchResult(MatchResult.ResultType resultType, @Nullable LineUp winningTeam, @Nullable Measure wonBy, @Nullable Integer wonByAmount, boolean duckworthLewisApplied) {
        this.resultType = requireNonNull(resultType, "resultType");
        boolean hasWinner = resultType == ResultType.AWARDED || resultType == ResultType.CONCEDED || resultType == ResultType.WON;
        if (hasWinner) {
            requireNonNull(winningTeam, "winningTeam must be set for a result of type " + resultType);
            requireNonNull(wonBy, "wonBy must be set for a result of type " + resultType);
            requireNonNull(wonByAmount, "wonByAmount must be set for a result of type " + resultType);
        } else if (winningTeam != null || wonBy != null || wonByAmount != null) {
            throw new IllegalStateException("For the " + resultType + " type of win, the winningTeam, wonBy and wonByAmount should not be set");
        }
        this.winningTeam = winningTeam;
        this.wonBy = wonBy;
        this.wonByAmount = wonByAmount;
        this.duckworthLewisApplied = duckworthLewisApplied;
    }

    /**
     * @return The type of result
     */
    public @Nonnull ResultType resultType() {
        return resultType;
    }

    /**
     * If {@link #resultType()} is {@link ResultType#WON}, {@link ResultType#AWARDED} or {@link ResultType#CONCEDED}, then this is the team that won the match
     *
     * @return The winning team, if there is one
     */
    public @Nullable LineUp winningTeam() {
        return winningTeam;
    }

    /**
     * @return The measure used to determine the winner
     */
    public @Nullable Measure wonBy() {
        return wonBy;
    }

    /**
     * @return The size of the win, where the unit is specified by {@link #wonBy()}
     */
    public @Nullable Integer wonByAmount() {
        return wonByAmount;
    }

    /**
     * @return True if the Duckworth-Lewis algorithm was used to determine this result
     */
    public boolean duckworthLewisApplied() {
        return duckworthLewisApplied;
    }

    /**
     * @return A new builder
     */
    public static @Nonnull Builder matchResult() {
        return new Builder();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchResult that = (MatchResult) o;
        return duckworthLewisApplied == that.duckworthLewisApplied &&
            resultType == that.resultType &&
            Objects.equals(winningTeam, that.winningTeam) &&
            wonBy == that.wonBy &&
            Objects.equals(wonByAmount, that.wonByAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultType, winningTeam, wonBy, wonByAmount, duckworthLewisApplied);
    }

    @Override
    public String toString() {
        switch (resultType) {
            case NO_RESULT:
                return "No result";
            case AWARDED:
                return "Awarded to " + winningTeam.teamName();
            case CONCEDED:
                return "Conceded. Won by " + winningTeam.teamName();
            case WON:
                return winningTeam.teamName() + " won by " + wonBy.toString(wonByAmount);
            case TIED:
                return "Match tied";
        }
        return resultType.toString();
    }

    public static final MatchResult NoResult = MatchResult.matchResult().withResultType(ResultType.NO_RESULT).build();

    public static final class Builder {
        private ResultType resultType;
        private LineUp winningTeam;
        private Measure wonBy;
        private Integer wonByAmount;
        private boolean duckworthLewisApplied;

        /**
         * @param resultType How the match was won
         * @return This builder
         */
        public @Nonnull Builder withResultType(ResultType resultType) {
            this.resultType = resultType;
            return this;
        }

        /**
         * @param winningTeam If one of the teams won, then this specifies the winner
         * @return This builder
         */
        public @Nonnull Builder withWinningTeam(@Nullable LineUp winningTeam) {
            this.winningTeam = winningTeam;
            return this;
        }

        /**
         * @param wonBy The measure of way the win is declared, e.g. by wickets if the batting team reaches their target
         * @return This builder
         */
        public @Nonnull Builder withWonBy(@Nullable Measure wonBy) {
            this.wonBy = wonBy;
            return this;
        }

        /**
         * @param wonByAmount The number of runs or wickets etc that the winning team won by
         * @return This builder
         */
        public @Nonnull Builder withWonByAmount(@Nullable Integer wonByAmount) {
            this.wonByAmount = wonByAmount;
            return this;
        }

        /**
         * @param duckworthLewisApplied true if Duckworth-Lewis was used when determining the winner
         * @return This builder
         */
        public @Nonnull Builder withDuckworthLewisApplied(boolean duckworthLewisApplied) {
            this.duckworthLewisApplied = duckworthLewisApplied;
            return this;
        }

        public @Nonnull MatchResult build() {
            return new MatchResult(resultType, winningTeam, wonBy, wonByAmount, duckworthLewisApplied);
        }
    }

    /**
     * Calculates the result based on a match.
     * <p>Note that on completion of a match, a different result can be determined via the
     * {@link MatchCompletedEvent.Builder#withResult(MatchResult)} method which may in fact disagree with the result
     * determined by this library.</p>
     * <p>Note: this method does apply Duckworth-Lewis in limited overs matches</p>
     *
     * @param match The match to check
     * @return The calculated result of the given match
     */
    public static @Nonnull MatchResult fromMatch(Match match) {
        if (match.inningsList().last() != null) {
            Innings innings = match.inningsList().last();
            boolean isLastInnings = innings.target() != null;
            if (!isLastInnings) {
                if (match.inningsList().stream().filter(i -> i.battingTeam().equals(innings.battingTeam()) && i.shouldBeComplete()).count() >= match.numberOfInningsPerTeam()) {
                    // the team that just completed their innings will bat no more. But have they exceeded the other team's score?
                    int justFinishedBattingTotalScore = match.scoredByTeam(innings.battingTeam()).teamRuns();
                    int otherTeamScore = match.scoredByTeam(innings.bowlingTeam()).teamRuns();
                    int difference = otherTeamScore - justFinishedBattingTotalScore;
                    if (difference > 0) {
                        return MatchResult.matchResult()
                            .withResultType(ResultType.WON)
                            .withWinningTeam(innings.bowlingTeam())
                            .withWonBy(Measure.INNINGS_AND_RUNS)
                            .withWonByAmount(difference)
                            .build();
                    }
                }
            }
            if (isLastInnings) {
                int remaining = innings.target() - innings.score().teamRuns();
                if (remaining <= 0) {
                    return matchResult()
                        .withResultType(ResultType.WON)
                        .withWinningTeam(innings.battingTeam())
                        .withWonBy(Measure.WICKETS)
                        .withWonByAmount(innings.yetToBat().size() + 1)
                        .build();
                } else if (innings.shouldBeComplete()) {
                    if (remaining > 1) {
                        return matchResult()
                            .withResultType(ResultType.WON)
                            .withWinningTeam(innings.bowlingTeam())
                            .withWonBy(Measure.RUNS)
                            .withWonByAmount(remaining - 1)
                            .build();
                    } else {
                        return MatchResult.matchResult().withResultType(ResultType.TIED).build();
                    }
                }
            }
        }
        return NoResult;
    }


}
