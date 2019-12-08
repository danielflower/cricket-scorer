package com.danielflower.crickam.scorer;

import java.util.ArrayList;
import java.util.List;

public class BowlerInnings {
	private final Player player;
	private Balls balls = new Balls();
	private final List<BowlingSpell> spells = new ArrayList<>();

	public Player getPlayer() {
		return player;
	}

	public Balls getScorecard() {
		return balls;
	}

	public List<BowlingSpell> getSpells() {
		return spells;
	}

	public BowlerInnings(Player player) {
		this.player = player;
	}

    public void addBowlingSpell(BowlingSpell bowlingSpell) {
        this.spells.add(bowlingSpell);
    }

    public void addBall(BallAtCompletion ball) {
		balls = balls.add(ball);
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


