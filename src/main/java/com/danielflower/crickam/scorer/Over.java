package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;

/**
 * A set of (normally) 6 balls bowled in an innings.
 * <p>Note that this class is immutable, so rather than having a single over instance that gets balls added to it,
 * there will be multiple over objects generated for a single over in a match- one for each ball in the over plus
 * one before the over starts and one after it finishes.</p>
 */
@Immutable
public final class Over {
	private final int inningsNumber;
	private final int overNumber;
	private final Player striker;
	private final Player nonStriker;
	private final Balls balls;
	private final Player bowler;
	private final int ballsInOver;
	private final Instant startTime;

    private Over(@Nonnegative int inningsNumber, @Nonnegative int overNumber, Player striker, Player nonStriker, Balls balls, Player bowler, @Nonnegative int ballsInOver, @Nullable Instant startTime) {
        this.inningsNumber = inningsNumber;
        this.overNumber = overNumber;
        this.striker = Objects.requireNonNull(striker);
        this.nonStriker = Objects.requireNonNull(nonStriker);
        if (striker.samePlayer(nonStriker)) {
            throw new IllegalStateException(striker + " has been set as both striker and non-striker for over " + overNumber);
        }
        this.balls = Objects.requireNonNull(balls);
        this.bowler = Objects.requireNonNull(bowler);
        this.ballsInOver = ballsInOver;
        this.startTime = startTime;
    }

    static @Nonnull Over newOver(OverStartingEvent e) {
        return new Over(e.inningsNumber(), e.overNumber(), e.striker(), e.nonStriker(), new Balls(), e.bowler(), e.ballsInOver(), e.time());
    }

    public @Nonnull Over onEvent(MatchEvent event) {
        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            Player striker = ball.playersCrossed() ? this.nonStriker : this.striker;
            Player nonStriker = ball.playersCrossed() ? this.striker : this.nonStriker;
            return new Over(inningsNumber, overNumber, striker, nonStriker, balls.add(ball), bowler, ballsInOver, startTime);
        } else {
            return this;
        }
    }

    /**
     * @return The current batter that is going to face the next ball
     */
    public @Nonnull Player striker() {
		return striker;
	}

    /**
     * @return The batter that is currently at the non-facing end
     */
	public @Nonnull Player nonStriker() {
		return nonStriker;
	}

    /**
     * @return The current bowler of this over.
     */
	public @Nonnull Player bowler() {
		return bowler;
	}

    /**
     * @return The zero-indexed number of this over (e.g. the first over in an innings returns 0)
     */
	public @Nonnegative int overNumber() {
		return overNumber;
	}

    /**
     * @return The 1-indexed innings number that this over is in
     */
    public @Nonnegative int inningsNumber() {
        return inningsNumber;
    }

    /**
     * @return The max number of valid deliveries allowed in the over
     */
    public @Nonnegative int ballsInOver() {
        return ballsInOver;
    }

    /**
     * @return The balls bowled in this over
     */
	public @Nonnull Balls balls() {
		return balls;
	}

    /**
     * @return The runs scored in this over
     */
	public @Nonnull Score score() {
	    return balls.score();
    }

    /**
     * @return The number of runs gained by the team in this over (including extras).
     */
	public @Nonnegative int teamRuns() {
		return balls.score().teamRuns();
	}

    /**
     * @return True if no runs have been scored from the bat or as bowling extras in this over.
     * If not {@link #isComplete()} then returns false even if there are no runs so far.
     */
	public boolean isMaiden() {
		return isComplete() && (score().batterRuns() + score().bowlingExtras()) == 0;
	}

    /**
     * @return The number of valid balls remaining to be bowled in this over.
     */
	public @Nonnegative int remainingBalls() {
	    return ballsInOver - validDeliveries();
    }

    /**
     * @return The number of valid (or legal) balls bowled so far in this over.
     */
	public @Nonnegative int validDeliveries() {
		return (int) balls.list().stream().filter(BallCompletedEvent::isValid).count();
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
            "inningsNumber=" + inningsNumber +
            ", overNumber=" + overNumber +
            ", striker=" + striker +
            ", nonStriker=" + nonStriker +
            ", bowler=" + bowler +
            ", ballsInOver=" + ballsInOver +
            ", startTime=" + startTime +
            '}';
    }
}


