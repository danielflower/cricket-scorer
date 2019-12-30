package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.toInteger;
import static com.danielflower.crickam.scorer.Crictils.toOptional;
import static com.danielflower.crickam.scorer.ImmutableList.emptyList;
import static com.danielflower.crickam.scorer.ImmutableList.toImmutableList;
import static java.util.Objects.requireNonNull;

/**
 * An innings in a match
 */
public final class Innings implements MatchEventListener<Innings> {

    public enum State {
        NOT_STARTED, IN_PROGRESS, BETWEEN_OVERS, DRINKS, LUNCH, TEA, RAIN_DELAY, COMPLETED;

    }

    private final ImmutableList<Partnership> partnerships;
    private final BatterInnings currentStriker;
    private final BatterInnings currentNonStriker;
    private final ImmutableList<BatterInnings> batters;
    private final ImmutableList<Player> yetToBat;
    private final ImmutableList<Over> overs;
    private final Over currentOver;
    private final Instant endTime;
    private final Balls balls;
    private final ImmutableList<BowlerInnings> bowlerInningses;
    private final InningsStartingEvent data;
    private final State state;
    private final Integer maxOvers;
    private final Integer maxBalls;
    private final Integer target;

    private Innings(InningsStartingEvent data, ImmutableList<Partnership> partnerships, BatterInnings currentStriker, BatterInnings currentNonStriker, ImmutableList<BatterInnings> batters, ImmutableList<Player> yetToBat, ImmutableList<Over> overs, Over currentOver, Instant endTime, Balls balls, ImmutableList<BowlerInnings> bowlerInningses, State state, Integer maxOvers, Integer maxBalls, Integer target) {
        this.maxOvers = maxOvers;
        this.maxBalls = maxBalls;
        this.target = target;
        if (currentStriker != null && currentNonStriker != null && currentStriker.sameInnings(currentNonStriker)) {
            throw new IllegalArgumentException("The striker and non-striker were both set to " + currentStriker);
        }
        this.data = requireNonNull(data);
        this.partnerships = requireNonNull(partnerships);
        this.currentStriker = currentStriker;
        this.currentNonStriker = currentNonStriker;
        this.batters = requireNonNull(batters);
        this.overs = requireNonNull(overs);
        this.currentOver = currentOver;
        this.endTime = endTime;
        this.balls = requireNonNull(balls);
        this.bowlerInningses = bowlerInningses;
        this.yetToBat = requireNonNull(yetToBat);
        this.state = requireNonNull(state);
    }

    static Innings newInnings(InningsStartingEvent event) {
        return new Innings(event, emptyList(), null, null, emptyList(), event.battingTeam().battingOrder(),
            emptyList(), null, null, new Balls(), emptyList(), State.NOT_STARTED,
            toInteger(event.maxOvers()), toInteger(event.maxBalls()), toInteger(event.target()));
    }

