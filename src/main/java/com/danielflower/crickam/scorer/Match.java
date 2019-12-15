package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    private final ImmutableList<BallAtCompletion> balls;
    private final ImmutableList<Innings> inningsList;
    private final Score score = ScoreBuilder.Empty;


    public Match(String matchID, Series series, Instant startTime, List<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, int oversPerInnings, int numberOfScheduledDays, Venue venue, ImmutableList<BallAtCompletion> balls, ImmutableList<Innings> inningsList) {
	    this.matchID = matchID;
	    this.series = series;
	    this.startTime = startTime;
        this.teams = teams;
        this.matchType = matchType;
        this.numberOfInningsPerTeam = numberOfInningsPerTeam;
        this.oversPerInnings = oversPerInnings;
        this.numberOfScheduledDays = numberOfScheduledDays;
	    this.venue = venue;
        this.balls = balls;
        this.inningsList = inningsList;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public List<LineUp> getTeams() {
        return teams;
    }

    public Optional<Innings> getCurrentInnings() {
        return inningsList.last();
    }

    public Match onEvent(MatchEvent event) {

        ImmutableList<BallAtCompletion> newBalls = event instanceof BallAtCompletion ? balls.add((BallAtCompletion) event) : balls;

        ImmutableList<Innings> newInningsList;
        if (event instanceof InningsStartingEvent) {
            InningsStartingEvent ise = (InningsStartingEvent) event;
            newInningsList = inningsList.add(
                new Innings(this, ise.battingTeam(), ise.bowlingTeam(), ise.battingTeam().players, inningsList.size() + 1, Instant.now(), oversPerInnings)
            );
        } else {
            newInningsList = inningsList;
        }

        return new Match(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, numberOfScheduledDays, venue, newBalls, newInningsList);
    }
}


