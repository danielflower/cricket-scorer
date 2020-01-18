package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.BatterInningsCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * The innings of a batter at a specific point in time.
 */
public final class BatterInnings {

    private final BattingState state;
    private final Player player;
    private final Balls balls;
    private final int numberCameIn;
    private final Instant inningsStartTime;
    private final Instant inningsEndTime;
    private final Dismissal dismissal;

    private BatterInnings(BattingState state, Player player, Balls balls, int numberCameIn, Instant inningsStartTime, Instant inningsEndTime, Dismissal dismissal) {
        this.state = state;
        this.player = requireNonNull(player);
        this.balls = requireNonNull(balls);
        this.numberCameIn = numberCameIn;
        this.inningsStartTime = inningsStartTime;
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }

    static BatterInnings newInnings(Player player, int numberCameIn, Instant startTime) {
        return new BatterInnings(BattingState.IN_PROGRESS, player, new Balls(), numberCameIn, startTime, null, null);
    }

    public static boolean sameBatter(BatterInnings one, BatterInnings two) {
        if (one == null || two == null) {
            return false;
        }
        return one.player().equals(two.player());
    }

    boolean sameInnings(BatterInnings other) {
        return player().equals(other.player());
    }

    boolean sameInnings(Player other) {
        return player().equals(other);
    }

    /**
     * @return The state that this innings is in
     */
    public BattingState state() {
        return state;
    }

    /**
     * @return The 1-based index of this batter (e.g. the first opener returns 1)
     */
    public int numberCameIn() {
        return numberCameIn;
    }

    /**
     * @return A shortcut for <code>this.balls().score().batterRuns()</code>
     */
    public int runs() {
        return balls.score().batterRuns();
    }

    /**
     * @return How this innings was ended, if it was ended in dismissal
     */
    public Optional<Dismissal> dismissal() {
        return Optional.ofNullable(dismissal);
    }

    /**
     * @return The balls faced in this innings. Use this to access the current score of the innings with {@link Balls#score()}
     */
    public Balls balls() {
        return balls;
    }

    /**
     * @return The batter
     */
    public Player player() {
        return player;
    }

    /**
     * @return The time the innings started
     */
    public Optional<Instant> inningsStartTime() {
        return Optional.ofNullable(inningsStartTime);
    }

    /**
     * @return The time the innings ended, if it has ended
     */
    public Optional<Instant> inningsEndTime() {
        return Optional.ofNullable(inningsEndTime);
    }

    /**
     * @return The batter's score.
     */
    public Score score() {
        return balls.score();
    }

    @Override
    public String toString() {
        String notout = state != BattingState.DISMISSED ? "*" : "";
        return player.familyName() + " - " + score().batterRuns() + notout + " (" + score().validDeliveries() + "b)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatterInnings that = (BatterInnings) o;
        return numberCameIn == that.numberCameIn &&
            state == that.state &&
            Objects.equals(player, that.player) &&
            Objects.equals(balls, that.balls) &&
            Objects.equals(inningsStartTime, that.inningsStartTime) &&
            Objects.equals(inningsEndTime, that.inningsEndTime) &&
            Objects.equals(dismissal, that.dismissal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, player, balls, numberCameIn, inningsStartTime, inningsEndTime, dismissal);
    }

    public BatterInnings onEvent(MatchEvent event) {
        boolean somethingChanged = false;
        Instant endTime = this.inningsEndTime;
        Dismissal dismissal = null;
        Balls newBalls = this.balls;
        BattingState newState = this.state;

        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            if (newState != BattingState.IN_PROGRESS) {
                throw new IllegalStateException("The innings of " + player.familyName() + " was " + newState + " but received ball " + ball);
            }
            if (ball.striker().equals(this.player)) {
                somethingChanged = true;
                newBalls = newBalls.add(ball);
            }
        } else if (event instanceof BatterInningsCompletedEvent) {
            BatterInningsCompletedEvent e = (BatterInningsCompletedEvent) event;
            if (sameInnings(e.batter())) {
                somethingChanged = true;
                newState = e.reason();
                endTime = e.time().orElse(null);
                dismissal = e.dismissal().orElse(null);
            }
        } else if (event instanceof InningsCompletedEvent) {
            if (newState == BattingState.IN_PROGRESS) {
                somethingChanged = true;
                newState = BattingState.INNINGS_ENDED;
                endTime = event.time().orElse(null);
            }
        }
        return somethingChanged ? new BatterInnings(newState, player, newBalls, numberCameIn, inningsStartTime, endTime, dismissal) : this;
    }
}