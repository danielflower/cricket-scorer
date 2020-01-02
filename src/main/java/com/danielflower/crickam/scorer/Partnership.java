package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.BatterInningsCompletedEvent;
import com.danielflower.crickam.scorer.events.InningsCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvent;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A batting partnership
 */
public final class Partnership implements MatchEventListener<Partnership> {
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
        private FixedData(Player firstBatter, Player secondBatter, int wicketNumber, Instant startTime) {
            this.firstBatter = requireNonNull(firstBatter);
            this.secondBatter = requireNonNull(secondBatter);
            this.wicketNumber = wicketNumber;
            this.startTime = requireNonNull(startTime);
        }
    }

    static Partnership newPartnership(int numberInInnings, Player first, Player second) {
        FixedData data = new FixedData(first, second, numberInInnings, Instant.now());
        return new Partnership(BattingState.IN_PROGRESS, data, new Balls(), new Balls(), new Balls(), null);
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

    private Partnership(BattingState state, FixedData data, Balls balls, Balls firstBatterContribution, Balls secondBatterContribution, Instant endTime) {
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
    public Balls firstBatterContribution() {
        return firstBatterContribution;
    }

    /**
     * @return The balls faced by {@link #secondBatter()} during this partnership
     */
    public Balls secondBatterContribution() {
        return secondBatterContribution;
    }

    public Partnership onEvent(MatchEvent event) {
        if (event instanceof BallCompletedEvent) {
            BallCompletedEvent ball = (BallCompletedEvent) event;
            Balls balls = this.balls.add(ball);
            Balls firstBatterContribution = ball.striker().equals(firstBatter()) ? this.firstBatterContribution.add(ball) : this.firstBatterContribution;
            Balls secondBatterContribution = ball.striker().equals(secondBatter()) ? this.secondBatterContribution.add(ball) : this.secondBatterContribution;
            return new Partnership(state, data, balls, firstBatterContribution, secondBatterContribution, endTime);
        } else if (event instanceof BatterInningsCompletedEvent) {
            BatterInningsCompletedEvent e = (BatterInningsCompletedEvent) event;
            return new Partnership(e.reason(), data, balls, firstBatterContribution, secondBatterContribution, e.time().orElse(null));
        } else if (event instanceof InningsCompletedEvent) {
            if (state == BattingState.IN_PROGRESS) {
                return new Partnership(BattingState.INNINGS_ENDED, data, balls, firstBatterContribution, secondBatterContribution, event.time().orElse(null));
            }
        }
        return this;
    }
}


