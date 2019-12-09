package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Match
{
	public final String matchID;
	public final Series series;
	private final Instant startTime;
    private final List<LineUp> teams;
	public final MatchType matchType;
	public final int numberOfInningsPerTeam;
	public final int oversPerInnings;
    public final Venue venue;
	public final int numberOfScheduledDays;
    private Balls balls = new Balls();
    private final List<Innings> inningsList = new ArrayList<>();
    public Match(String matchID, Series series, Instant startTime, List<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, int oversPerInnings, int numberOfScheduledDays, Venue venue) {
	    this.matchID = matchID;
	    this.series = series;
	    this.startTime = startTime;
        this.teams = teams;
        this.matchType = matchType;
        this.numberOfInningsPerTeam = numberOfInningsPerTeam;
        this.oversPerInnings = oversPerInnings;
        this.numberOfScheduledDays = numberOfScheduledDays;
	    this.venue = venue;
    }

    public Innings newInnings(LineUp battingTeam, LineUp bowlingTeam, List<Player> battingOrder, Date startTime) {
        Innings innings = new Innings(this, battingTeam, bowlingTeam, battingOrder, inningsList.size(), startTime, oversPerInnings);
        inningsList.add(innings);
        return innings;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public List<Innings> getInningsList() {
        return inningsList;
    };

    public List<LineUp> getTeams() {
        return teams;
    }

    public Innings getCurrentInnings() {
        return inningsList.get(inningsList.size() - 1);
    }

    public void addBall(BallAtCompletion ball) {
        balls = balls.add(ball);
        getCurrentInnings().addBall(ball);
    }

}


