package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.Objects;

/**
 * An immutable list of {@link BallCompletedEvent} objects and their total score.
 */
@Immutable
public final class Balls implements Iterable<BallCompletedEvent> {
    private final ImmutableList<BallCompletedEvent> balls;
    private final Score score;

    Balls() {
        this(ImmutableList.emptyList(), Score.EMPTY);
    }

    private Balls(ImmutableList<BallCompletedEvent> balls, Score score) {
        this.balls = Objects.requireNonNull(balls);
        this.score = Objects.requireNonNull(score);
    }

    /**
     * @return The total score of all the balls in this list
     */
    public @Nonnull Score score() {
		return score;
	}

    /**
     * @return The balls as an immutable list
     */
    public @Nonnull ImmutableList<BallCompletedEvent> list() {
        return balls;
    }

    Balls add(BallCompletedEvent ball) {
        return new Balls(balls.add(ball), score.add(ball.score()));
    }

    /**
     * @return The number of balls bowled (including invalid balls such as wides)
     */
    public @Nonnegative int size() {
        return balls.size();
    }

    @Override
    public String toString() {
        return balls.size() + " (" + score.teamRuns() + " runs)";
    }

    @Override
    public Iterator<BallCompletedEvent> iterator() {
        return balls.iterator();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balls balls1 = (Balls) o;
        return balls.equals(balls1.balls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balls);
    }

    /**
     * @return The last valid ball in this list, or null if there are none
     * @see #last()
     */
    public @Nullable BallCompletedEvent lastValid() {
        Iterator<BallCompletedEvent> ballIterator = list().reverseIterator();
        while (ballIterator.hasNext()) {
            BallCompletedEvent next = ballIterator.next();
            if (next.isValid()) {
                return next;
            }
        }
        return null;
    }

    /**
     * @return The last ball in this list, or null if there are none
     * @see #lastValid()
     */
    public @Nullable BallCompletedEvent last() {
        return list().last();
    }

}
