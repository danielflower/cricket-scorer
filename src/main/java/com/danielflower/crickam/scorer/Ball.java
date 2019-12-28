package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class Ball {
    private final Player striker;
    private final Player nonStriker;
    private final Player bowler;
    private final int overNumber;
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

    Ball(int id, Player striker, Player nonStriker, int overNumber, int numberInOver, Player bowler,
         Delivery delivery, Swing swing, Trajectory trajectoryAtImpact,
         Score score, Dismissal dismissal, boolean playersCrossed, Player fielder, Instant dateCompleted) {
        this.id = id;
        this.overNumber = overNumber;
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
        this.dateCompleted = dateCompleted;
    }

    public int id() {
        return id;
    }

	public Player striker() {
        return striker;
    }

    public Player nonStriker() {
        return nonStriker;
    }

    public Player bowler() {
        return bowler;
    }

    public int numberInOver() {
        return numberInOver;
    }

    public int overNumber() {
        return overNumber;
    }

    public Optional<Delivery> delivery() {
        return Optional.ofNullable(delivery);
    }

    public Optional<Swing> batterSwing() {
        return Optional.ofNullable(swing);
    }

    public Score score() {
        return score;
    }

    public boolean playersCrossed() {
        return playersCrossed;
    }

    public Optional<Dismissal> dismissal() {
        return Optional.ofNullable(dismissal);
    }

    /**
     * @return True if this is a valid, or legal delivery (i.e. a ball that was not a wide or no-ball)
     */
    public boolean isValid() {
        return score().validDeliveries() > 0;
    }

    public Swing batSwing() {
        return swing;
    }

    public Optional<Trajectory> trajectoryAtImpact() {
        return Optional.ofNullable(trajectoryAtImpact);
    }

    public Optional<Player> fielder() {
        return Optional.ofNullable(fielder);
    }

	public Optional<Instant> dateCompleted() {
		return Optional.ofNullable(dateCompleted);
	}

    /**
     * Gets the number of the this ball as a string in the format <em>over.ball</em>, for example &quot;0.1&quot;
     *
     * @return The number of the last ball
     */
	public String overDotBallString() {
        return overNumber + "." + numberInOver;
    }
}
