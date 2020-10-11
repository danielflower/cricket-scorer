package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.BatterInningsCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The innings of a batter at a specific point in time.
 */
@Immutable
public final class BatterInnings {

    private final BattingState state;
    private final Player player;
    private final Balls balls;
    private final int numberCameIn;
    private final Instant inningsStartTime;
    private final Instant inningsEndTime;
    private final Dismissal dismissal;

    private BatterInnings(BattingState state, Player player, Balls balls, @Nonnegative int numberCameIn, @Nullable Instant inningsStartTime, @Nullable Instant inningsEndTime, @Nullable Dismissal dismissal) {
        this.state = requireNonNull(state, "state");
        this.player = requireNonNull(player, "player");
        this.balls = requireNonNull(balls, "balls");
        this.numberCameIn = numberCameIn;
        this.inningsStartTime = inningsStartTime;
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }

    @Nonnull
    static BatterInnings newInnings(Player player, int numberCameIn, @Nullable Instant startTime) {
        return new BatterInnings(BattingState.IN_PROGRESS, player, new Balls(), numberCameIn, startTime, null, null);
    }

    public static boolean sameBatter(@Nullable BatterInnings one, @Nullable BatterInnings two) {
        if (one == null || two == null) {
            return false;
        }
        return one.player().samePlayer(two.player());
    }

    boolean sameInnings(@Nonnull BatterInnings other) {
        return player().samePlayer(other.player());
    }

    boolean sameInnings(Player other) {
        return player().samePlayer(other);
    }

    /**
     * @return The state that this innings is in
     */
    public @Nonnull BattingState state() {
        return state;
    }

    /**
     * @return The 1-based index of this batter (e.g. the first opener returns 1)
     */
    public @Nonnegative int numberCameIn() {
        return numberCameIn;
    }

    /**
     * @return A shortcut for <code>this.balls().score().batterRuns()</code>
     */
    public @Nonnegative int runs() {
        return balls.score().batterRuns();
    }

    /**
     * @return How this innings was ended, if it was ended in dismissal
     */
    public @Nullable Dismissal dismissal() {
        return dismissal;
    }

    /**
     * @return The balls faced in this innings. Use this to access the current score of the innings with {@link Balls#score()}
     */
    public @Nonnull Balls balls() {
        return balls;
    }

    /**
     * @return The batter
     */
    public @Nonnull Player player() {
        return player;
    }

    /**
     * @return The time the innings started
     */
    public @Nullable Instant inningsStartTime() {
        return inningsStartTime;
    }

    /**
     * @return The time the innings ended, if it has ended
     */
    public @Nullable Instant inningsEndTime() {
        return inningsEndTime;
    }

    /**
     * @return The batter's score.
     */
    public @Nonnull Score score() {
        return balls.score();
    }

    @Override
    public String toString() {
        String notout = state != BattingState.DISMISSED ? "*" : "";
        return player.scorecardName() + " - " + score().batterRuns() + notout + " (" + score().validDeliveries() + "b)";
    }

    @Override
    public boolean equals(@Nullable Object o) {
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

    public @Nonnull BatterInnings onEvent(MatchEvent event) {
        boolean somethingChanged = false;
        Instant endTime = this.inningsEndTime;
        Dismissal dismissal = null;
        Balls newBalls = this.balls;
        BattingState newState = this.state;

        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            if (newState != BattingState.IN_PROGRESS) {
                throw new IllegalStateException("The innings of " + player + " was " + newState + " but received ball " + ball);
            }
            if (ball.striker().samePlayer(this.player)) {
                somethingChanged = true;
                newBalls = newBalls.add(ball);
            }
        } else if (event instanceof BatterInningsCompletedEvent) {
            BatterInningsCompletedEvent e = (BatterInningsCompletedEvent) event;
            if (sameInnings(e.batter())) {
                somethingChanged = true;
                newState = e.reason();
                endTime = e.time();
                dismissal = e.dismissal();
            }
        } else if (event instanceof InningsCompletedEvent) {
            if (newState == BattingState.IN_PROGRESS) {
                somethingChanged = true;
                newState = BattingState.INNINGS_ENDED;
                endTime = event.time();
            }
        }
        return somethingChanged ? new BatterInnings(newState, player, newBalls, numberCameIn, inningsStartTime, endTime, dismissal) : this;
    }
}