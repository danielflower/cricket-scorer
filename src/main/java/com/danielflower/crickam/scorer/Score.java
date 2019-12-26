package com.danielflower.crickam.scorer;


import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Runs scored, deliveries bowled, and wickets taken either from a single ball, or 0 or multiple balls.
 * <p>The types of runs (e.g. from the bat, from boundaries, from extras etc) are specified in this class along
 * with the number of valid and invalid deliveries.</p>
 * <p>These objects are immutable and can only be created with the {@link Score#score()} method to create
 * a builder, or by using a predefined score defined as static fields in this class, such as {@link #DOT_BALL}, {@link #SINGLE}, etc</p>
 */
public final class Score {
    /**
     * No balls delivered, with no wickets or runs scored.
     */
    public static final Score EMPTY = new Score(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    /**
     * No runs scored from a valid delivery.
     */
    public static final Score DOT_BALL = new Score(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0);
    /**
     * One run scored from the bat off of a valid delivery.
     */
    public static final Score SINGLE = new Score(1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0);
    /**
     * Two runs scored from the bat off of a valid delivery.
     */
    public static final Score TWO = new Score(2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0);
    /**
     * Three runs scored from the bat off of a valid delivery.
     */
    public static final Score THREE = new Score(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0);

    /**
     * Four runs scored by hitting the ball to the boundary from a valid delivery.
     * <p>Note that this is different from 4 runs run by the batters without it going to the boundary (which would have {@link #batterRuns} being 4 but {@link #fours()} being 0).</p>
     */
    public static final Score FOUR = new Score(4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0);
    /**
     * Six runs scored by hitting the ball over the boundary on the full from a valid delivery.
     */
    public static final Score SIX = new Score(6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0);
    /**
     * A wide (with no extra runs or wicket taken).
     */
    public static final Score WIDE = new Score(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1);

    /**
     * A no-ball (with no extra runs or wicket taken).
     */
    public static final Score NO_BALL = new Score(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1);

    /**
     * A single bye of a valid delivery.
     */
    public static final Score BYE = new Score(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0);
    /**
     * A single leg bye of a valid delivery.
     */
    public static final Score LEG_BYE = new Score(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0);

    /**
     * A wicket taken off a valid delivery.
     */
    public static final Score WICKET = new Score(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0);

    private final int batterRuns;
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
    private final int validDeliveries;
    private final int invalidDeliveries;

    Score(int batterRuns, int wides, int noBalls, int legByes, int byes, int penaltyRuns, int wickets, int dots, int singles, int twos, int threes, int fours, int sixes, int validDeliveries, int invalidDeliveries) {
        if ((wides > 0 || noBalls > 0) && invalidDeliveries == 0) {
            throw new IllegalStateException("When wides and/or no-balls are set, the number of invalid deliveries must be set also with the withInvalidDeliveries(int) method.");
        }
        if ((singles > 0 || twos > 0 || threes > 0 || fours > 0 || sixes > 0 || byes > 0 || legByes > 0) && ((validDeliveries + invalidDeliveries) == 0)) {
            throw new IllegalStateException("Wickets and runs from the bat can only be scored with valid or invalid balls are set too.");
        }
        this.batterRuns = batterRuns;
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
        this.validDeliveries = validDeliveries;
        this.invalidDeliveries = invalidDeliveries;
    }

    /**
     * Creates a new score builder
     * @return A new builder
     */
    public static Builder score() {
        return new Builder();
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
        return fieldingExtras() + bowlingExtras();
    }

    /**
     * @return The number of runs ascribed by the batter, which is the total runs less extras.
     */
    public int batterRuns() {
        return batterRuns;
    }

    /**
     * @return The number of runs scored from wides (e.g. a single wide delivery that goes to the boundary would
     * have a value of 4).
     */
    public int wides() {
        return wides;
    }

    /**
     * @return The number of runs from a no-ball. Note that if a boundary is hit off a no-ball, then this value
     * should return one, and {@link #batterRuns()} would return 4.
     */
    public int noBalls() {
        return noBalls;
    }

    /**
     * @return The number of leg byes scored.
     */
    public int legByes() {
        return legByes;
    }

    /**
     * @return The number of byes scored.
     */
    public int byes() {
        return byes;
    }

    /**
     * @return The number of penalty runs awarded.
     */
    public int penaltyRuns() {
        return penaltyRuns;
    }

    /**
     * @return The number of wickets taken.
     */
    public int wickets() {
        return wickets;
    }

    /**
     * @return The number of dot balls (i.e. balls with no runs and no extras)
     */
    public int dots() {
        return dots;
    }

    /**
     * @return The number of times a single was scored from the bat
     */
    public int singles() {
        return singles;
    }

    /**
     * @return The number of times a two was scored from the bat
     */
    public int twos() {
        return twos;
    }

    /**
     * @return The number of times a three was scored from the bat
     */
    public int threes() {
        return threes;
    }

    /**
     * @return The number of times the batter hit the ball to the boundary to score 4 runs (excludes cases where
     * the batters run 4 runs)
     */
    public int fours() {
        return fours;
    }

    /**
     * @return The number of times the batter hit the ball over the boundary to score 6 runs (excludes the unlikely
     * cases where the batters run 6 runs)
     */
    public int sixes() {
        return sixes;
    }

    /**
     * @return The number of valid (or legal) deliveries bowled (i.e. the number of balls excluding wides and no-balls).
     */
    public int validDeliveries() {
        return validDeliveries;
    }

    /**
     * @return The number of deliveries bowled by the bowler which were wides or no-balls
     */
    public int invalidDeliveries() {
        return invalidDeliveries;
    }

    /**
     * @return The number of extras that count against the bowler, i.e. wides plus no-balls
     */
    public int bowlingExtras() {
        return wides + noBalls;
    }

    /**
     * @return The number of extras that do not count against the bowler, i.e. byes, leg byes, and penalty runs
     */
    public int fieldingExtras() {
        return byes + legByes + penaltyRuns;
    }

    /**
     * @return The number of runs from the bat (i.e. excluding extras) per one hundred balls (or empty if there have been no valid balls).
     */
    public Optional<Double> battingStrikeRate() {
        return validDeliveries == 0 ? Optional.empty() : Optional.of((batterRuns() * 100.0) / validDeliveries);
    }

    /**
     * @return The average number of runs scored from the bat (i.e. excluding extras) per wicket, or empty if there have been no wickets
     */
    public Optional<Double> battingAverage() {
        return wickets == 0 ? Optional.empty() : Optional.of(batterRuns() / (double) wickets);
    }

    /**
     * @return The total number of runs scored per over, assuming 6-ball overs.
     * @see #bowlerEconomyRate()
     */
    public RPO runsPerOver() {
        return RPO.fromDouble(validDeliveries == 0 ? 0.0 : 6.0 * (teamRuns() / (double) validDeliveries));
    }

    /**
     * @return The number of runs scored per over that is ascribed to the bowler (i.e. {@link #batterRuns()} + {@link #bowlingExtras()}), assuming 6-ball overs.
     * @see #runsPerOver()
     */
    public RPO bowlerEconomyRate() {
        return RPO.fromDouble(validDeliveries == 0 ? 0.0 : 6.0 * ((batterRuns() + bowlingExtras()) / (double) validDeliveries));
    }

    /**
     * @return The average number of (valid) balls bowled per wicket, or empty if no wickets have been taken.
     */
    public Optional<Double> bowlingStrikeRate() {
        return wickets == 0 ? Optional.empty() : Optional.of(validDeliveries / (double) wickets);
    }

    /**
     * Creates a new score by adding each value from another score with this score.
     * <p>Note that as score objects are immutable, the original score will not be changed.</p>
     *
     * @param other The score to add
     * @return A new score which is the addition of the current with the other score.
     */
    public Score add(Score other) {
        return score()
            .withBatterRuns(batterRuns + other.batterRuns)
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
            .withValidDeliveries(validDeliveries + other.validDeliveries)
            .withInvalidDeliveries(invalidDeliveries + other.invalidDeliveries)
            .build();
    }

    /**
     * Creates a new score by subtracting each value from another score from each value in this score.
     * <p>Note that as score objects are immutable, the original score will not be changed.</p>
     *
     * @param other The score to subtract
     * @return A new score which is the addition of the current with the other score.
     */
    public Score subtract(Score other) {
        return score()
            .withBatterRuns(batterRuns - other.batterRuns)
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
            .withValidDeliveries(validDeliveries - other.validDeliveries)
            .withInvalidDeliveries(invalidDeliveries - other.invalidDeliveries)
            .build();
    }

    /**
     * Checks for value equality.
     * @param o The object to compare
     * @return Returns true if every field of the two scores match.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score that = (Score) o;

        if (validDeliveries != that.validDeliveries) return false;
        if (invalidDeliveries != that.invalidDeliveries) return false;
        if (byes != that.byes) return false;
        if (fours != that.fours) return false;
        if (legByes != that.legByes) return false;
        if (noBalls != that.noBalls) return false;
        if (penaltyRuns != that.penaltyRuns) return false;
        if (batterRuns != that.batterRuns) return false;
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
        int result = batterRuns;
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
        result = 31 * result + validDeliveries;
        result = 31 * result + invalidDeliveries;
        return result;
    }

    @Override
    public String toString() {
        return "Score{" +
            "batterRuns=" + batterRuns +
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
            ", validDeliveries=" + validDeliveries +
            ", invalidDeliveries=" + invalidDeliveries +
            '}';
    }

    /**
     * Parses a scorecard value for a score.
     * <p>Examples of scores:</p>
     * <ul>
     *     <li><strong>0</strong> or <strong>.</strong> which returns {@link #DOT_BALL}</li>
     *     <li><strong>1</strong> which returns {@link #SINGLE}</li>
     *     <li><strong>2</strong> which returns {@link #TWO}</li>
     *     <li><strong>3</strong> which returns {@link #THREE}</li>
     *     <li><strong>4</strong> which returns {@link #FOUR}</li>
     *     <li><strong>5</strong> which returns a score of 5 runs</li>
     *     <li><strong>6</strong> which returns {@link #SIX}</li>
     *     <li><strong>1lb</strong> which returns {@link #LEG_BYE}</li>
     *     <li><strong>2lb</strong> which returns 2 leg byes</li>
     *     <li><strong>W</strong> which returns a wicket without a run</li>
     *     <li><strong>1w</strong> which returns {@link #WIDE}</li>
     *     <li><strong>1nb</strong> which returns {@link #NO_BALL}</li>
     *     <li><strong>5nb</strong> which a no-ball with 4 runs off the bat</li>
     *     <li>etc</li>
     * </ul>
     * @param text A score, such as &quot;1&quot;, &quot;1lb&quot; &quot;W&quot; etc
     * @return A built score, or empty if unknown
     */
    public static Optional<Score> parse(String text) {
        if (".".equals(text)) return Optional.of(DOT_BALL);
        if ("W".equals(text)) return Optional.of(WICKET);
        Pattern pattern = Pattern.compile("^(?<num>[0-9]+)(?<modifier>[A-Za-z]+)?$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int runs = Integer.parseInt(matcher.group("num"));
        String modifier = matcher.group("modifier");
        Builder score = Score.score();
        if (modifier == null) {
            score.withValidDeliveries(1)
                .withBatterRuns(runs)
                .withDots(runs == 0 ? 1 : 0)
                .withSingles(runs == 1 ? 1 : 0)
                .withTwos(runs == 2 ? 1 : 0)
                .withThrees(runs == 3 ? 1 : 0)
                .withFours(runs == 4 ? 1 : 0)
                .withSixes(runs == 6 ? 1 : 0)
                ;
        } else {
            switch (modifier) {
                case "W":
                    score.withWickets(1).withValidDeliveries(1).withBatterRuns(runs);
                    break;
                case "w":
                    score.withWides(runs).withInvalidDeliveries(1);
                    break;
                case "nb":
                    score.withNoBalls(1).withInvalidDeliveries(1).withBatterRuns(runs - 1);
                    break;
                case "b":
                    score.withByes(1).withValidDeliveries(1);
                    break;
                case "lb":
                    score.withLegByes(1).withValidDeliveries(1);
                    break;
            }
        }
        return Optional.of(score.build());
    }

    /**
     * A builder to create a {@link Score} object.
     * <p>The runs scored and the number of valid or invalid deliveries need to be set on almost all scores. For
     * example, if setting {@link #withLegByes(int)} off a fair delivery, then {@link #withValidDeliveries(int)}
     * should be called as well. However, if the ball was a no-ball, then {@link #withInvalidDeliveries(int)} should be
     * called instead.</p>
     */
    public static final class Builder {
        private int batterRuns = 0;
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
        private int validDeliveries = 0;
        private int dots = 0;
        private int invalidDeliveries = 0;

        /**
         * @param batterRuns The number of runs scored off the bat. You should also call one of {@link #withSingles(int)},
         *                    {@link #withTwos(int)}, {@link #withThrees(int)}, {@link #withFours(int)}, {@link #withSixes(int)}
         *                    if it is one of those amounts.
         * @return This builder
         */
        public Builder withBatterRuns(int batterRuns) {
            this.batterRuns = batterRuns;
            return this;
        }

        /**
         * The number of runs scored from wides. Note this is a separate from the number of deliveries that were bowled
         * that were wides, which needs to also be set with {@link #withInvalidDeliveries(int)}.
         * @param wides The number of runs scored from wides.
         * @return This builder
         * @see #withInvalidDeliveries(int)
         */
        public Builder withWides(int wides) {
            this.wides = wides;
            return this;
        }

        /**
         * The number of runs scored from no-balls. Note this is a separate from the number of deliveries that were bowled
         * which needs to also be set with {@link #withInvalidDeliveries(int)}.
         * @param noBalls The number of runs scored from no-balls.
         * @return This builder
         * @see #withInvalidDeliveries(int)
         */
        public Builder withNoBalls(int noBalls) {
            this.noBalls = noBalls;
            return this;
        }

        /**
         * @param legByes The number of leg byes run
         * @return This builder
         */
        public Builder withLegByes(int legByes) {
            this.legByes = legByes;
            return this;
        }

        /**
         * @param byes The number of byes run
         * @return This builder
         */
        public Builder withByes(int byes) {
            this.byes = byes;
            return this;
        }

        /**
         * @param penaltyRuns The number of penalty runs awarded, for example 5 if the ball hits the helmet behind
         *                    the wicket keeper.
         * @return This builder
         */
        public Builder withPenaltyRuns(int penaltyRuns) {
            this.penaltyRuns = penaltyRuns;
            return this;
        }

        /**
         * @param wickets The number of wickets taken
         * @return This builder
         */
        public Builder withWickets(int wickets) {
            this.wickets = wickets;
            return this;
        }

        /**
         * @param dots The number of dot balls (i.e. balls with no runs and no extras)
         * @return This builder
         */
        public Builder withDots(int dots) {
            this.dots = dots;
            return this;
        }

        /**
         * Sets the number of singles run. For a single valid delivery where one run is scored off the bat, this value should be 1;
         * {@link #withBatterRuns(int)} should be 1; and {@link #withValidDeliveries(int)} should be 1.
         * @param singles The number of times a single was scored from the bat
         * @return This builder
         */
        public Builder withSingles(int singles) {
            this.singles = singles;
            return this;
        }

        /**
         * Sets the number of twos. For a single valid delivery where two runs are scored off the bat, this value should be 1;
         * {@link #withBatterRuns(int)} should be 2; and {@link #withValidDeliveries(int)} should be 1.
         * @param twos The number of times a two was scored from the bat
         * @return This builder
         */
        public Builder withTwos(int twos) {
            this.twos = twos;
            return this;
        }

        /**
         * Sets the number of threes. For a single valid delivery where three runs are scored off the bat, this value should be 1;
         * {@link #withBatterRuns(int)} should be 3; and {@link #withValidDeliveries(int)} should be 1.
         * @param threes The number of times a three was scored from the bat
         * @return This builder
         */
        public Builder withThrees(int threes) {
            this.threes = threes;
            return this;
        }

        /**
         * Sets the number of balls that were struck along the ground to the boundary (so this excludes when 4 runs are
         * run by the batters, or when 4 wides, byes, or leg-byes occur). For a single valid delivery the boundary is
         * scored off the bat, this value should be 1; {@link #withBatterRuns(int)} should be 4; and
         * {@link #withValidDeliveries(int)} should be 1.
         * @param fours The number of boundaries (for four) hit off the bat.
         * @return This builder.
         */
        public Builder withFours(int fours) {
            this.fours = fours;
            return this;
        }

        /**
         * Sets the number of balls that were struck over boundary on the full (so this excludes the unlikely case where
         * the batters run 6 runs). For a single valid delivery where a six is hit, this value should be 1;
         * {@link #withBatterRuns(int)} should be 6; and {@link #withValidDeliveries(int)} should be 1.
         * @param sixes The number of sixes hit.
         * @return This builder.
         */
        public Builder withSixes(int sixes) {
            this.sixes = sixes;
            return this;
        }

        /**
         * @param count The number of legal deliveries (i.e. number of balls that are not wides or no-balls)
         * @return This builder
         */
        public Builder withValidDeliveries(int count) {
            this.validDeliveries = count;
            return this;
        }

        /**
         * Sets the invalid balls. Note a situation where a single ball results in 4 wides the score would
         * have <code>.withInvalidDeliveries(1).withWides(4)</code>
         * @param count The number of balls that were wide or no-balls.
         * @return This builder
         * @see #withWides(int)
         * @see #withNoBalls(int)
         */
        public Builder withInvalidDeliveries(int count) {
            this.invalidDeliveries = count;
            return this;
        }

        /**
         * Creates a builder from a score instance
         * @param score The score to base the builder on
         * @return A new builder
         */
        public static Builder from(Score score) {
            return new Builder()
                .withThrees(score.threes())
                .withTwos(score.twos())
                .withSingles(score.singles())
                .withBatterRuns(score.batterRuns())
                .withValidDeliveries(score.validDeliveries())
                .withInvalidDeliveries(score.invalidDeliveries())
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

        private static final Score[] COMMON = new Score[]{EMPTY, DOT_BALL, SIX, FOUR, THREE, TWO, SINGLE, WIDE, NO_BALL, WICKET, BYE, LEG_BYE};

        public Score build() {
            Score score = new Score(batterRuns, wides, noBalls, legByes, byes, penaltyRuns, wickets, dots, singles, twos, threes, fours, sixes, validDeliveries, invalidDeliveries);
            for (Score cached : COMMON) {
                if (score.equals(cached)) {
                    return cached;
                }
            }
            return score;
        }

    }
}
