package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsStartingEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import java.time.Instant;
import java.util.Optional;

public final class Match {

    private final MatchStartingEvent data;

    private final ImmutableList<Innings> inningsList;

    Match(MatchStartingEvent data, ImmutableList<Innings> inningsList) {
        this.data = data;
        this.inningsList = inningsList;
    }

    public Optional<Innings> currentInnings() {
        return inningsList.last();
    }


    public String matchID() {
        return data.matchID();
    }

    public Optional<Series> series() {
        return data.series();
    }

    public Instant time() {
        return data.time();
    }

    public Optional<Instant> scheduledStartTime() {
        return data.scheduledStartTime();
    }
    public ImmutableList<LineUp> teams() {
        return data.teams();
    }

    /**
     * @return The number of scheduled balls per innings, or empty if there is no limit.
     */
    public Optional<Integer> ballsPerInnings() {
        return data.ballsPerInnings();
    }

    /**
     * @return The number of scheduled balls per innings, or -1 if there is no limit.
     */
    public Optional<Integer> oversPerInnings() {
        return data.oversPerInnings();
    }

    public MatchType matchType() {
        return data.matchType();
    }

    /**
     * @return The number of innings per team. Generally 1 for limited overs matches and 2 for first class / test matches.
     */
    public int numberOfInningsPerTeam() {
        return data.inningsPerTeam();
    }

    public Optional<Venue> venue() {
        return data.venue();
    }

    /**
     * @return The number of days this match goes for. Generally 1 for limited overs matches, and 5 for test matches.
     */
    public int numberOfScheduledDays() {
        return data.numberOfScheduledDays();
    }

    Match onEvent(MatchEvent event) {

        if (event instanceof BallCompletedEvent && currentInnings().isEmpty()) {
            throw new IllegalStateException("Cannot process a ball when there is no active innings");
        }

        ImmutableList<Innings> newInningsList = inningsList;
        if (event instanceof InningsStartingEvent) {
            InningsStartingEvent ise = (InningsStartingEvent) event;
            Integer scheduledBalls = data.ballsPerInnings().orElse(null);
            newInningsList = inningsList.add(Innings.newInnings(this, ise.battingTeam(), ise.bowlingTeam(), ise.openers(), inningsList.size() + 1, Instant.now(), scheduledBalls));
        } else {
            Optional<Innings> lastInnings = inningsList.last();
            if (lastInnings.isPresent()) {
                Innings i = lastInnings.get().onEvent(event);
                newInningsList = newInningsList.removeLast().copy().add(i);
            }
        }

        return new Match(data, newInningsList);
    }
}
