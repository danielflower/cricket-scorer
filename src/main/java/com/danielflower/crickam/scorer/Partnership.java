package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.BatterInningsCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * A batting partnership
 */
@Immutable
public final class Partnership {
    private final BattingState state;
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
        private FixedData(Player firstBatter, Player secondBatter, @Nonnegative int wicketNumber, @Nullable Instant startTime) {
            this.firstBatter = requireNonNull(firstBatter);
            this.secondBatter = requireNonNull(secondBatter);
            this.wicketNumber = wicketNumber;
            this.startTime = startTime;
        }
    }

    static Partnership newPartnership(@Nonnegative int numberInInnings, Player first, Player second, @Nullable Instant startTime) {
        FixedData data = new FixedData(first, second, numberInInnings, startTime);
        return new Partnership(BattingState.IN_PROGRESS, data, new Balls(), new Balls(), new Balls(), null);
    }

    /**
     * @return The time the partnership was ended (due to a wicket or the end of the innings), or null if it is ongoing
     */
    public @Nullable Instant endTime() {
        return endTime;
    }

    /**
     * @return The partner in the partnership who is higher in the batting order
     */
    public final @Nonnull Player firstBatter() {
        return data.firstBatter;
    }

    /**
     * @return The partner in the partnership who is lower in the batting order
     */
    public final @Nonnull Player secondBatter() {
        return data.secondBatter;
    }

    /**
     * @return All deliveries faced during this partnership
     */
    public @Nonnull Balls balls() {
        return balls;
    }

    /**
     * @return The score of this partnership
     */
    public @Nonnull Score score() {
        return balls.score();
    }

    /**
     * @return True if this partnership was ended by one of the batters getting out
     */
    public boolean brokenByWicket() {
        BallCompletedEvent b = balls.list().last();
        return b != null && b.dismissal() != null;
    }

    /**
     * @return The index of this partnership: 1 for the first partnership, 2 for the second, etc.
     */
    public @Nonnegative int wicketNumber() {
	    return data.wicketNumber;
    }

    /**
     * @return The time that the partnership started
     */
    public @Nullable Instant startTime() {
	    return data.startTime;
    }

    private Partnership(BattingState state, FixedData data, Balls balls, Balls firstBatterContribution, Balls secondBatterContribution, @Nullable Instant endTime) {
        this.state = state;
        this.data = requireNonNull(data);
        this.balls = requireNonNull(balls);
        this.firstBatterContribution = requireNonNull(firstBatterContribution);
        this.secondBatterContribution = requireNonNull(secondBatterContribution);
        this.endTime = endTime;
    }

    /**
     * @return The balls faced by {@link #firstBatter()} during this partnership
     */
    public @Nonnull Balls firstBatterContribution() {
        return firstBatterContribution;
    }

    /**
     * @return The balls faced by {@link #secondBatter()} during this partnership
     */
    public @Nonnull Balls secondBatterContribution() {
        return secondBatterContribution;
    }

    public @Nonnull Partnership onEvent(MatchEvent event) {
        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            Balls balls = this.balls.add(ball);
            Balls firstBatterContribution = ball.striker().equals(firstBatter()) ? this.firstBatterContribution.add(ball) : this.firstBatterContribution;
            Balls secondBatterContribution = ball.striker().equals(secondBatter()) ? this.secondBatterContribution.add(ball) : this.secondBatterContribution;
            return new Partnership(state, data, balls, firstBatterContribution, secondBatterContribution, endTime);
        } else if (event instanceof BatterInningsCompletedEvent) {
            BatterInningsCompletedEvent e = (BatterInningsCompletedEvent) event;
            return new Partnership(e.reason(), data, balls, firstBatterContribution, secondBatterContribution, e.time());
        } else if (event instanceof InningsCompletedEvent) {
            if (state == BattingState.IN_PROGRESS) {
                return new Partnership(BattingState.INNINGS_ENDED, data, balls, firstBatterContribution, secondBatterContribution, event.time());
            }
        }
        return this;
    }
}


