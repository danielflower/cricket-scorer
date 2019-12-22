package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class BowlerInnings {
	private final Player player;
	private final Balls balls;
	private final ImmutableList<BowlingSpell> spells;

	public Player bowler() {
		return player;
	}

	public Balls balls() {
		return balls;
	}

	public ImmutableList<BowlingSpell> spells() {
		return spells;
	}

	public BowlerInnings(Player player, Balls balls, ImmutableList<BowlingSpell> spells) {
		this.player = player;
        this.balls = balls;
        this.spells = spells;
    }


    @Override
    public String toString() {
        return "BowlerInnings{" +
                "player=" + player +
                ", spells=" + spells +
                ", scorecard=" + balls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlerInnings that = (BowlerInnings) o;

        if (!player.equals(that.player)) return false;
        if (!balls.equals(that.balls)) return false;
        if (!spells.equals(that.spells)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + balls.hashCode();
        result = 31 * result + spells.hashCode();
        return result;
    }
}


