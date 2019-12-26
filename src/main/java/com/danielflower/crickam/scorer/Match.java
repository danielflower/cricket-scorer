package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsStartingEvent;

import java.time.Instant;
import java.util.Optional;

public final class Match {

    /**
     * Data in a match that never changes throughout the match. This exists due to the fact that because matches are immutable,
     * many match objects will be created during a match which can use a lot of memory. This allows less duplication of fields.
     */
    private static class FixedData {

        private final String matchID;
        private final Series series;
        private final Instant startTime;
        private final ImmutableList<LineUp> teams;
        private final MatchType matchType;
        private final int inningsPerTeam;
        private final Integer oversPerInnings;
        private final Venue venue;
        private final int numberOfScheduledDays;
        private final Integer ballsPerInnings;
        private FixedData(String matchID, Series series, Instant startTime, ImmutableList<LineUp> teams, MatchType matchType, int inningsPerTeam, Integer oversPerInnings, Venue venue, int numberOfScheduledDays, Integer ballsPerInnings) {
            this.matchID = matchID;
            this.series = series;
            this.startTime = startTime;
            this.teams = teams;
            this.matchType = matchType;
            this.inningsPerTeam = inningsPerTeam;
            this.oversPerInnings = oversPerInnings;
            this.venue = venue;
            this.numberOfScheduledDays = numberOfScheduledDays;
            this.ballsPerInnings = ballsPerInnings;
        }

    }
    private final FixedData data;

    private final ImmutableList<Innings> inningsList;
    private Match(FixedData data, ImmutableList<Innings> inningsList) {
        this.data = data;
        this.inningsList = inningsList;
    }

    Match(String matchID, Series series, Instant startTime, ImmutableList<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, Integer oversPerInnings, int numberOfScheduledDays, Integer ballsPerInnings, Venue venue, ImmutableList<Innings> inningsList) {
        this(new FixedData(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, venue, numberOfScheduledDays, ballsPerInnings), inningsList);
    }


    public Optional<Innings> currentInnings() {
        return inningsList.last();
    }


    Match onEvent(MatchEvent event) {

        if (event instanceof BallCompletedEvent && currentInnings().isEmpty()) {
            throw new IllegalStateException("Cannot process a ball when there is no active innings");
        }

        ImmutableList<Innings> newInningsList = inningsList;
        if (event instanceof InningsStartingEvent) {
            InningsStartingEvent ise = (InningsStartingEvent) event;
            newInningsList = inningsList.add(Innings.newInnings(this, ise.battingTeam(), ise.bowlingTeam(), ise.openers(), inningsList.size() + 1, Instant.now(), data.ballsPerInnings));
        } else {
            Optional<Innings> lastInnings = inningsList.last();
            if (lastInnings.isPresent()) {
                Innings i = lastInnings.get().onEvent(event);
                newInningsList = newInningsList.removeLast().copy().add(i);
            }
        }

        return new Match(data, newInningsList);
    }


    public String matchID() {
        return data.matchID;
    }

    public Series series() {
        return data.series;
    }

    public Instant startTime() {
        return data.startTime;
    }

    public ImmutableList<LineUp> teams() {
        return data.teams;
    }

    /**
     * @return The number of scheduled balls per innings, or -1 if there is no limit.
     */
    public Optional<Integer> ballsPerInnings() {
        return Optional.ofNullable(data.ballsPerInnings);
    }

    /**
     * @return The number of scheduled balls per innings, or -1 if there is no limit.
     */
    public Optional<Integer> oversPerInnings() {
        return Optional.ofNullable(data.oversPerInnings);
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

    public Venue venue() {
        return data.venue;
    }

    /**
     * @return The number of days this match goes for. Generally 1 for limited overs matches, and 5 for test matches.
     */
    public int numberOfScheduledDays() {
        return data.numberOfScheduledDays;
    }
}
