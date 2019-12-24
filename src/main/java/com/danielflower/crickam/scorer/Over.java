package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Objects;

public class Over {
	private final int numberInInnings;
	private final BatsmanInnings striker;
	private final BatsmanInnings nonStriker;
	private final Balls balls;
	private final BowlingSpell bowlingSpell;
	private final int ballsInOver;
	private final Instant startTime;

    static Over newOver(int numberInInnings, BatsmanInnings striker, BatsmanInnings nonStriker, BowlingSpell spell, int ballsInOver, Instant startTime) {
        return new Over(numberInInnings, striker, nonStriker, new Balls(), spell, ballsInOver, startTime);
    }

    public Over onBall(Ball ball) {
        BatsmanInnings striker = ball.getPlayersCrossed() ? this.nonStriker : this.striker;
        BatsmanInnings nonStriker = ball.getPlayersCrossed() ? this.striker : this.nonStriker;
        return new Over(numberInInnings, striker, nonStriker, balls.add(ball), bowlingSpell, ballsInOver, startTime);
    }

    public BatsmanInnings striker() {
		return striker;
	}

	public BatsmanInnings nonStriker() {
		return nonStriker;
	}

	public BowlingSpell spell() {
		return bowlingSpell;
	}

    /**
     * @return The zero-indexed number of this over (e.g. the first over in an innings returns 0)
     */
	public int numberInInnings() {
		return numberInInnings;
	}

	public Balls balls() {
		return balls;
	}

	private Over(int numberInInnings, BatsmanInnings striker, BatsmanInnings nonStriker, Balls balls, BowlingSpell bowlingSpell, int ballsInOver, Instant startTime) {
		this.numberInInnings = numberInInnings;
        this.striker = Objects.requireNonNull(striker);
        this.nonStriker = Objects.requireNonNull(nonStriker);
        this.balls = Objects.requireNonNull(balls);
        this.bowlingSpell = Objects.requireNonNull(bowlingSpell);
        this.ballsInOver = ballsInOver;
        this.startTime = startTime;
    }

	public int runs() {
		return balls.score().totalRuns();
	}

    /**
     * @return True if no runs have been scored in this over. If not {@link #isComplete()} then returns true if there
     * are no runs so far.
     */
	public boolean isMaiden() {
		return runs() == 0;
	}

	public int remainingBalls() {
	    return ballsInOver - legalBalls();
    }

	public int legalBalls() {
		return (int) balls.list().stream().filter(BallAtCompletion::isLegal).count();
	}

    public boolean isComplete() {
        return legalBalls() >= 6;
    }

    @Override
    public String toString() {
        return "Over{" +
                "numberInInnings=" + numberInInnings +
                '}';
    }
}


