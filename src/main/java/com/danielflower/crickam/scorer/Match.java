package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.Optional;

public class Match {

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
        private final int numberOfInningsPerTeam;
        private final int oversPerInnings;
        private final Venue venue;
        private final int numberOfScheduledDays;
        private FixedData(String matchID, Series series, Instant startTime, ImmutableList<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, int oversPerInnings, Venue venue, int numberOfScheduledDays) {
            this.matchID = matchID;
            this.series = series;
            this.startTime = startTime;
            this.teams = teams;
            this.matchType = matchType;
            this.numberOfInningsPerTeam = numberOfInningsPerTeam;
            this.oversPerInnings = oversPerInnings;
            this.venue = venue;
            this.numberOfScheduledDays = numberOfScheduledDays;
        }
    }

    private final FixedData data;
    private final Balls balls;
    private final ImmutableList<Innings> inningsList;

    private Match(FixedData data, Balls balls, ImmutableList<Innings> inningsList) {
        this.data = data;
        this.balls = balls;
        this.inningsList = inningsList;
    }

    Match(String matchID, Series series, Instant startTime, ImmutableList<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, int oversPerInnings, int numberOfScheduledDays, Venue venue, Balls balls, ImmutableList<Innings> inningsList) {
        this(new FixedData(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, venue, numberOfScheduledDays), balls, inningsList);
    }


    public Optional<Innings> currentInnings() {
        return inningsList.last();
    }


    Match onEvent(MatchEvent event) {

        Balls newBalls = event instanceof BallAtCompletion ? balls.add((BallAtCompletion) event) : balls;

        ImmutableList<Innings> newInningsList = inningsList;
        if (event instanceof InningsStartingEvent) {
            InningsStartingEvent ise = (InningsStartingEvent) event;
            newInningsList = inningsList.add(Innings.newInnings(this, ise.battingTeam(), ise.bowlingTeam(), ise.openers(), inningsList.size() + 1, Instant.now(), oversPerInnings()));
        } else {
            Optional<Innings> lastInnings = inningsList.last();
            if (lastInnings.isPresent()) {
                Innings i = lastInnings.get();
                i.onEvent(event);
            }
        }

        return new Match(data, newBalls, newInningsList);
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

    public MatchType matchType() {
        return data.matchType;
    }

    public int numberOfInningsPerTeam() {
        return data.numberOfInningsPerTeam;
    }

    public int oversPerInnings() {
        return data.oversPerInnings;
    }

    public Venue venue() {
        return data.venue;
    }

    public int numberOfScheduledDays() {
        return data.numberOfScheduledDays;
    }
}


