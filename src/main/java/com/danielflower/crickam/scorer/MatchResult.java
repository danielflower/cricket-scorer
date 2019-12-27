package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchCompletedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.toOptional;
import static java.util.Objects.requireNonNull;

/**
 * The result of a match
 */
public class MatchResult {

    /**
     * Describes how a match was won, drawn, tied or otherwise concluded
     */
    enum ResultType {Won, Tied, Drawn, NoResult, Abandoned, Awarded, Conceded}

    /**
     * Specifies the measure used to determine the winning team
     */
    enum Measure {
        Wickets(false, "wicket", "wickets"),
        Runs(false, "run", "runs"),
        WicketCount(true, "in wicket count (tie breaker)", null),
        BoundaryCount(true, "boundaries in boundary count (tie breaker)", null),
        WicketsInBowlOut(true, "wickets in bowl out (tie breaker)", null);

        private final boolean isTieBreaker;
        private final String singular;
        private final String plural;

        Measure(boolean isTieBreaker, String singular, String plural) {
            this.isTieBreaker = isTieBreaker;
            this.singular = singular;
            this.plural = plural;
        }

        public String toString(int count) {
            return count + " " + ((count == 1) ? singular : requireNonNull(plural, singular));
        }
    }

    private final ResultType resultType;
    private final LineUp winningTeam;
    private final Measure wonBy;
    private final Integer wonByAmount;
    private final boolean duckworthLewisApplied;

    private MatchResult(@NotNull MatchResult.ResultType resultType, LineUp winningTeam, Measure wonBy, Integer wonByAmount, boolean duckworthLewisApplied) {
        this.resultType = requireNonNull(resultType);
        boolean hasWinner = resultType == ResultType.Awarded || resultType == ResultType.Conceded || resultType == ResultType.Won;
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
    public ResultType resultType() {
        return resultType;
    }

    /**
     * If {@link #resultType()} is {@link ResultType#Won}, {@link ResultType#Awarded} or {@link ResultType#Conceded}, then this is the team that won the match
     *
     * @return The winning team, if there is one
     */
    public Optional<LineUp> winningTeam() {
        return Optional.ofNullable(winningTeam);
    }

    /**
     * @return The measure used to determine the winner
     */
    public Optional<Measure> wonBy() {
        return Optional.ofNullable(wonBy);
    }

    /**
     * @return The size of the win, where the unit is specified by {@link #wonBy()}
     */
    public OptionalInt wonByAmount() {
        return toOptional(wonByAmount);
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
    public static Builder matchResult() {
        return new Builder();
    }

    @Override
    public String toString() {
        switch (resultType) {
            case NoResult:
                return "No result";
            case Awarded:
                return "Awarded to " + winningTeam.team().name();
            case Conceded:
                return "Conceded. Won by " + winningTeam.team().name();
            case Won:
                return winningTeam.team().name() + " won by " + wonBy.toString(wonByAmount);
            case Tied:
                return "Match tied";
        }
        return resultType.toString();
    }

    public static final MatchResult NoResult = MatchResult.matchResult().withResultType(ResultType.NoResult).build();

    public static class Builder {
        private ResultType resultType;
        private LineUp winningTeam;
        private Measure wonBy;
        private Integer wonByAmount;
        private boolean duckworthLewisApplied;

        /**
         * @param resultType How the match was won
         * @return This builder
         */
        public Builder withResultType(ResultType resultType) {
            this.resultType = resultType;
            return this;
        }

        /**
         * @param winningTeam If one of the teams won, then this specifies the winner
         * @return This builder
         */
        public Builder withWinningTeam(LineUp winningTeam) {
            this.winningTeam = winningTeam;
            return this;
        }

        /**
         * @param wonBy The measure of way the win is declared, e.g. by wickets if the batting team reaches their target
         * @return This builder
         */
        public Builder withWonBy(Measure wonBy) {
            this.wonBy = wonBy;
            return this;
        }

        /**
         * @param wonByAmount The number of runs or wickets etc that the winning team won by
         * @return This builder
         */
        public Builder withWonByAmount(Integer wonByAmount) {
            this.wonByAmount = wonByAmount;
            return this;
        }

        /**
         * @param duckworthLewisApplied true if Duckworth-Lewis was used when determining the winner
         * @return This builder
         */
        public Builder withDuckworthLewisApplied(boolean duckworthLewisApplied) {
            this.duckworthLewisApplied = duckworthLewisApplied;
            return this;
        }

        public MatchResult build() {
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
    public static MatchResult fromMatch(Match match) {
        if (match.inningsList().last().isPresent()) {
            Innings innings = match.inningsList().last().get();
            if (innings.target().isPresent()) {
                int remaining = innings.target().getAsInt() - innings.score().teamRuns();
                if (remaining <= 0) {
                    return matchResult()
                        .withResultType(ResultType.Won)
                        .withWinningTeam(innings.battingTeam())
                        .withWonBy(Measure.Wickets)
                        .withWonByAmount(innings.yetToBat().size() + 1)
                        .build();
                } else if (innings.state() == Innings.State.COMPLETED) {
                    if (remaining > 1) {
                        return matchResult()
                            .withResultType(ResultType.Won)
                            .withWinningTeam(innings.bowlingTeam())
                            .withWonBy(Measure.Runs)
                            .withWonByAmount(remaining - 1)
                            .build();
                    } else if (remaining == 1) {
                        return MatchResult.matchResult().withResultType(ResultType.Tied).build();
                    }
                }
            }
        }
        return NoResult;
    }


}
