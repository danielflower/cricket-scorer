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

    /**
     * @param scored The number of runs scored off the bat. You should also call one of {@link #setSingles(int)},
     *               {@link #setTwos(int)}, {@link #setThrees(int)}, {@link #setFours(int)}, {@link #setSixes(int)}
     *               if it is one of those amounts.
     * @return This builder
     */
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

    public static Score byes(int number) {
        return score().setBalls(1).setByes(number).build();
    }

    public static Score legByes(int number) {
        return score().setBalls(1).setLegByes(number).build();
    }

    public static Score penaltyRuns(int number) {
        return score().setBalls(1).setPenaltyRuns(number).build();
    }

    public static ScoreBuilder from(Score score) {
        return new ScoreBuilder()
            .setThrees(score.threes())
            .setTwos(score.twos())
            .setSingles(score.singles())
            .setScored(score.scoredFromBat())
            .setBalls(score.balls())
            .setDots(score.dots())
            .setByes(score.byes())
            .setFours(score.fours())
            .setLegByes(score.legByes())
            .setNoBalls(score.noBalls())
            .setPenaltyRuns(score.penaltyRuns())
            .setSixes(score.sixes())
            .setWickets(score.wickets())
            .setWides(score.wides());
    }

    public ScoreBuilder withNoExtras() {
        return this.setWides(0).setPenaltyRuns(0).setNoBalls(0).setLegByes(0).setByes(0);
    }


    public static final Score DOT_BALL = score().setBalls(1).setDots(1).build();
    public static final Score SIX = score().setBalls(1).setScored(6).setSixes(1).build();
    public static final Score FOUR = score().setBalls(1).setScored(4).setFours(1).build();
    public static final Score THREE = score().setBalls(1).setThrees(1).setScored(3).build();
    public static final Score TWO = score().setBalls(1).setTwos(1).setScored(2).build();
    public static final Score SINGLE = score().setBalls(1).setSingles(1).setScored(1).build();
    public static final Score WIDE = score().setWides(1).build();
    public static final Score NO_BALL = score().setNoBalls(1).build();
    public static final Score WICKET = score().setBalls(1).setDots(1).setWickets(1).build();
    private static final Score[] COMMON = new Score[] {DOT_BALL, SIX, FOUR, THREE, TWO, SINGLE, WIDE, NO_BALL, WICKET};

    public static final Score EMPTY = score().build();


    public Score build() {
        Score score = new Score(scored, wides, noBalls, legByes, byes, penaltyRuns, wickets, dots, singles, twos, threes, fours, sixes, balls);
        if (COMMON != null) {
            for (Score cached : COMMON) {
                if (cached.equals(score)) {
                    return cached;
                }
            }
        }
        return score;
    }
    public static ScoreBuilder score() {
        return new ScoreBuilder();
    }
}