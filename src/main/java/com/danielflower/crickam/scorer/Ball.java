package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Ball {
    private final BatsmanInnings striker;
    private final BatsmanInnings nonStriker;
    private final Player bowler;
    private final int numberInOver;
    private final Score score;
    private final boolean playersCrossed;
    private final Dismissal dismissal;
    private final Delivery delivery;
    private final Swing swing;
    private final Trajectory trajectoryAtImpact;
    private final int id;
    private final Player fielder;
	private final Instant dateCompleted;

	public BatsmanInnings getStriker() {
        return striker;
    }

    public BatsmanInnings getNonStriker() {
        return nonStriker;
    }

    public Player bowler() {
        return bowler;
    }

    public int id() {
        return id;
    }

    public int getNumberInOver() {
        return numberInOver;
    }


    public Delivery getDelivery() {
        return delivery;
    }

    public Swing getSwing() {
        return swing;
    }

    public Score score() {
        return score;
    }

    public boolean getPlayersCrossed() {
        return playersCrossed;
    }

    public Optional<Dismissal> dismissal() {
        return Optional.ofNullable(dismissal);
    }

    public boolean isLegal() {
        return score().wides() == 0 && score().noBalls() == 0;
    }

    Ball(int id, BatsmanInnings striker, BatsmanInnings nonStriker, int numberInOver, Player bowler,
         Delivery delivery, Swing swing, Trajectory trajectoryAtImpact,
         Score score, Dismissal dismissal, boolean playersCrossed, Player fielder, Instant dateCompleted) {
        this.id = id;
	    this.fielder = fielder;
	    this.trajectoryAtImpact = trajectoryAtImpact;
	    this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker);
        this.nonStriker = requireNonNull(nonStriker);
	    this.numberInOver = numberInOver;
	    this.delivery = delivery;
	    this.swing = swing;
        this.score = requireNonNull(score);
	    this.dismissal = dismissal;
	    this.playersCrossed = playersCrossed;
        this.dateCompleted = requireNonNull(dateCompleted);
    }

    public Trajectory getTrajectoryAtImpact() {
        return trajectoryAtImpact;
    }

    public Optional<Player> getFielder() {
        return Optional.ofNullable(fielder);
    }

	public Instant getDateCompleted() {
		return dateCompleted;
	}
}
