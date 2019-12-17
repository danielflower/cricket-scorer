package com.danielflower.crickam.scorer;

public class Over {
	private final int numberInInnings;
	private final BatsmanInnings striker;
	private final BatsmanInnings nonStriker;
	private final Balls balls;
	private final BowlingSpell bowlingSpell;

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

	public Over(int numberInInnings, BatsmanInnings striker, BatsmanInnings nonStriker, Balls balls, BowlingSpell bowlingSpell) {
		this.numberInInnings = numberInInnings;
		this.striker = striker;
		this.nonStriker = nonStriker;
        this.balls = balls;
        this.bowlingSpell = bowlingSpell;
	}

	public int runs() {
		return balls.score().totalRuns();
	}

	public boolean isMaiden() {
		return runs() == 0;
	}



	public int getLegalBalls() {
		return (int) balls.list().stream().filter(BallAtCompletion::isLegal).count();
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


