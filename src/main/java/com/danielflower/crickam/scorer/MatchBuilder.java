package com.danielflower.crickam.scorer;

import java.time.Instant;

public final class MatchBuilder {
    private String matchID;
    private Series series;
    private Instant startTime;
    private ImmutableList<LineUp> teams;
    private MatchType matchType;
    private int numberOfInningsPerTeam;
    private Integer oversPerInnings;
    private int numberOfScheduledDays;
    private Venue venue;
    private Integer ballsPerInnings;

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

    public MatchBuilder withTeams(ImmutableList<LineUp> teams) {
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

    public MatchBuilder withOversPerInnings(Integer oversPerInnings) {
        this.oversPerInnings = oversPerInnings;
        return this;
    }

    public MatchBuilder withBallsPerInnings(Integer ballsPerInnings) {
        this.ballsPerInnings = ballsPerInnings;
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
        Integer bpi = this.ballsPerInnings;
        if (bpi == null && oversPerInnings != null) {
            bpi = 6 * oversPerInnings;
        }
        return new Match(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, numberOfScheduledDays, bpi, venue, new ImmutableList<>());
    }
}