package com.danielflower.crickam.scorer;


import static com.danielflower.crickam.scorer.ScoreBuilder.score;

public final class Score {
    public static Score EMPTY = new Score(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Score DOT_BALL = new Score(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1);
    public static final Score SINGLE = new Score(1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    public static final Score TWO = new Score(2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1);
    public static final Score THREE = new Score(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1);
    public static final Score FOUR = new Score(4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1);
    public static final Score SIX = new Score(6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1);
    public static final Score WIDE = new Score(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Score NO_BALL = new Score(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Score BYE = new Score(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1);
    public static final Score LEG_BYE = new Score(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
    public static final Score WICKET = new Score(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1);
    private final int runsFromBat;
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
	private final int validBalls;

	Score(int runsFromBat, int wides, int noBalls, int legByes, int byes, int penaltyRuns, int wickets, int dots, int singles, int twos, int threes, int fours, int sixes, int validBalls) {
		this.runsFromBat = runsFromBat;
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
		this.validBalls = validBalls;
	}

    /**
     * @return The total runs scored for the team (which is runs off the bat plus extras)
     */
	public int teamRuns() {
		return bowlerRuns() + legByes + byes;
	}

    /**
     * @return The number of runs ascribed to the bowler, which is total runs less byes and leg byes
     */
    public int bowlerRuns() {
        return batterRuns() + wides + noBalls + penaltyRuns;
    }

    /**
     * @return The number of extras, i.e. runs not scored from the bat
     */
    public int extras() {
        return wides + noBalls + legByes + byes + penaltyRuns;
    }

	public Score add(Score other) {
		return score()
				.withRunsFromBat(runsFromBat + other.runsFromBat)
				.withWides(wides + other.wides)
				.withNoBalls(noBalls + other.noBalls)
				.withLegByes(legByes + other.legByes)
				.withByes(byes + other.byes)
				.withPenaltyRuns(penaltyRuns + other.penaltyRuns)
				.withWickets(wickets + other.wickets)
				.withDots(dots + other.dots)
				.withSingles(singles + other.singles)
				.withTwos(twos + other.twos)
				.withThrees(threes + other.threes)
				.withFours(fours + other.fours)
				.withSixes(sixes + other.sixes)
				.withValidBalls(validBalls + other.validBalls)
				.build();
	}

	public Score subtract(Score other) {
		return score()
				.withRunsFromBat(runsFromBat - other.runsFromBat)
				.withWides(wides - other.wides)
				.withNoBalls(noBalls - other.noBalls)
				.withLegByes(legByes - other.legByes)
				.withByes(byes - other.byes)
				.withPenaltyRuns(penaltyRuns - other.penaltyRuns)
				.withWickets(wickets - other.wickets)
				.withDots(dots - other.dots)
				.withSingles(singles - other.singles)
				.withTwos(twos - other.twos)
				.withThrees(threes - other.threes)
				.withFours(fours - other.fours)
				.withSixes(sixes - other.sixes)
				.withValidBalls(validBalls - other.validBalls)
				.build();
	}



	public int strikeRate() {
		return validBalls == 0 ? 0 : (int) Math.round((runsFromBat * 100.0) / validBalls);
	}

	public Double averageRunsPerWicket() {
		return wickets == 0 ? null : teamRuns() / (double)wickets;
	}

	public RPO runsPerOver() {
		return RPO.fromDouble(validBalls == 0 ? 0.0 : 6.0 * (teamRuns() / (double) validBalls));
	}

	public int wicketStrikeRate() {
		return wickets == 0 ? 0 : validBalls / wickets;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Score that = (Score) o;

		if (validBalls != that.validBalls) return false;
		if (byes != that.byes) return false;
		if (fours != that.fours) return false;
		if (legByes != that.legByes) return false;
		if (noBalls != that.noBalls) return false;
		if (penaltyRuns != that.penaltyRuns) return false;
		if (runsFromBat != that.runsFromBat) return false;
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
		int result = runsFromBat;
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
		result = 31 * result + validBalls;
		return result;
	}

	@Override
	public String toString() {
		return "Score{" +
				"scored=" + runsFromBat +
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
				", balls=" + validBalls +
				'}';
	}


    /**
     * @return The number of runs ascribed by the batter, which is the total runs less extras.
     */
    public int batterRuns() {
        return runsFromBat;
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

    public int validBalls() {
        return validBalls;
    }

}


