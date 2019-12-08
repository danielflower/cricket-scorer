package com.danielflower.crickam.scorer;

import java.util.ArrayList;
import java.util.List;

public class BowlingSpell {
	private final BowlerInnings bowlerInnings;
    private int spellNumber;
    private final List<Over> overs = new ArrayList<>();
	private Balls balls = new Balls();

	public BowlerInnings getBowlerInnings() {
		return bowlerInnings;
	}

	public List<Over> getOvers() {
		return overs;
	}

	public Balls getBalls() {
		return balls;
	}

	public int getSpellNumber() {
		return spellNumber;
	}

	public BowlingSpell(BowlerInnings bowlerInnings, int spellNumber) {
		this.bowlerInnings = bowlerInnings;
        this.spellNumber = spellNumber;
    }

	public void addBall(BallAtCompletion ball) {
		balls = balls.add(ball);
		bowlerInnings.addBall(ball);
	}

    public void addOver(Over over) {
        this.overs.add(over);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlingSpell that = (BowlingSpell) o;

        if (spellNumber != that.spellNumber) return false;
        if (!overs.equals(that.overs)) return false;
        if (!balls.equals(that.balls)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = spellNumber;
        result = 31 * result + overs.hashCode();
        result = 31 * result + balls.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BowlingSpell{" +
                "spellNumber=" + spellNumber +
                ", overs=" + overs +
                ", scorecard=" + balls +
                '}';
    }
}


