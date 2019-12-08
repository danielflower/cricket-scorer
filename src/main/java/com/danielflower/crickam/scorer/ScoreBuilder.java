package com.danielflower.crickam.scorer;

public class ScoreBuilder {
	private int scored = 0;
	private int wides = 0;
	private int noBalls = 0;
	private int legByes = 0;
	private int byes = 0;
	private int penaltyRuns = 0;
	private int wickets = 0;
	private int singles = 0;
	private int twos = 0;
	private int threes = 0;
	private int fours = 0;
	private int sixes = 0;
	private int balls = 0;
	private int dots = 0;

	public ScoreBuilder setScored(int scored) {
		this.scored = scored;
		return this;
	}

	public ScoreBuilder setWides(int wides) {
		this.wides = wides;
		return this;
	}

	public ScoreBuilder setNoBalls(int noBalls) {
		this.noBalls = noBalls;
		return this;
	}

	public ScoreBuilder setLegByes(int legByes) {
		this.legByes = legByes;
		return this;
	}

	public ScoreBuilder setByes(int byes) {
		this.byes = byes;
		return this;
	}

	public ScoreBuilder setPenaltyRuns(int penaltyRuns) {
		this.penaltyRuns = penaltyRuns;
		return this;
	}

	public ScoreBuilder setWickets(int wickets) {
		this.wickets = wickets;
		return this;
	}

	public ScoreBuilder setDots(int dots) {
		this.dots = dots;
		return this;
	}

	public ScoreBuilder setSingles(int singles) {
		this.singles = singles;
		return this;
	}

	public ScoreBuilder setTwos(int twos) {
		this.twos = twos;
		return this;
	}

	public ScoreBuilder setThrees(int threes) {
		this.threes = threes;
		return this;
	}

	public ScoreBuilder setFours(int fours) {
		this.fours = fours;
		return this;
	}

	public ScoreBuilder setSixes(int sixes) {
		this.sixes = sixes;
		return this;
	}

	public ScoreBuilder setBalls(int balls) {
		this.balls = balls;
		return this;
	}

	public Score build() {
		return new Score(scored, wides, noBalls, legByes, byes, penaltyRuns, wickets, dots, singles, twos, threes, fours, sixes, balls);
	}

	public static ScoreBuilder dotBall() {
		return score().setBalls(1).setDots(1);
	}

	public static ScoreBuilder byes(int number) {
		return score().setBalls(1).setByes(number);
	}

	public static ScoreBuilder legByes(int number) {
		return score().setBalls(1).setLegByes(number);
	}

	public static ScoreBuilder penaltyRuns(int number) {
		return score().setBalls(1).setPenaltyRuns(number);
	}

	public static ScoreBuilder six() {
		return score().setBalls(1).setScored(6).setSixes(1);
	}

	public static ScoreBuilder four() {
		return score().setBalls(1).setScored(4).setFours(1);
	}

	public static ScoreBuilder wide() {
		return score().setWides(1);
	}

	public static ScoreBuilder noBall() {
		return score().setNoBalls(1);
	}

	public static ScoreBuilder wicket() {
		return score().setBalls(1).setDots(1).setWickets(1);
	}

	public static ScoreBuilder score() {
		return new ScoreBuilder();
	}

	public static final Score Empty = score().build();
}