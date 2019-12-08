package com.danielflower.crickam.scorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Balls {
	private volatile Score score = ScoreBuilder.Empty;
	private final List<BallAtCompletion> list = new ArrayList<>();

	public Score score() {
		return score;
	}

	public List<BallAtCompletion> asList() {
		return Collections.unmodifiableList(list);
	}

	public Balls add(BallAtCompletion ball) {
		list.add(ball);
		score = score.add(ball.getScore());
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Balls balls = (Balls) o;

		if (!this.list.equals(balls.list)) return false;
		if (!score.equals(balls.score)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = score.hashCode();
		result = 31 * result + list.hashCode();
		return result;
	}

	public int size() {
		return list.size();
	}

	public BallAtCompletion first() {
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public BallAtCompletion last() {
		if (list.size() == 0) {
			return null;
		}
		return list.get(list.size() - 1);
	}
}
