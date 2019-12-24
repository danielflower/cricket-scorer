package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class BowlingSpell {
	private final BowlerInnings bowlerInnings;
    private final int spellNumber;
    private final ImmutableList<Over> overs;
	private final Balls balls;

	public BowlerInnings bowlerInnings() {
		return bowlerInnings;
	}

	public ImmutableList<Over> overs() {
		return overs;
	}

	public Balls balls() {
		return balls;
	}

    /**
     * @return The spell number that this spell is for this bowler. Their first bowling spell will return 1.
     */
	public int spellNumber() {
		return spellNumber;
	}

	public BowlingSpell(BowlerInnings bowlerInnings, int spellNumber, ImmutableList<Over> overs, Balls balls) {
		this.bowlerInnings = bowlerInnings;
        this.spellNumber = spellNumber;
        this.overs = overs;
        this.balls = balls;
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
                ", balls=" + balls +
                '}';
    }
}


