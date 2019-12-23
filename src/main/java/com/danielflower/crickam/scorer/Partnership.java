package com.danielflower.crickam.scorer;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class Partnership {
    private final Balls balls;
	public final Balls firstBatterContribution;
	public final Balls secondBatterContribution;
	public final Instant endTime;
	private final FixedData data;

    public Instant endTime() {
        return endTime;
    }

    private static class FixedData {
        private final BatsmanInnings firstBatter;
        private final BatsmanInnings secondBatter;
        private final int wicketNumber;
        private final Instant startTime;
        private FixedData(BatsmanInnings firstBatter, BatsmanInnings secondBatter, int wicketNumber, Instant startTime) {
            this.firstBatter = requireNonNull(firstBatter);
            this.secondBatter = requireNonNull(secondBatter);
            this.wicketNumber = wicketNumber;
            this.startTime = requireNonNull(startTime);
        }
    }

    public static Partnership newPartnership(int numberInInnings, BatsmanInnings first, BatsmanInnings second) {
        FixedData data = new FixedData(first, second, numberInInnings, Instant.now());
        return new Partnership(data, new Balls(), new Balls(), new Balls(), null);
    }

    public final BatsmanInnings firstBatter() {
        return data.firstBatter;
    }

    public final BatsmanInnings secondBatter() {
        return data.secondBatter;
    }

    public Balls balls() {
        return balls;
    }

    public int wicketNumber() {
	    return data.wicketNumber;
    }
    public Instant startTime() {
	    return data.startTime;
    }

    Partnership(FixedData data, Balls balls, Balls firstBatterContribution, Balls secondBatterContribution, Instant endTime) {
        this.data = requireNonNull(data);
        this.balls = requireNonNull(balls);
        this.firstBatterContribution = requireNonNull(firstBatterContribution);
        this.secondBatterContribution = requireNonNull(secondBatterContribution);
        this.endTime = endTime;
    }

	public int totalRuns() {
		return balls().score().totalRuns();
	}

    public Balls firstBatterContribution() {
        return firstBatterContribution;
    }
    public Balls secondBatterContribution() {
        return secondBatterContribution;
    }

    public Partnership onBall(Ball ball) {
        Balls balls = this.balls.add(ball);
        Balls firstBatterContribution = ball.getStriker().isSameInnings(firstBatter()) ? this.firstBatterContribution.add(ball) : this.firstBatterContribution;
        Balls secondBatterContribution = ball.getStriker().isSameInnings(secondBatter()) ? this.secondBatterContribution.add(ball) : this.secondBatterContribution;
        Instant endTime = ball.dismissal().isPresent() ? ball.getDateCompleted() : null;
        return new Partnership(data, balls, firstBatterContribution, secondBatterContribution, endTime);
    }
}


