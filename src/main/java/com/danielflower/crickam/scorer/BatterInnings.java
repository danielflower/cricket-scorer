package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class BatterInnings {
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

    public int runs() {
        return balls.score().batterRuns();
    }

	public Optional<Dismissal> dismissal() {
		return Optional.ofNullable(dismissal);
	}

    public Balls balls() {
        return balls;
    }

    public Player player() {
        return player;
    }

    public Instant inningsStartTime() {
        return inningsStartTime;
    }

    public Optional<Instant> inningsEndTime() {
        return Optional.ofNullable(inningsEndTime);
    }

    public boolean isOut() {
        return dismissal != null;
    }
    public boolean isNotOut() {
        return dismissal == null;
    }

    @Override
	public String toString() {
		return player.familyName() + " (" + balls.score().batterRuns() + " runs)";
	}

    BatterInnings onBall(Ball ball) {
        Instant endTime = this.inningsEndTime;
        Dismissal dismissal = null;
        boolean somethingChanged = false;
        if (ball.dismissal().isPresent() && ball.dismissal().get().batter().equals(this.player)) {
            endTime = ball.getDateCompleted();
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