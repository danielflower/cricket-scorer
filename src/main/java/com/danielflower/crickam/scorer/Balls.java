package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class Balls {
    private final ImmutableList<BallAtCompletion> balls;
    private final Score score;

    public Balls() {
        this(new ImmutableList<>(), Score.Empty);
    }
    private Balls(ImmutableList<BallAtCompletion> balls, Score score) {
        this.balls = balls;
        this.score = score;
    }

    public Score score() {
		return score;
	}

    public ImmutableList<BallAtCompletion> list() {
        return balls;
    }

    public Balls add(BallAtCompletion ball) {
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
