package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TimeZone;

import static com.danielflower.crickam.scorer.Crictils.toInteger;

/**
 * A lovely game of cricket between two teams
 */
public final class Match {



    public enum State {
        NOT_STARTED, ABANDONED, IN_PROGRESS, COMPLETED;
    }
    private final FixedData data;

    private final State state;
    private final MatchResult result;
    private final ImmutableList<Innings> inningsList;
    private Match(FixedData data, State state, MatchResult result, ImmutableList<Innings> inningsList) {
        this.data = Objects.requireNonNull(data);
        this.state = Objects.requireNonNull(state);
        this.result = result;
        this.inningsList = Objects.requireNonNull(inningsList);
    }

    static Match newMatch(MatchStartingEvent e) {
        Venue venue = e.venue().orElse(null);
        TimeZone timeZone = e.timeZone().orElse(null);
        FixedData fd = new FixedData(e.matchID(), e.series().orElse(null), e.time().orElse(null), e.scheduledStartTime().orElse(null),
            e.teams(), e.matchType(), e.inningsPerTeam(), toInteger(e.oversPerInnings()), venue,
            e.numberOfScheduledDays(), toInteger(e.ballsPerInnings()), timeZone);
        return new Match(fd, State.NOT_STARTED, null, new ImmutableList<>());
    }
    private static class FixedData {

        private final String matchID;
        private final Series series;
        private final Instant time;
        private final Instant scheduledStartTime;
        private final ImmutableList<LineUp> teams;
        private final MatchType matchType;
        private final int inningsPerTeam;
        private final Integer oversPerInnings;
        private final Venue venue;
        private final int numberOfScheduledDays;
        private final Integer ballsPerInnings;
        private final TimeZone timeZone;
        public FixedData(String matchID, Series series, Instant time, Instant scheduledStartTime, ImmutableList<LineUp> teams, MatchType matchType, int inningsPerTeam, Integer oversPerInnings, Venue venue, int numberOfScheduledDays, Integer ballsPerInnings, TimeZone timeZone) {
            this.matchID = matchID;
            this.series = series;
            this.time = time;
            this.scheduledStartTime = scheduledStartTime;
            this.teams = teams;
            this.matchType = matchType;
            this.inningsPerTeam = inningsPerTeam;
            this.oversPerInnings = oversPerInnings;
            this.venue = venue;
            this.numberOfScheduledDays = numberOfScheduledDays;
            this.ballsPerInnings = ballsPerInnings;
            this.timeZone = timeZone;
        }
    }
    /**
     * @return The current match state
     */
    public State state() {
        return state;
    }

    /**
     * @return The current innings in the match, if an innings is in progress
     */
    public Optional<Innings> currentInnings() {
        return inningsList.last();
    }

    /**
     * @return The innings played in the match
     */
    public ImmutableList<Innings> inningsList() {
        return inningsList;
    }

    /**
     * @return A unique ID for this match
     */
    public String matchID() {
        return data.matchID;
    }

    public Optional<Series> series() {
        return Optional.ofNullable(data.series);
    }

    /**
     * @return The time when the match event was created
     */
    public Optional<Instant> time() {
        return Optional.ofNullable(data.time);
    }

    /**
     * @return The timezone that the match is played in
     */
    public Optional<TimeZone> timeZone() {
        return Optional.ofNullable(data.timeZone);
    }

    /**
     * @return The scheduled start time of the match, if known.
     */
    public Optional<Instant> scheduledStartTime() {
        return Optional.ofNullable(data.scheduledStartTime);
    }

    /**
     * @return The teams playing in this match, in no particular order.
     */
    public ImmutableList<LineUp> teams() {
        return data.teams;
    }

    /**
     * @return The number of scheduled balls per innings, or empty if there is no limit.
     */
    public OptionalInt ballsPerInnings() {
        return Crictils.toOptional(data.ballsPerInnings);
    }

