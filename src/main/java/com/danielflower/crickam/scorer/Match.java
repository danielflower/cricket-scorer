package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.UUID;

import static com.danielflower.crickam.scorer.ImmutableList.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * A lovely game of cricket between two teams.
 * <p>To create a new match, pass a builder from {@link MatchEvents#matchStarting(int, Integer)} to
 * {@link MatchControl#newMatch(MatchStartingEvent)}</p>
 */
@Immutable
public final class Match {

    public enum State {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    private final FixedData data;
    private final State state;
    private final MatchResult result;

    private final ImmutableList<Innings> completedInningsList;
    private final Innings currentInnings;
    private final Balls balls;

    private Match(FixedData data, State state, @Nullable MatchResult result, ImmutableList<Innings> completedInningsList, @Nullable Innings currentInnings, Balls balls) {
        this.data = requireNonNull(data);
        this.state = requireNonNull(state);
        this.result = result;
        this.completedInningsList = requireNonNull(completedInningsList);
        this.currentInnings = currentInnings;
        this.balls = requireNonNull(balls);
    }

    static @Nonnull
    Match newMatch(MatchStartingEvent e) {
        FixedData fd = new FixedData(e.customData(), e.matchID(), e.time(), e.scheduledStartTime(),
            e.teamLineUps(), e.inningsPerTeam(), e.oversPerInnings(),
            e.numberOfScheduledDays(), e.ballsPerInnings(), e.timeZone());
        return new Match(fd, State.NOT_STARTED, null, emptyList(), null, new Balls());
    }

    /**
     * @return A list of every ball bowled in the match
     */
    public @Nonnull
    Balls balls() {
        return balls;
    }

    /**
     * @return The current match state
     */
    public @Nonnull
    State state() {
        return state;
    }

    /**
     * @return The current innings in the match, if an innings is in progress
     * @see #inningsList()
     * @see #currentInnings()
     */
    public @Nullable
    Innings currentInnings() {
        return currentInnings;
    }

    /**
     * @return The innings played in the match that have been completed
     */
    public @Nonnull
    ImmutableList<Innings> completedInningsList() {
        return completedInningsList;
    }

    /**
     * @return All innings in the match, including any in-progress innings
     */
    public @Nonnull
    ImmutableList<Innings> inningsList() {
        return currentInnings == null ? completedInningsList : completedInningsList.add(currentInnings);
    }

    /**
     * @return A unique ID for this match
     */
    public @Nonnull
    UUID id() {
        return data.matchID;
    }

    /**
     * @return The time when the match event was created
     */
    public @Nullable
    Instant time() {
        return data.time;
    }

    /**
     * @return The timezone that the match is played in
     */
    public @Nullable
    TimeZone timeZone() {
        return data.timeZone;
    }

    /**
     * @return The scheduled start time of the match, if known.
     */
    public @Nullable
    Instant scheduledStartTime() {
        return data.scheduledStartTime;
    }

    /**
     * @return The teams playing in this match, in no particular order.
     */
    public @Nonnull
    ImmutableList<LineUp<?>> teams() {
        return data.teams;
    }

    /**
     * @return The number of scheduled balls per innings, or null if there is no limit.
     */
    public @Nullable
    Integer ballsPerInnings() {
        return data.ballsPerInnings;
    }

    /**
     * @return The number of scheduled balls per innings, or -1 if there is no limit.
     */
    public @Nullable
    Integer oversPerInnings() {
        return data.oversPerInnings;
    }

    /**
     * @return The number of innings per team. Generally 1 for limited overs matches and 2 for first class / test matches.
     */
    public @Nonnegative
    int numberOfInningsPerTeam() {
        return data.inningsPerTeam;
    }

    /**
     * @return The number of days this match goes for. Generally 1 for limited overs matches, and 5 for test matches.
     */
    public @Nonnegative
    int numberOfScheduledDays() {
        return data.numberOfScheduledDays;
    }

    /**
     * @return The final result of the match, or null if {@link #state()} is not {@link State#COMPLETED}
     */
    public @Nullable
    MatchResult result() {
        return result;
    }

    /**
     * Guesses the result of a match.
     * <p>Note that on the conclusion of the match, an actual result is determined and accessed by {@link #result()}
     * (and this may return a different value than this method).</p>
     *
     * @return Calculates what the current result should be at the current point in time.
     */
    public @Nonnull
    MatchResult calculateResult() {
        return MatchResult.fromMatch(this);
    }

    /**
     * Returns any data associated with the match.
     *
     * @return Arbitrary data associated with the match.
     */
    public Object customData() {
        return data.customData;
    }

    @Nonnull
    Match onEvent(MatchEvent event) {
        if (event instanceof BallCompletedEvent) {
            Innings inn = currentInnings();
            if (inn == null) throw new IllegalStateException("Cannot process a ball when there is no active innings");
            if (inn.allOut()) throw new IllegalStateException("Cannot process a ball when the batting team is already all out");
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
            newResult = ((MatchCompletedEvent) event).result();
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
     *
     * @param team The team to find the score for
     * @return The score for all innings the team has played in this match
     */
    public Score scoredByTeam(LineUp<?> team) {
        Score total = Score.EMPTY;
        ImmutableList<Innings> list = this.completedInningsList;
        if (currentInnings != null) {
            list = list.add(currentInnings);
        }
        for (Innings innings : list) {
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
    public LineUp<?> otherTeam(LineUp<?> someTeam) {
        return this.teams().stream()
            .filter(t -> !t.equals(someTeam))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("An invalid line-up was given: " + someTeam));
    }

    private static class FixedData {

        private final Object customData;
        private final UUID matchID;
        private final Instant time;
        private final Instant scheduledStartTime;
        private final ImmutableList<LineUp<?>> teams;
        private final int inningsPerTeam;
        private final Integer oversPerInnings;
        private final int numberOfScheduledDays;
        private final Integer ballsPerInnings;
        private final TimeZone timeZone;

        public FixedData(@Nullable Object customData, UUID matchID, @Nullable Instant time, @Nullable Instant scheduledStartTime, ImmutableList<LineUp<?>> teams, @Nonnegative int inningsPerTeam, @Nullable Integer oversPerInnings, @Nonnegative int numberOfScheduledDays, @Nullable Integer ballsPerInnings, @Nullable TimeZone timeZone) {
            this.customData = customData;
            this.matchID = matchID;
            this.time = time;
            this.scheduledStartTime = scheduledStartTime;
            this.teams = teams;
            this.inningsPerTeam = inningsPerTeam;
            this.oversPerInnings = oversPerInnings;
            this.numberOfScheduledDays = numberOfScheduledDays;
            this.ballsPerInnings = ballsPerInnings;
            this.timeZone = timeZone;
        }

    }
}
