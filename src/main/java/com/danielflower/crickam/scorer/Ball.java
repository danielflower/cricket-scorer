package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class Ball {

    private final BallCompletedEvent e;

    Ball(BallCompletedEvent event) {
        this.e = requireNonNull(event, "event");
    }

    /**
     * @return The 0-indexed number that this ball is in the match (including invalid deliveries and dead balls)
     */
    public int numberInMatch() {
        return e.numberInMatch();
    }

	public Player striker() {
        return e.striker();
    }

    public Player nonStriker() {
        return e.nonStriker();
    }

    public Player bowler() {
        return e.bowler();
    }

    public int numberInOver() {
        return e.numberInOver();
    }

    public int overNumber() {
        return e.overNumber();
    }

    public Optional<Delivery> deliveryType() {
        return e.delivery();
    }


    public Score score() {
        return e.runsScored();
    }

    public boolean playersCrossed() {
        return e.playersCrossed();
    }

    public Optional<Dismissal> dismissal() {
        return e.dismissal();
    }

    /**
     * @return True if this is a valid, or legal delivery (i.e. a ball that was not a wide or no-ball)
     */
    public boolean isValid() {
        return score().validDeliveries() > 0;
    }

    public Optional<Swing> batSwing() {
        return e.swing();
    }

    public Optional<Trajectory> trajectoryAtImpact() {
        return e.trajectoryAtImpact();
    }

    public Optional<Player> fielder() {
        return e.fielder();
    }

	public Optional<Instant> dateCompleted() {
		return e.time();
	}

    /**
     * Gets the number of the this ball as a string in the format <em>over.ball</em>, for example &quot;0.1&quot;
     *
     * @return The number of the last ball
     */
	public String overDotBallString() {
        return overNumber() + "." + numberInOver();
    }
}
