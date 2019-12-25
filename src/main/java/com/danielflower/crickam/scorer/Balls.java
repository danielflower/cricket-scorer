package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

import java.util.Objects;

public final class Balls {
    private final ImmutableList<Ball> balls;
    private final Score score;

    public Balls() {
        this(new ImmutableList<>(), Score.EMPTY);
    }
    private Balls(ImmutableList<Ball> balls, Score score) {
        this.balls = Objects.requireNonNull(balls);
        this.score = Objects.requireNonNull(score);
    }

    public Score score() {
		return score;
	}

    public ImmutableList<Ball> list() {
        return balls;
    }

    public Balls add(Ball ball) {
        return new Balls(balls.add(ball), score.add(ball.score()));
    }

    public int size() {
        return balls.size();
    }

    @Override
    public String toString() {
        return balls.size() + " (" + score.teamRuns() + " runs)";
    }


}
