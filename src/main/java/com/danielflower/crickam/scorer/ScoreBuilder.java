package com.danielflower.crickam.scorer;

/**
 * A score for a number of balls
 */
public class ScoreBuilder {
    private int runsFromBat = 0;
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

    /**
     * @param runsFromBat The number of runs scored off the bat. You should also call one of {@link #withSingles(int)},
     *                    {@link #withTwos(int)}, {@link #withThrees(int)}, {@link #withFours(int)}, {@link #withSixes(int)}
     *                    if it is one of those amounts.
     * @return This builder
     */
    public ScoreBuilder withRunsFromBat(int runsFromBat) {
        this.runsFromBat = runsFromBat;
        return this;
    }

    public ScoreBuilder withWides(int wides) {
        this.wides = wides;
        return this;
    }

    public ScoreBuilder withNoBalls(int noBalls) {
        this.noBalls = noBalls;
        return this;
    }

    public ScoreBuilder withLegByes(int legByes) {
        this.legByes = legByes;
        return this;
    }

    public ScoreBuilder withByes(int byes) {
        this.byes = byes;
        return this;
    }

    public ScoreBuilder withPenaltyRuns(int penaltyRuns) {
        this.penaltyRuns = penaltyRuns;
        return this;
    }

    public ScoreBuilder withWickets(int wickets) {
        this.wickets = wickets;
        return this;
    }

    public ScoreBuilder withDots(int dots) {
        this.dots = dots;
        return this;
    }

    public ScoreBuilder withSingles(int singles) {
        this.singles = singles;
        return this;
    }

    public ScoreBuilder withTwos(int twos) {
        this.twos = twos;
        return this;
    }

    public ScoreBuilder withThrees(int threes) {
        this.threes = threes;
        return this;
    }

    /**
     * @param fours The number of boundaries hit off the bat. Should not be set for 4 extras or if 4 are run.
     *              Note that {@link #withRunsFromBat(int)} should be set too.
     * @return This builder.
     */
    public ScoreBuilder withFours(int fours) {
        this.fours = fours;
        return this;
    }

    public ScoreBuilder withSixes(int sixes) {
        this.sixes = sixes;
        return this;
    }

    /**
     * @param balls The number of legal balls (i.e. number of balls that are not wides or no-balls
     * @return This builder
     */
    public ScoreBuilder withValidBalls(int balls) {
        this.balls = balls;
        return this;
    }

    public static Score byes(int number) {
        return score().withValidBalls(1).withByes(number).build();
    }

    public static Score legByes(int number) {
        return score().withValidBalls(1).withLegByes(number).build();
    }

    public static Score penaltyRuns(int number) {
        return score().withValidBalls(1).withPenaltyRuns(number).build();
    }

    public static ScoreBuilder from(Score score) {
        return new ScoreBuilder()
            .withThrees(score.threes())
            .withTwos(score.twos())
            .withSingles(score.singles())
            .withRunsFromBat(score.batterRuns())
            .withValidBalls(score.balls())
            .withDots(score.dots())
            .withByes(score.byes())
            .withFours(score.fours())
            .withLegByes(score.legByes())
            .withNoBalls(score.noBalls())
            .withPenaltyRuns(score.penaltyRuns())
            .withSixes(score.sixes())
            .withWickets(score.wickets())
            .withWides(score.wides());
    }

    public ScoreBuilder withNoExtras() {
        return this.withWides(0).withPenaltyRuns(0).withNoBalls(0).withLegByes(0).withByes(0);
    }


    private static final Score[] COMMON = new Score[]{Score.DOT_BALL, Score.SIX, Score.FOUR, Score.THREE, Score.TWO, Score.SINGLE, Score.WIDE, Score.NO_BALL, Score.WICKET, Score.BYE, Score.LEG_BYE};


    public Score build() {
        Score score = new Score(runsFromBat, wides, noBalls, legByes, byes, penaltyRuns, wickets, dots, singles, twos, threes, fours, sixes, balls);
        for (Score cached : COMMON) {
            if (score.equals(cached)) {
                return cached;
            }
        }
        return score;
    }

    public static ScoreBuilder score() {
        return new ScoreBuilder();
    }
}