    public Innings onEvent(MatchEvent event) {
        if (state == State.COMPLETED) {
            throw new IllegalStateException("No events can be added after innings completion");
        }

        ImmutableList<BatterInnings> batters = this.batters;
        ImmutableList<Partnership> partnerships = this.partnerships;
        ImmutableList<Player> yetToBat = this.yetToBat;
        ImmutableList<Over> overs = this.overs;
        Instant endTime = this.endTime;
        Balls balls = this.balls;
        ImmutableList<BowlerInnings> bowlerInningses = this.bowlerInningses;
        State newState = this.state;
        BatterInnings striker = this.currentStriker;
        BatterInnings nonStriker = this.currentNonStriker;

        Over currentOver = this.currentOver;
        if (event instanceof OverStartingEvent) {
            OverStartingEvent e = (OverStartingEvent) event;
            striker = findBatterInnings(e.striker());
            nonStriker = findBatterInnings(e.nonStriker());
            currentOver = Over.newOver(e);
            overs = overs.add(currentOver);

            BowlerInnings bi = getBowlerInnings(e.bowler());
            if (bi == null) {
                bi = BowlerInnings.newInnings(currentOver, currentOver.bowler());
                bowlerInningses = bowlerInningses.add(bi);
            }
            newState = State.IN_PROGRESS;
        } else if (event instanceof BallCompletedEvent) {
            if (state != State.IN_PROGRESS) {
                throw new IllegalStateException("Cannot process a ball when the innings state is " + state);
            }
            if (currentStriker().isEmpty() || currentNonStriker().isEmpty())
                throw new IllegalStateException("There is only one batter in. Make sure an "
                    + OverStartingEvent.class.getSimpleName() + " has been raised at the beginning of the over and that a "
                    + BatterInningsStartingEvent.class.getSimpleName() + " is called after a wicket");
            BallCompletedEvent ball = (BallCompletedEvent) event;
            striker = findBatterInnings(ball.striker());
            nonStriker = findBatterInnings(ball.nonStriker());

            Over over = currentOver().orElseThrow();
            Player bowler = ball.bowler();
            balls = balls.add(ball);

            currentOver = over.onEvent(ball);
            overs = overs.removeLast().add(currentOver);

            Partnership currentPartnership = currentPartnership().get();
            partnerships = partnerships.removeLast().add(currentPartnership.onEvent(ball));

            BowlerInnings bi = getBowlerInnings(bowler);
            if (bi == null) {
                bi = BowlerInnings.newInnings(currentOver, bowler).onBall(currentOver, ball);
                bowlerInningses = bowlerInningses.add(bi);
            } else {
                bi = bi.onBall(currentOver, ball);
                bowlerInningses = emptyList();
                for (BowlerInnings existing : this.bowlerInningses) {
                    if (existing.bowler().equals(bowler)) {
                        bowlerInningses = bowlerInningses.add(bi);
                    } else {
                        bowlerInningses = bowlerInningses.add(existing);
                    }
                }
            }

            striker = striker.onEvent(ball);
            nonStriker = nonStriker.onEvent(ball);
            batters = emptyList();
            for (BatterInnings existing : this.batterInningsList()) {
                if (existing.sameInnings(striker)) {
                    batters = batters.add(striker);
                } else if (existing.sameInnings(nonStriker)) {
                    batters = batters.add(nonStriker);
                } else {
                    batters = batters.add(existing);
                }
            }


            if (ball.playersCrossed()) {
                BatterInnings temp = striker;
                striker = nonStriker;
                nonStriker = temp;
            }
        } else if (event instanceof OverCompletedEvent) {
            newState = State.BETWEEN_OVERS;
            currentOver = null;
        } else if (event instanceof InningsCompletedEvent) {
            newState = State.COMPLETED;
            currentOver = null;
            endTime = event.time().orElse(null);
            striker = null;
            nonStriker = null;
        } else if (event instanceof BatterInningsStartingEvent) {
            BatterInningsStartingEvent e = (BatterInningsStartingEvent) event;
            yetToBat = yetToBat.stream().filter(p -> !p.equals(e.batter())).collect(toImmutableList());
            BatterInnings newBatterInnings = BatterInnings.newInnings(e.batter(), batters.size() + 1);
            batters = batters.add(newBatterInnings);
            if (striker != null || nonStriker != null) {
                Partnership newPartnership = Partnership.newPartnership(partnerships.size() + 1, striker == null ? nonStriker.player() : striker.player(), newBatterInnings.player());
                partnerships = partnerships.add(newPartnership);
            }
            if (striker == null) {
                striker = newBatterInnings;
            } else if (nonStriker == null) {
                nonStriker = newBatterInnings;
            } else {
                throw new IllegalStateException("A new batter innings cannot start when there are already two batters in");
            }
        } else {

            if (this.currentStriker != null) {
                striker = this.currentStriker.onEvent(event);
                batters = batters.replace(this.currentStriker, striker);
            }
            if (this.currentNonStriker != null) {
                nonStriker = this.currentNonStriker.onEvent(event);
                batters = batters.replace(this.currentNonStriker, nonStriker);
            }

            Partnership currentValue = currentPartnership().orElse(null);
            if (currentValue != null) {
                partnerships = partnerships.replace(currentValue, currentValue.onEvent(event));
            }

            if (event instanceof BatterInningsEndedEvent) {
                BatterInningsEndedEvent e = (BatterInningsEndedEvent) event;
                Player dismissalBatter = e.batter();
                if (dismissalBatter.equals(striker.player())) {
                    striker = null;
                } else if (dismissalBatter.equals(nonStriker.player())) {
                    nonStriker = null;
                }
            }

        }

        return new Innings(data, partnerships, striker, nonStriker, batters, yetToBat, overs, currentOver, endTime, balls, bowlerInningses, newState, maxOvers, maxBalls, target);
    }


