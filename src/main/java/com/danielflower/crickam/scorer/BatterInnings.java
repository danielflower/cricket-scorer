package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.BatterInningsEndedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * The innings of a batter at a specific point in time.
 */
public final class BatterInnings implements MatchEventListener<BatterInnings> {

    public enum State {
        /**
         * The innings is in progress
         */
        IN_PROGRESS,

        /**
         * The batter retired due to illness, injury, or some other unavoidable reason, and may bat again.
         */
        RETIRED,

        /**
         * The batter chose to retire, so cannot resume their innings (unless the opposition caption agrees)
         */
        RETIRED_OUT,

        /**
         * The batter was dismissed
         */
        DISMISSED,

        /**
         * The batter was not out when the team's innings ended
         */
        INNINGS_ENDED
    }

    private final State state;
    private final Player player;
    private final Balls balls;
    private final int numberCameIn;
	private final Instant inningsStartTime;
	private final Instant inningsEndTime;
	private final Dismissal dismissal;

    private BatterInnings(State state, Player player, Balls balls, int numberCameIn, Instant inningsStartTime, Instant inningsEndTime, Dismissal dismissal) {
        this.state = state;
        this.player = requireNonNull(player);
        this.balls = requireNonNull(balls);
        this.numberCameIn = numberCameIn;
        this.inningsStartTime = requireNonNull(inningsStartTime);
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }

    static BatterInnings newInnings(Player player, int numberCameIn) {
        return new BatterInnings(State.IN_PROGRESS, player, new Balls(), numberCameIn, Instant.now(), null, null);
    }

    boolean isSameInnings(BatterInnings other) {
        return player().equals(other.player());
    }

    /**
     * @return The state that this innings is in
     */
    public State state() {
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
    public Instant inningsStartTime() {
        return inningsStartTime;
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
        String notout = state != State.DISMISSED ? "*" : "";
		return player.familyName() + " - " + score().batterRuns() + notout + " (" + score().validDeliveries() + "b)";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatterInnings that = (BatterInnings) o;
        return numberCameIn == that.numberCameIn &&
            player.equals(that.player) &&
            balls.equals(that.balls) &&
            inningsStartTime.equals(that.inningsStartTime) &&
            Objects.equals(inningsEndTime, that.inningsEndTime) &&
            Objects.equals(dismissal, that.dismissal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, balls, numberCameIn, inningsStartTime, inningsEndTime, dismissal);
    }

    public BatterInnings onEvent(MatchEvent event) {
        boolean somethingChanged = false;
        Instant endTime = this.inningsEndTime;
        Dismissal dismissal = null;
        Balls newBalls = this.balls;
        State newState = this.state;

        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            if (newState != State.IN_PROGRESS) {
                throw new IllegalStateException("The current batter's innings was " + newState + " but received ball " + ball);
            }

            if (ball.dismissal().isPresent() && ball.dismissal().get().batter().equals(this.player)) {
                endTime = ball.time().orElse(null);
                dismissal = ball.dismissal().get();
                somethingChanged = true;
            }
            if (ball.striker().equals(this.player)) {
                newBalls = newBalls.add(ball);
                somethingChanged = true;
            }
        } else if (event instanceof BatterInningsEndedEvent) {
            BatterInningsEndedEvent e = (BatterInningsEndedEvent) event;
            newState = e.reason();
            endTime = e.time().orElse(null);
            somethingChanged = true;
        }
        return somethingChanged ? new BatterInnings(newState, player, newBalls, numberCameIn, inningsStartTime, endTime, dismissal) : this;
    }
}