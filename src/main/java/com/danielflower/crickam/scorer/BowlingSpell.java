package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;

import java.util.Objects;

import static com.danielflower.crickam.scorer.BowlerInnings.addOverWithPreviousRemovedIfSame;

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
    private final int wickets;

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

    /**
     * The number of wickets credited to the bowler, which may differ from the wickets reported by {@link #score()}
     * which includes dismissals such as run-outs
     * @return The number of wickets credited to the bowler
     */
    public int wickets() {
        return wickets;
    }

	BowlingSpell(Player bowler, int spellNumber, ImmutableList<Over> overs, Balls balls, int wickets) {
		this.bowler = bowler;
        this.spellNumber = spellNumber;
        this.overs = overs;
        this.balls = balls;
        this.wickets = wickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BowlingSpell that = (BowlingSpell) o;
        return spellNumber == that.spellNumber &&
            wickets == that.wickets &&
            Objects.equals(bowler, that.bowler) &&
            Objects.equals(overs, that.overs) &&
            Objects.equals(balls, that.balls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bowler, spellNumber, overs, balls, wickets);
    }

    @Override
    public String toString() {
        Score s = balls.score();
        return bowler + "    " + overs.size() + " Overs; " + s.teamRuns() + " Runs; " + wickets() + " Wkts; " + s.runsPerOver() + " RPO; " + s.dots() + " 0s; " + s.fours() + " 4s; " + s.sixes() + " 6s";
    }

    BowlingSpell onBall(Over over, BallCompletedEvent ball) {
        ImmutableList<Over> newOvers = addOverWithPreviousRemovedIfSame(overs, over);
        int wickets = this.wickets;
        if (ball.dismissal().isPresent() && ball.dismissal().get().type().creditedToBowler()) {
            wickets++;
        }
        return new BowlingSpell(bowler, spellNumber, newOvers, balls.add(ball), wickets);
    }
}


