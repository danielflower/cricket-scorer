package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Objects;

/**
 * A set of (normally) 6 balls bowled in an innings.
 * <p>Note that this class is immutable, so rather than having a single over instance that gets balls added to it,
 * there will be multiple over objects generated for a single over in a match- one for each ball in the over plus
 * one before the over starts and one after it finishes.</p>
 */
public final class Over {
	private final int numberInInnings;
	private final Player striker;
	private final Player nonStriker;
	private final Balls balls;
	private final Player bowler;
	private final int ballsInOver;
	private final Instant startTime;

    static Over newOver(int numberInInnings, Player striker, Player nonStriker, Player bowler, int ballsInOver, Instant startTime) {
        return new Over(numberInInnings, striker, nonStriker, new Balls(), bowler, ballsInOver, startTime);
    }

    Over onBall(Ball ball) {
        Player striker = ball.playersCrossed() ? this.nonStriker : this.striker;
        Player nonStriker = ball.playersCrossed() ? this.striker : this.nonStriker;
        return new Over(numberInInnings, striker, nonStriker, balls.add(ball), bowler, ballsInOver, startTime);
    }

    /**
     * @return The current batter that is going to face the next ball
     */
    public Player striker() {
		return striker;
	}

    /**
     * @return The batter that is currently at the non-facing end
     */
	public Player nonStriker() {
		return nonStriker;
	}

    /**
     * @return The current bowler of this over.
     */
	public Player bowler() {
		return bowler;
	}

    /**
     * @return The zero-indexed number of this over (e.g. the first over in an innings returns 0)
     */
	public int numberInInnings() {
		return numberInInnings;
	}

    /**
     * @return The balls bowled in this over
     */
	public Balls balls() {
		return balls;
	}

    /**
     * @return The runs scored in this over
     */
	public Score score() {
	    return balls.score();
    }

	private Over(int numberInInnings, Player striker, Player nonStriker, Balls balls, Player bowler, int ballsInOver, Instant startTime) {
		this.numberInInnings = numberInInnings;
        this.striker = Objects.requireNonNull(striker);
        this.nonStriker = Objects.requireNonNull(nonStriker);
        if (striker.equals(nonStriker)) {
            throw new IllegalStateException(striker + " has been set as both striker and non-striker for over " + numberInInnings);
        }
        this.balls = Objects.requireNonNull(balls);
        this.bowler = Objects.requireNonNull(bowler);
        this.ballsInOver = ballsInOver;
        this.startTime = startTime;
    }

    /**
     * @return The number of runs gained by the team in this over (including extras).
     */
	public int runs() {
		return balls.score().teamRuns();
	}

    /**
     * @return True if no runs have been scored in this over. If not {@link #isComplete()} then returns false even if there
     * are no runs so far.
     */
	public boolean isMaiden() {
		return isComplete() && runs() == 0;
	}

    /**
     * @return The number of valid balls remaining to be bowled in this over.
     */
	public int remainingBalls() {
	    return ballsInOver - validDeliveries();
    }

    /**
     * @return The number of valid (or legal) balls bowled so far in this over.
     */
	public int validDeliveries() {
		return (int) balls.list().stream().filter(Ball::isValid).count();
	}

    /**
     * @return True if there shouldn't be any more balls bowled in this over. Note that it is still possible to have
     * more balls in this over even if it is complete if there is an umpiring error.
     */
    public boolean isComplete() {
        return validDeliveries() >= ballsInOver;
    }

    @Override
    public String toString() {
        return "Over{" +
                "numberInInnings=" + numberInInnings +
                '}';
    }
}


