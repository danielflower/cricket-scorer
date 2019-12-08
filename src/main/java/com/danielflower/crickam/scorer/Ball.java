package com.danielflower.crickam.scorer;

import java.util.Date;
import java.util.Optional;

public class Ball implements BallAtCompletion {
    private final BatsmanInnings striker;
    private final BatsmanInnings nonStriker;
    private final BowlingSpell bowlingSpell;
    private final int numberInOver;
    private final Score score;
    private final boolean playersCrossed;
    private final Optional<Dismissal> dismissal;
    private final Delivery delivery;
    private final Swing swing;
    private final Trajectory trajectoryAtImpact;
    private final int id;
    private final Optional<Player> fielder;
	private final Date dateCompleted;

	public BatsmanInnings getStriker() {
        return striker;
    }

    public BatsmanInnings getNonStriker() {
        return nonStriker;
    }

    public BowlingSpell getBowlingSpell() {
        return bowlingSpell;
    }

    public int id() {
        return id;
    }

    public int getNumberInOver() {
        return numberInOver;
    }

    @Override
    public BallAtEngagement engage(Delivery delivery, Swing swing, Trajectory ballTrajectory) {
        Guard.notNull("delivery", delivery);
        Guard.notNull("swing", swing);
        Guard.notNull("ballTrajectory", ballTrajectory);
        return new Ball(
                id, this.getStriker(), this.getNonStriker(), this.getNumberInOver(), this.getBowlingSpell(),
                delivery, swing, ballTrajectory, null, null, false, null, null);
    }


    @Override
    public BallAtCompletion complete(Score score, Optional<Dismissal> dismissal, boolean playersCrossed, Optional<Player> fielder, Date dateCompleted) {
        Guard.notNull("dismissal", dismissal);
        return new Ball(
                id, this.getStriker(), this.getNonStriker(), this.getNumberInOver(), this.getBowlingSpell(),
                this.getDelivery(), this.getSwing(), this.getTrajectoryAtImpact(),
                score, dismissal, playersCrossed,
                fielder, dateCompleted);
    }

    @Override
    public Delivery getDelivery() {
        return delivery;
    }

    @Override
    public Swing getSwing() {
        return swing;
    }

    public Score getScore() {
        return score;
    }

    public boolean getPlayersCrossed() {
        return playersCrossed;
    }

    public Optional<Dismissal> getDismissal() {
        return dismissal;
    }

    public boolean isLegal() {
        return getScore().wides == 0 && getScore().noBalls == 0;
    }

    public Ball(int id, BatsmanInnings striker, BatsmanInnings nonStriker, int numberInOver, BowlingSpell bowlingSpell) {
        this(id, striker, nonStriker, numberInOver, bowlingSpell, null, null, null, null, null, false, null, null);
    }

    private Ball(int id, BatsmanInnings striker, BatsmanInnings nonStriker, int numberInOver, BowlingSpell bowlingSpell,
                 Delivery delivery, Swing swing, Trajectory trajectoryAtImpact,
                 Score score, Optional<Dismissal> dismissal, boolean playersCrossed, Optional<Player> fielder, Date dateCompleted) {
	    Guard.notNull("bowlingSpell", bowlingSpell);
	    this.id = id;
	    this.fielder = fielder;
	    this.trajectoryAtImpact = trajectoryAtImpact;
	    this.bowlingSpell = bowlingSpell;
	    this.striker = striker;
	    this.nonStriker = nonStriker;
	    this.numberInOver = numberInOver;
	    this.delivery = delivery;
	    this.swing = swing;
	    this.score = score;
	    this.dismissal = dismissal;
	    this.playersCrossed = playersCrossed;
	    this.dateCompleted = dateCompleted;
    }

    public Trajectory getTrajectoryAtImpact() {
        return trajectoryAtImpact;
    }

    @Override
    public Optional<Player> getFielder() {
        return fielder;
    }

	@Override
	public Date getDateCompleted() {
		return dateCompleted;
	}
}
