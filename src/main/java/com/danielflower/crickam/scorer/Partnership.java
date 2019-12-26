package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A batting partnership
 */
public final class Partnership {
    private final Balls balls;
	private final Balls firstBatterContribution;
	private final Balls secondBatterContribution;
	private final Instant endTime;
	private final FixedData data;

    private static class FixedData {

        private final Player firstBatter;
        private final Player secondBatter;
        private final int wicketNumber;
        private final Instant startTime;
        private FixedData(Player firstBatter, Player secondBatter, int wicketNumber, Instant startTime) {
            this.firstBatter = requireNonNull(firstBatter);
            this.secondBatter = requireNonNull(secondBatter);
            this.wicketNumber = wicketNumber;
            this.startTime = requireNonNull(startTime);
        }
    }

    static Partnership newPartnership(int numberInInnings, Player first, Player second) {
        FixedData data = new FixedData(first, second, numberInInnings, Instant.now());
        return new Partnership(data, new Balls(), new Balls(), new Balls(), null);
    }

    /**
     * @return The time the partnership was ended (due to a wicket or the end of the innings), or empty if it is ongoing
     */
    public Optional<Instant> endTime() {
        return Optional.ofNullable(endTime);
    }

    /**
     * @return The partner in the partnership who is higher in the batting order
     */
    public final Player firstBatter() {
        return data.firstBatter;
    }

    /**
     * @return The partner in the partnership who is lower in the batting order
     */
    public final Player secondBatter() {
        return data.secondBatter;
    }

    /**
     * @return All deliveries faced during this partnership
     */
    public Balls balls() {
        return balls;
    }

    /**
     * @return The score of this partnership
     */
    public Score score() {
        return balls.score();
    }

    /**
     * @return True if this partnership was ended by one of the batters getting out
     */
    public boolean brokenByWicket() {
        return balls.size() > 0 && balls.list().last().get().dismissal().isPresent();
    }

    /**
     * @return The index of this partnership: 1 for the first partnership, 2 for the second, etc.
     */
    public int wicketNumber() {
	    return data.wicketNumber;
    }

    /**
     * @return The time that the partnership started
     */
    public Instant startTime() {
	    return data.startTime;
    }

    private Partnership(FixedData data, Balls balls, Balls firstBatterContribution, Balls secondBatterContribution, Instant endTime) {
        this.data = requireNonNull(data);
        this.balls = requireNonNull(balls);
        this.firstBatterContribution = requireNonNull(firstBatterContribution);
        this.secondBatterContribution = requireNonNull(secondBatterContribution);
        this.endTime = endTime;
    }

    /**
     * @return The balls faced by {@link #firstBatter()} during this partnership
     */
    public Balls firstBatterContribution() {
        return firstBatterContribution;
    }

    /**
     * @return The balls faced by {@link #secondBatter()} during this partnership
     */
    public Balls secondBatterContribution() {
        return secondBatterContribution;
    }

    Partnership onBall(Ball ball) {
        Balls balls = this.balls.add(ball);
        Balls firstBatterContribution = ball.striker().equals(firstBatter()) ? this.firstBatterContribution.add(ball) : this.firstBatterContribution;
        Balls secondBatterContribution = ball.striker().equals(secondBatter()) ? this.secondBatterContribution.add(ball) : this.secondBatterContribution;
        Instant endTime = ball.dismissal().isPresent() ? ball.dateCompleted() : null;
        return new Partnership(data, balls, firstBatterContribution, secondBatterContribution, endTime);
    }
}


