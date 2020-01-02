package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TimeZone;

import static com.danielflower.crickam.scorer.Crictils.toInteger;
import static com.danielflower.crickam.scorer.ImmutableList.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * A lovely game of cricket between two teams.
 * <p>To create a new match, pass a builder from {@link MatchEvents#matchStarting(MatchType)} to {@link MatchControl#newMatch(MatchStartingEvent.Builder)}</p>
 */
public final class Match {

    public enum State {
        NOT_STARTED, ABANDONED, IN_PROGRESS, COMPLETED
    }
    private final FixedData data;
    private final State state;

    private final MatchResult result;
    private final ImmutableList<Innings> completedInningsList;
    private final Innings currentInnings;
    private final Balls balls;

    private Match(FixedData data, State state, MatchResult result, ImmutableList<Innings> completedInningsList, Innings currentInnings, Balls balls) {
        this.data = requireNonNull(data);
        this.state = requireNonNull(state);
        this.result = result;
        this.completedInningsList = requireNonNull(completedInningsList);
        this.currentInnings = currentInnings;
        this.balls = requireNonNull(balls);
    }

    static Match newMatch(MatchStartingEvent e) {
        Venue venue = e.venue().orElse(null);
        TimeZone timeZone = e.timeZone().orElse(null);
        FixedData fd = new FixedData(e.matchID(), e.series().orElse(null), e.time().orElse(null), e.scheduledStartTime().orElse(null),
            e.teams(), e.matchType(), e.inningsPerTeam(), toInteger(e.oversPerInnings()), venue,
            e.numberOfScheduledDays(), toInteger(e.ballsPerInnings()), timeZone);
        return new Match(fd, State.NOT_STARTED, null, emptyList(), null, new Balls());
    }

    /**
     * @return A list of every ball bowled in the match
     */
    public Balls balls() {
        return balls;
    }

    /**
     * @return The current match state
     */
    public State state() {
        return state;
    }

    /**
     * @return The current innings in the match, if an innings is in progress
     * @see #inningsList()
     * @see #currentInnings()
     */
    public Optional<Innings> currentInnings() {
        return Optional.ofNullable(currentInnings);
    }

    /**
     * @return The innings played in the match that have been completed
     */
    public ImmutableList<Innings> completedInningsList() {
        return completedInningsList;
    }

    /**
     * @return All innings in the match, including any in-progress innings
     */
    public ImmutableList<Innings> inningsList() {
        return currentInnings == null ? completedInningsList : completedInningsList.add(currentInnings);
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

    Match onEvent(MatchEvent event) {

        if (event instanceof BallCompletedEvent && currentInnings().isEmpty()) {
            throw new IllegalStateException("Cannot process a ball when there is no active innings");
        }
        State newState = this.state;
        MatchResult newResult = this.result;
        Balls newBalls = this.balls;
        Innings newCurrentInnings = this.currentInnings;

        ImmutableList<Innings> newInningsList = completedInningsList;
        if (event instanceof InningsStartingEvent) {
            newState = State.IN_PROGRESS;
            newCurrentInnings = Innings.newInnings((InningsStartingEvent) event);
        } else if (event instanceof MatchCompletedEvent) {
            // don't pass to the innings
            newState = State.COMPLETED;
            newResult = ((MatchCompletedEvent) event).result().orElseGet(this::calculateResult);
        } else {
            if (newCurrentInnings != null) {
                if (event instanceof BallCompletedEvent) {
                    newBalls = newBalls.add((BallCompletedEvent) event);
                }
                newCurrentInnings = newCurrentInnings.onEvent(event);

                if (event instanceof InningsCompletedEvent) {
                    newInningsList = newInningsList.add(newCurrentInnings);
                    newCurrentInnings = null;
                }
            }
        }
        return new Match(data, newState, newResult, newInningsList, newCurrentInnings, newBalls);
    }

    /**
     * Gets the score for the given team line-up for the entire match
     * @param team The team to find the score for
     * @return The score for all innings the team has played in this match
     */
    public Score scoredByTeam(LineUp team) {
        Score total = Score.EMPTY;
        for (Innings innings : completedInningsList) {
            if (innings.battingTeam().equals(team)) {
                total = total.add(innings.score());
            }
        }
        return total;
    }

    /**
     * @param someTeam Either the bowling team line-up or the batting team line-up
     * @return The bowling team if the batting team is given; or the batting team if the bowling team is given
     * @throws java.util.NoSuchElementException An invalid line-up was given
     */
    public LineUp otherTeam(LineUp someTeam) {
        return this.teams().stream().filter(t -> !t.equals(someTeam)).findFirst().orElseThrow();
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
}
