package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.List;

public class MatchBuilder {
    private String matchID;
    private Series series;
    private Instant startTime;
    private List<LineUp> teams;
    private MatchType matchType;
    private int numberOfInningsPerTeam;
    private int oversPerInnings;
    private int numberOfScheduledDays;
    private Venue venue;

    public MatchBuilder withMatchID(String matchID) {
        this.matchID = matchID;
        return this;
    }

    public MatchBuilder withSeries(Series series) {
        this.series = series;
        return this;
    }

    public MatchBuilder withStartTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public MatchBuilder withTeams(List<LineUp> teams) {
        this.teams = teams;
        return this;
    }

    public MatchBuilder withMatchType(MatchType matchType) {
        this.matchType = matchType;
        return this;
    }

    public MatchBuilder withNumberOfInningsPerTeam(int numberOfInningsPerTeam) {
        this.numberOfInningsPerTeam = numberOfInningsPerTeam;
        return this;
    }

    public MatchBuilder withOversPerInnings(int oversPerInnings) {
        this.oversPerInnings = oversPerInnings;
        return this;
    }

    public MatchBuilder withNumberOfScheduledDays(int numberOfScheduledDays) {
        this.numberOfScheduledDays = numberOfScheduledDays;
        return this;
    }

    public MatchBuilder withVenue(Venue venue) {
        this.venue = venue;
        return this;
    }

    public Match build() {
        return new Match(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, numberOfScheduledDays, venue, new Balls(), new ImmutableList<>());
    }
}