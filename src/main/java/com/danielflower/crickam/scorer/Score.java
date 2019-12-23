package com.danielflower.crickam.scorer;


import static com.danielflower.crickam.scorer.ScoreBuilder.score;

public class Score {
	private final int scored;
	private final int wides;
	private final int noBalls;
	private final int legByes;
	private final int byes;
	private final int penaltyRuns;
	private final int wickets;
	private final int dots;
	private final int singles;
	private final int twos;
	private final int threes;
	private final int fours;
	private final int sixes;
	private final int balls;

	Score(int scored, int wides, int noBalls, int legByes, int byes, int penaltyRuns, int wickets, int dots, int singles, int twos, int threes, int fours, int sixes, int balls) {
		this.scored = scored;
		this.wides = wides;
		this.noBalls = noBalls;
		this.legByes = legByes;
		this.byes = byes;
		this.penaltyRuns = penaltyRuns;
		this.wickets = wickets;
		this.dots = dots;
		this.singles = singles;
		this.twos = twos;
		this.threes = threes;
		this.fours = fours;
		this.sixes = sixes;
		this.balls = balls;
	}

	public int totalRuns() {
		return scored + wides + noBalls + legByes + byes + penaltyRuns;
	}

	public Score add(Score other) {
		return score()
				.setScored(scored + other.scored)
				.setWides(wides + other.wides)
				.setNoBalls(noBalls + other.noBalls)
				.setLegByes(legByes + other.legByes)
				.setByes(byes + other.byes)
				.setPenaltyRuns(penaltyRuns + other.penaltyRuns)
				.setWickets(wickets + other.wickets)
				.setDots(dots + other.dots)
				.setSingles(singles + other.singles)
				.setTwos(twos + other.twos)
				.setThrees(threes + other.threes)
				.setFours(fours + other.fours)
				.setSixes(sixes + other.sixes)
				.setBalls(balls + other.balls)
				.build();
	}

	public Score subtract(Score other) {
		return score()
				.setScored(scored - other.scored)
				.setWides(wides - other.wides)
				.setNoBalls(noBalls - other.noBalls)
				.setLegByes(legByes - other.legByes)
				.setByes(byes - other.byes)
				.setPenaltyRuns(penaltyRuns - other.penaltyRuns)
				.setWickets(wickets - other.wickets)
				.setDots(dots - other.dots)
				.setSingles(singles - other.singles)
				.setTwos(twos - other.twos)
				.setThrees(threes - other.threes)
				.setFours(fours - other.fours)
				.setSixes(sixes - other.sixes)
				.setBalls(balls - other.balls)
				.build();
	}

    public int extras() {
        return wides + noBalls + legByes + byes + penaltyRuns;
    }

	public int strikeRate() {
		return balls == 0 ? 0 : (int) Math.round((scored * 100.0) / balls);
	}

	public Double averageRunsPerWicket() {
		return wickets == 0 ? null : totalRuns() / (double)wickets;
	}

	public RPO runsPerOver() {
		return RPO.fromDouble(balls == 0 ? 0.0 : 6.0 * (totalRuns() / (double) balls));
	}

	public int wicketStrikeRate() {
		return wickets == 0 ? 0 : balls / wickets;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Score that = (Score) o;

		if (balls != that.balls) return false;
		if (byes != that.byes) return false;
		if (fours != that.fours) return false;
		if (legByes != that.legByes) return false;
		if (noBalls != that.noBalls) return false;
		if (penaltyRuns != that.penaltyRuns) return false;
		if (scored != that.scored) return false;
		if (dots != that.dots) return false;
		if (singles != that.singles) return false;
		if (sixes != that.sixes) return false;
		if (threes != that.threes) return false;
		if (twos != that.twos) return false;
		if (wickets != that.wickets) return false;
		if (wides != that.wides) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = scored;
		result = 31 * result + wides;
		result = 31 * result + noBalls;
		result = 31 * result + legByes;
		result = 31 * result + byes;
		result = 31 * result + penaltyRuns;
		result = 31 * result + wickets;
		result = 31 * result + dots;
		result = 31 * result + singles;
		result = 31 * result + twos;
		result = 31 * result + threes;
		result = 31 * result + fours;
		result = 31 * result + sixes;
		result = 31 * result + balls;
		return result;
	}

	@Override
	public String toString() {
		return "Score{" +
				"scored=" + scored +
				", wides=" + wides +
				", noBalls=" + noBalls +
				", legByes=" + legByes +
				", byes=" + byes +
				", penaltyRuns=" + penaltyRuns +
				", wickets=" + wickets +
				", dots=" + dots +
				", singles=" + singles +
				", twos=" + twos +
				", threes=" + threes +
				", fours=" + fours +
				", sixes=" + sixes +
				", balls=" + balls +
				'}';
	}

	public static Score Empty = ScoreBuilder.EMPTY;

    public int scoredFromBat() {
        return scored;
    }

    public int wides() {
        return wides;
    }

    public int noBalls() {
        return noBalls;
    }

    public int legByes() {
        return legByes;
    }

    public int byes() {
        return byes;
    }

    public int penaltyRuns() {
        return penaltyRuns;
    }

    public int wickets() {
        return wickets;
    }

    public int dots() {
        return dots;
    }

    public int singles() {
        return singles;
    }

    public int twos() {
        return twos;
    }

    public int threes() {
        return threes;
    }

    public int fours() {
        return fours;
    }

    public int sixes() {
        return sixes;
    }

    public int balls() {
        return balls;
    }
}


