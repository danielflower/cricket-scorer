package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;

/**
 * A bowling spell for a single bowler.
 * <p>Any overs bowled consecutively by a bowler (i.e. where there is a gap of just one over between two overs)
 * are considered part of a single spell.</p>
 */
public final class BowlingSpell {
	private final Player bowler;
    private final int spellNumber;
    private final ImmutableList<Over> overs;
	private final Balls balls;

    /**
     * @return The bowler
     */
	public Player bowler() {
		return bowler;
	}

    /**
     * @return The overs in this spell
     */
	public ImmutableList<Over> overs() {
		return overs;
	}

    /**
     * @return All the balls in this spell
     */
	public Balls balls() {
		return balls;
	}

    /**
     * @return The total score in this spell.
     */
	public Score score() {
	    return balls.score();
    }

    /**
     * @return The spell number that this spell is for this bowler. Their first bowling spell will return 1.
     */
	public int spellNumber() {
		return spellNumber;
	}

    /**
     * @return The number of maidens bowled in this spell
     */
    public int maidens() {
        return (int) overs.stream().filter(over -> over.isMaiden() && over.balls().list().get(0).bowler().equals(bowler)).count();
    }

	BowlingSpell(Player bowler, int spellNumber, ImmutableList<Over> overs, Balls balls) {
		this.bowler = bowler;
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

    BowlingSpell onBall(Over over, Ball ball) {
        return new BowlingSpell(bowler, spellNumber, overs.add(over), balls.add(ball));
    }
}