    /**
     * @return The over being bowled, or empty if between overs or before/after the innings has started
     */
    public Optional<Over> currentOver() {
        return Optional.ofNullable(currentOver);
    }

    /**
     * @return The batter currently facing. This may be empty before the innings starts or directly after a dismissal.
     */
    public Optional<BatterInnings> currentStriker() {
        return Optional.ofNullable(currentStriker);
    }

    /**
     * @return The batter at the non-facing end. This may be empty before the innings starts or directly after a dismissal.
     */
    public Optional<BatterInnings> currentNonStriker() {
        return Optional.ofNullable(currentNonStriker);
    }

    /**
     * @return The current batting partnership
     */
    public Optional<Partnership> currentPartnership() {
        return (currentStriker == null || currentNonStriker == null) ? Optional.empty() : partnerships.last();
    }

    /**
     * @return All the batting partnerships in the innings so far
     */
    public ImmutableList<Partnership> partnerships() {
        return partnerships;
    }

    /**
     * @return All the batters who have batted so far (including batters who are out and not-out) in the order they
     * came in.
     */
    public ImmutableList<BatterInnings> batterInningsList() {
        return batters;
    }

    /**
     * @return The players who have not yet batted, in the order they are expected to bat.
     */
    public ImmutableList<Player> yetToBat() {
        return yetToBat;
    }

    /**
     * @return The overs bowled so far in the innings.
     */
    public ImmutableList<Over> overs() {
        return overs;
    }

    /**
     * @return The balls bowled so far in the innings.
     */
    public Balls balls() {
        return balls;
    }

    /**
     * @return Gets the info on the bowlers who have bowled so far (in the order they first bowled in)
     */
    public ImmutableList<BowlerInnings> bowlerInningsList() {
        return bowlerInningses;
    }

    /**
     * @return The score in the innings so far. Access {@link Score#teamRuns()} for the total number of runs in this innings.
     */
    public Score score() {
        return balls.score();
    }

    /**
     * @return True if all of the batters (except one) have been dismissed.
     */
    public boolean allOut() {
        return yetToBat.size() == 0 && (currentStriker().isEmpty() || currentNonStriker().isEmpty());
    }

    /**
     * @return The number of dismissals left before {@link #allOut()} returns true.
     */
    public int wicketsRemaining() {
        return yetToBat.size() + 1;
    }

    /**
     * @return The state of the innings
     */
    public State state() {
        return state;
    }


    /**
     * @return The number of scheduled balls remaining in the innings, or empty if this innings does not have a limit.
     */
    public OptionalInt numberOfBallsRemaining() {
        OptionalInt scheduled = maxBalls();
        if (scheduled.isEmpty()) {
            return scheduled;
        }
        return OptionalInt.of(scheduled.getAsInt() - balls().score().validDeliveries());
    }

