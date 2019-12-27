package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * The innings of a batter at a specific point in time.
 */
public final class BatterInnings {
    private final Player player;
    private final Balls balls;
    private final int numberCameIn;
	private final Instant inningsStartTime;
	private final Instant inningsEndTime;
	private final Dismissal dismissal;

    BatterInnings(Player player, Balls balls, int numberCameIn, Instant inningsStartTime, Instant inningsEndTime, Dismissal dismissal) {
        this.player = requireNonNull(player);
        this.balls = requireNonNull(balls);
        this.numberCameIn = numberCameIn;
        this.inningsStartTime = requireNonNull(inningsStartTime);
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }

    static BatterInnings newInnings(Player player, int numberCameIn) {
        return new BatterInnings(player, new Balls(), numberCameIn, Instant.now(), null, null);
    }

    boolean isSameInnings(BatterInnings other) {
        return player().equals(other.player());
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
     * @return How this innings was ended (if the player is out or retired)
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
     * @return True if the batter is out or retired
     */
    public boolean isOut() {
        return dismissal != null;
    }

    /**
     * @return True if the batter is not out
     */
    public boolean isNotOut() {
        return dismissal == null;
    }

    /**
     * @return The batter's score.
     */
    public Score score() {
        return balls.score();
    }

    @Override
	public String toString() {
		return player.familyName() + " - " + score().batterRuns() + " (" + score().validDeliveries() + "b)";
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

    BatterInnings onBall(Ball ball) {
        Instant endTime = this.inningsEndTime;
        Dismissal dismissal = null;
        boolean somethingChanged = false;
        if (ball.dismissal().isPresent() && ball.dismissal().get().batter().equals(this.player)) {
            endTime = ball.dateCompleted().orElse(null);
            dismissal = ball.dismissal().get();
            somethingChanged = true;
        }
        Balls newBalls = this.balls;
        if (ball.striker().equals(this.player)) {
            newBalls = newBalls.add(ball);
            somethingChanged = true;
        }
        return somethingChanged ? new BatterInnings(player, newBalls, numberCameIn, inningsStartTime, endTime, dismissal) : this;
    }
}