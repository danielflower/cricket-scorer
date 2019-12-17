package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.Optional;

public class Match
{
	public final String matchID;
	public final Series series;
	private final Instant startTime;
    private final ImmutableList<LineUp> teams;
	public final MatchType matchType;
	public final int numberOfInningsPerTeam;
	public final int oversPerInnings;
    public final Venue venue;
	public final int numberOfScheduledDays;
    private final Balls balls;
    private final ImmutableList<Innings> inningsList;
    private final Score score = ScoreBuilder.Empty;


    public Match(String matchID, Series series, Instant startTime, ImmutableList<LineUp> teams, MatchType matchType, int numberOfInningsPerTeam, int oversPerInnings, int numberOfScheduledDays, Venue venue, Balls balls, ImmutableList<Innings> inningsList) {
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

    public ImmutableList<LineUp> getTeams() {
        return teams;
    }

    public Optional<Innings> getCurrentInnings() {
        return inningsList.last();
    }

    public Match onEvent(MatchEvent event) {

        Balls newBalls = event instanceof BallAtCompletion ? balls.add((BallAtCompletion) event) : balls;

        ImmutableList<Innings> newInningsList;
        if (event instanceof InningsStartingEvent) {
            InningsStartingEvent ise = (InningsStartingEvent) event;
            BatsmanInnings currentStriker = null;
            BatsmanInnings currentNonStriker = null;
            BowlingSpell currentBowlingSpell = null;
            BowlingSpell currentBowlingSpellOfOtherBowler = null;
            newInningsList = inningsList.add(
                new Innings(this, ise.battingTeam(), ise.bowlingTeam(), new ImmutableList<>(), ise.openers(), inningsList.size() + 1, new ImmutableList<>(), new ImmutableList<>(), Instant.now(), null, new Balls(), currentStriker, currentNonStriker, currentBowlingSpell, currentBowlingSpellOfOtherBowler, new ImmutableList<>(), new ImmutableList<>(), Score.Empty, oversPerInnings)
            );
        } else {
            newInningsList = inningsList;
        }

        return new Match(matchID, series, startTime, teams, matchType, numberOfInningsPerTeam, oversPerInnings, numberOfScheduledDays, venue, newBalls, newInningsList);
    }
}


