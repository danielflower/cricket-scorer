package com.danielflower.crickam.scorer;

public class Over {
	private final int numberInInnings;
	private final BatsmanInnings striker;
	private final BatsmanInnings nonStriker;
	private Balls balls = new Balls();
	private BowlingSpell bowlingSpell;

	public BatsmanInnings getStriker() {
		return striker;
	}

	public BatsmanInnings getNonStriker() {
		return nonStriker;
	}

	public BowlingSpell getBowlingSpell() {
		return bowlingSpell;
	}

	public int getNumberInInnings() {
		return numberInInnings;
	}

	public Balls getBalls() {
		return balls;
	}

	public BallAtStart getCurrentBall() {
		if (balls.asList().size() == 0) {
			return null;
		}
		return balls.asList().get(balls.asList().size() - 1);
	}

	public Over(int numberInInnings, BatsmanInnings striker, BatsmanInnings nonStriker, BowlingSpell bowlingSpell) {
		this.numberInInnings = numberInInnings;
		this.striker = striker;
		this.nonStriker = nonStriker;
		this.bowlingSpell = bowlingSpell;
	}

	public void addBall(BallAtCompletion ball) {
		balls = balls.add(ball);
	}

	public int runs() {
		return balls.score().totalRuns();
	}

	public boolean isMaiden() {
		return runs() == 0;
	}

	public BallAtStart newBall(int id, BatsmanInnings striker, BatsmanInnings nonStriker) {
		int alreadyBowled = balls.asList().size();
		BallAtCompletion previousBall = alreadyBowled == 0 ? null : balls.asList().get(alreadyBowled - 1);
		return new Ball(
				id, striker, nonStriker, alreadyBowled + 1, previousBall, bowlingSpell
		);
	}


	public int getLegalBalls() {
		return (int) balls.asList().stream().filter(BallAtCompletion::isLegal).count();
	}

    public boolean isComplete() {
        return getLegalBalls() >= 6;
    }

    @Override
    public String toString() {
        return "Over{" +
                "numberInInnings=" + numberInInnings +
                '}';
    }
}


