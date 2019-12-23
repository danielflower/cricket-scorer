package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public class BatsmanInnings {
    private final Player player;
    private final Balls balls;
    private final int numberCameIn;
	public final Instant inningsStartTime;
	public final Instant inningsEndTime;

	public Player getPlayer() {
        return player;
    }
	private final Optional<Dismissal> dismissal;

    public int getNumberCameIn() {
        return numberCameIn;
    }

    public Balls getBalls() {
        return balls;
    }

    BatsmanInnings(Player player, Balls balls, int numberCameIn, Instant inningsStartTime, Instant inningsEndTime, Optional<Dismissal> dismissal) {
        this.player = player;
        this.balls = balls;
        this.numberCameIn = numberCameIn;
	    this.inningsStartTime = inningsStartTime;
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }

    static BatsmanInnings newInnings(Player player, int numberCameIn) {
        return new BatsmanInnings(player, new Balls(), numberCameIn, Instant.now(), null, Optional.empty());
    }
    public boolean isSameInnings(BatsmanInnings other) {
        return getPlayer().equals(other.getPlayer());
    }

    /**
     * @return The 1-based index of this batter (e.g. the first opener returns 1)
     */
    public int numberCameIn() {
        return numberCameIn;
    }

    public int runs() {
        return balls.score().scoredFromBat();
    }

	public Optional<Dismissal> getDismissal() {
		return dismissal;
	}

	@Override
	public String toString() {
		return player.familyName() + " (" + balls.score().scoredFromBat() + " runs)";
	}
}