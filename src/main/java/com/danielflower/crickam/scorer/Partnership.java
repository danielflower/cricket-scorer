package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Date;

public class Partnership {
    private final BatsmanInnings firstBatter;
    private final BatsmanInnings secondBatter;
	public final int wicketNumber;
    private Balls balls;
	public final Balls firstBatterContribution;
	public final Balls secondBatterContribution;
	public final Instant startTime;
	public Date endTime;
    public BatsmanInnings getFirstBatter() {
        return firstBatter;
    }

    public BatsmanInnings getSecondBatter() {
        return secondBatter;
    }

    public Balls getBalls() {
        return balls;
    }

    public Partnership(int wicketNumber, BatsmanInnings firstBatter, BatsmanInnings secondBatter, Instant startTime) {
	    Guard.notNull("firstBatter", firstBatter);
	    Guard.notNull("secondBatter", secondBatter);
	    Guard.notNull("startTime", startTime);
	    this.wicketNumber = wicketNumber;
	    this.startTime = startTime;
	    this.firstBatter = firstBatter;
        this.secondBatter = secondBatter;
        this.balls = new Balls();
        this.firstBatterContribution = new Balls();
        this.secondBatterContribution = new Balls();
    }

    public void addBall(BallAtCompletion ball) {
        balls = balls.add(ball);
        if (ball.getStriker() == firstBatter) {
        	firstBatterContribution.add(ball);
        } else if (ball.getStriker() == secondBatter) {
        	secondBatterContribution.add(ball);
        } else {
	        throw new RuntimeException("I thought " + firstBatter + " and " + secondBatter + " were batting but the ball is for " + ball.getStriker());
        }
	    endTime = ball.getDateCompleted();
    }

	public int totalRuns() {
		return getBalls().score().totalRuns();
	}
}