    /**
     * @return Returns true if the batting team is {@link #allOut()} or if this is a limited overs match and {@link #numberOfBallsRemaining()} is 0
     */
    public boolean isFinished() {
        return allOut() || numberOfBallsRemaining().orElse(-1) == 0;
    }

    private BatterInnings findBatterInnings(Player target) {
        requireNonNull(target, "target");
        for (BatterInnings batter : batters) {
            if (target.equals(batter.player())) {
                return batter;
            }
        }
        throw new IllegalStateException(target + " does not have a batter innings");
    }

    private BowlerInnings getBowlerInnings(Player target) {
        requireNonNull(target, "target");
        for (BowlerInnings bowlerInnings : bowlerInningses) {
            if (bowlerInnings.bowler().equals(target)) {
                return bowlerInnings;
            }
        }
        return null;
    }

    /**
     * Gets the number of the last ball as a string in the format <em>over.ball</em>, for example &quot;0.1&quot;
     * <p>After 6 balls but before an {@link OverCompletedEvent} it will return a value such as &quot;0.6&quot;
     * while after the over is completed it will be &quot;1.0&quot;</p>
     *
     * @return The number of the last ball
     */
    public String overDotBallString() {
        Optional<BallCompletedEvent> ball = balls.list().last();
        Optional<Over> over = overs.last();
        if (ball.isPresent() && over.isPresent()) {
            int b = ball.get().numberInOver();
            int o = over.get().overNumber();
            if (b == 6 && currentOver().isEmpty()) {
                b = 0;
                o++;
            }
            return o + "." + b;
        } else {
            return "0.0";
        }
    }

    /**
     * @return The target the batting team is aiming for in order to win, or empty if this is not the last innings
     * in the match
     */
    public OptionalInt target() {
        return toOptional(target);
    }

    /**
     * @return The team that is currently batting
     */
    public LineUp battingTeam() {
        return data.battingTeam();
    }

    /**
     * @return The team that is currently bowling
     */
    public LineUp bowlingTeam() {
        return data.bowlingTeam();
    }

    /**
     * @return 1 for the first innings in a match; 2 for the second etc
     */
    public int inningsNumber() {
        return data.inningsNumberForMatch();
    }

    /**
     * @return 1 for the first innings that the current batting team is batting for in a match; 2 for the second etc
     */
    public int inningsNumberForBattingTeam() {
        return data.inningsNumberForBattingTeam();
    }

    /**
     * @return The time the innings started
     */
    public Optional<Instant> startTime() {
        return data.time();
    }

    /**
     * @return The time the innings ended, if {@link #state()} is {@link State#COMPLETED}
     */
    public Optional<Instant> endTime() {
        return Optional.ofNullable(endTime);
    }

    /**
     * @return The current total number of scheduled balls in the innings, after adjustments such as reductions due
     * to bad weather. (Returns empty if there is no limit)
     * @see #originalMaxBalls()
     */
    public OptionalInt maxBalls() {
        return toOptional(maxBalls);
    }

    /**
     * @return The current total number of scheduled overs in the innings, after adjustments such as reductions due
     * to bad weather. (Returns empty if there is no limit)
     * @see #originalMaxOvers()
     */
    public OptionalInt maxOvers() {
        return toOptional(maxOvers);
    }

    /**
     * @return The number of scheduled balls at the beginning of the innings, or empty if there is no limit.
     * @see #maxBalls()
     */
    public OptionalInt originalMaxBalls() {
        return data.maxBalls();
    }

    /**
     * @return The number of scheduled overs in this match, or empty if there is no limit
     */
    public OptionalInt originalMaxOvers() {
        return data.maxOvers();
    }

    /**
     * @return The number of maidens in this innings
     */
    public int maidens() {
        return (int) overs.stream().filter(Over::isMaiden).count();
    }

    @Override
    public String toString() {
        return battingTeam().team().name() + " innings " + inningsNumber() + " - " +
            score().teamRuns() + "/" + score().wickets() + " from " + overDotBallString() + " ov";
    }

}


