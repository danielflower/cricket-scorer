package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.toInteger;

/**
 * A lovely game of cricket between two teams
 */
public final class Match {

    public enum State {
        NOT_STARTED, ABANDONED, IN_PROGRESS, COMPLETED
    }

    private final FixedData data;
    private final State state;
    private final ImmutableList<Innings> inningsList;

    private Match(FixedData data, State state, ImmutableList<Innings> inningsList) {
        this.data = data;
        this.state = state;
        this.inningsList = inningsList;
    }
    static Match newMatch(MatchStartingEvent e) {
        FixedData fd = new FixedData(e.matchID(), e.series().orElse(null), e.time().orElse(null), e.scheduledStartTime().orElse(null),
            e.teams(), e.matchType(), e.inningsPerTeam(), toInteger(e.oversPerInnings()), e.venue().orElse(null),
            e.numberOfScheduledDays(), toInteger(e.ballsPerInnings()));
        return new Match(fd, State.NOT_STARTED, new ImmutableList<>());
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
        public FixedData(String matchID, Series series, Instant time, Instant scheduledStartTime, ImmutableList<LineUp> teams, MatchType matchType, int inningsPerTeam, Integer oversPerInnings, Venue venue, int numberOfScheduledDays, Integer ballsPerInnings) {
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

    Match onEvent(MatchEvent event) {

        if (event instanceof BallCompletedEvent && currentInnings().isEmpty()) {
            throw new IllegalStateException("Cannot process a ball when there is no active innings");
        }
        State newState = this.state;

        ImmutableList<Innings> newInningsList = inningsList;
        if (event instanceof InningsStartingEvent) {
            newState = State.IN_PROGRESS;
            InningsStartingEvent ise = (InningsStartingEvent) event;
            Integer scheduledBalls = data.ballsPerInnings;
            newInningsList = inningsList.add(Innings.newInnings(this, ise.battingTeam(), ise.bowlingTeam(), ise.openers(), inningsList.size() + 1, Instant.now(), scheduledBalls));
        } else if (event instanceof MatchCompletedEvent) {
            // don't pass to the innings
            newState = State.COMPLETED;
        } else {
            Optional<Innings> lastInnings = inningsList.last();
            if (lastInnings.isPresent()) {
                Innings i = lastInnings.get().onEvent(event);
                newInningsList = newInningsList.removeLast().copy().add(i);
            }
        }

        return new Match(data, newState, newInningsList);
    }
}
