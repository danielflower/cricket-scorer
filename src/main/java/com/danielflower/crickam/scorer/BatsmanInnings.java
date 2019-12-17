package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public class BatsmanInnings {
    private final Player player;
	public final Score teamScorecardAtStartOfInnings;
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

    BatsmanInnings(Player player, Balls ballsSoFarInInnings, Balls balls, int numberCameIn, Instant inningsStartTime, Instant inningsEndTime, Optional<Dismissal> dismissal) {
        this.player = player;
	    this.teamScorecardAtStartOfInnings = ballsSoFarInInnings.score();
        this.balls = balls;
        this.numberCameIn = numberCameIn;
	    this.inningsStartTime = inningsStartTime;
        this.inningsEndTime = inningsEndTime;
        this.dismissal = dismissal;
    }


	public int runs() {
        return balls.score().scored;
    }

	public Optional<Dismissal> getDismissal() {
		return dismissal;
	}

	@Override
	public String toString() {
		return player.familyName() + " (" + balls.score().scored + " runs)";
	}
}