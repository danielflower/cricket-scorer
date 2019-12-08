package com.danielflower.crickam.scorer;

import java.util.Date;
import java.util.Optional;

public class BatsmanInnings {
    private final Player player;
	public final Score teamScorecardAtStartOfInnings;
    private Balls balls = new Balls();
    private final int numberCameIn;
	public final Date inningsStartTime;
	public Date inningsEndTime;

	public Player getPlayer() {
        return player;
    }
	private Optional<Dismissal> dismissal = Optional.empty();

    public int getNumberCameIn() {
        return numberCameIn;
    }

    public Balls getBalls() {
        return balls;
    }

    public BatsmanInnings(Player player, Balls ballsSoFarInInnings, int numberCameIn, Date inningsStartTime) {
        this.player = player;
	    this.teamScorecardAtStartOfInnings = ballsSoFarInInnings.score();
        this.numberCameIn = numberCameIn;
	    this.inningsStartTime = inningsStartTime;
    }

    public void addBall(BallAtCompletion ball) {
        balls = balls.add(ball);
    }

    public void setDismissal(Dismissal dismissal, Date time) {
    	this.inningsEndTime = time;
    	this.dismissal = Optional.ofNullable(dismissal);
    }
    public void undoDismissal() {
    	setDismissal(null, null);
    }

	public int runs() {
        return balls.score().scored;
    }

	public Optional<Dismissal> getDismissal() {
		return dismissal;
	}

	@Override
	public String toString() {
		return player.familyName + " (" + balls.score().scored + " runs)";
	}
}