    /**
     * @return The number of scheduled balls per innings, or -1 if there is no limit.
     */
    public OptionalInt oversPerInnings() {
        return Crictils.toOptional(data.oversPerInnings);
    }

    public MatchType matchType() {
        return data.matchType;
    }

    /**
     * @return The number of innings per team. Generally 1 for limited overs matches and 2 for first class / test matches.
     */
    public int numberOfInningsPerTeam() {
        return data.inningsPerTeam;
    }

    public Optional<Venue> venue() {
        return Optional.ofNullable(data.venue);
    }

    /**
     * @return The number of days this match goes for. Generally 1 for limited overs matches, and 5 for test matches.
     */
    public int numberOfScheduledDays() {
        return data.numberOfScheduledDays;
    }

    /**
     * @return The final result of the match, or empty if {@link #state()} is not {@link State#COMPLETED}
     */
    public Optional<MatchResult> result() {
        return Optional.ofNullable(result);
    }

    /**
     * Guesses the result of a match.
     * <p>Note that on the conclusion of the match, an actual result is determined and accessed by {@link #result()}
     * (and this may return a different value than this method).</p>
     * @return Calculates what the current result may be at the current point in time.
     */
    public MatchResult calculateResult() {
        return MatchResult.fromMatch(this);
    }

    Match onEvent(MatchEventBuilder<?> builder) {
        return onEvent(builder.build());
    }

    Match onEvent(MatchEvent event) {

        if (event instanceof BallCompletedEvent && currentInnings().isEmpty()) {
            throw new IllegalStateException("Cannot process a ball when there is no active innings");
        }
        State newState = this.state;
        MatchResult newResult = this.result;

        ImmutableList<Innings> newInningsList = inningsList;
        if (event instanceof InningsStartingEvent) {
            newState = State.IN_PROGRESS;
            InningsStartingEvent ise = (InningsStartingEvent) event;
            Integer scheduledBalls = data.ballsPerInnings;
            LineUp battingTeam = ise.battingTeam();
            LineUp bowlingTeam = ise.bowlingTeam().orElse(otherTeam(battingTeam));
            int inningsNumber = inningsList.size() + 1;
            Integer target = null;
            if (inningsNumber == (2 * numberOfInningsPerTeam())) {
                target = toInteger(ise.target());
                if (target == null) {
                    int battingScore = scoredByTeam(battingTeam).teamRuns();
                    int bowlingScore = scoredByTeam(bowlingTeam).teamRuns();
                    target = (bowlingScore - battingScore) + 1;
                }
            } else if (ise.target().isPresent()) {
                throw new IllegalStateException("There should not be a target specified until the last scheduled innings");
            }
            newInningsList = inningsList.add(Innings.newInnings(this, battingTeam, bowlingTeam, ise.openers(), inningsNumber, Instant.now(), scheduledBalls, target));
        } else if (event instanceof MatchCompletedEvent) {
            // don't pass to the innings
            newState = State.COMPLETED;
            newResult = ((MatchCompletedEvent) event).result().orElseGet(this::calculateResult);
        } else {
            Optional<Innings> lastInnings = inningsList.last();
            if (lastInnings.isPresent()) {
                Innings i = lastInnings.get().onEvent(event);
                newInningsList = newInningsList.removeLast().add(i);
            }
        }

        return new Match(data, newState, newResult, newInningsList);
    }

    /**
     * Gets the score for the given team line-up for the entire match
     * @param team The team to find the score for
     * @return The score for all innings the team has played in this match
     */
    public Score scoredByTeam(LineUp team) {
        Score total = Score.EMPTY;
        for (Innings innings : inningsList) {
            if (innings.battingTeam().equals(team)) {
                total = total.add(innings.score());
            }
        }
        return total;
    }

    private LineUp otherTeam(LineUp someTeam) {
        return this.teams().stream().filter(t -> !t.equals(someTeam)).findFirst().orElseThrow();
    }
}
