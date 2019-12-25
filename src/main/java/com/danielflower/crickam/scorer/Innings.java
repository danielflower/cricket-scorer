package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.*;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.utils.ImmutableListCollector.toImmutableList;
import static java.util.Objects.requireNonNull;

public final class Innings {


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
    private final FixedData data;
    private final State state;

    Innings onEvent(MatchEvent event) {
        if (state == State.COMPLETED) {
            throw new IllegalStateException("No events can be added after innings completion");
        }
        if (event instanceof BallCompletedEvent && state != State.IN_PROGRESS) {
            throw new IllegalStateException("Cannot process a ball when the innings state is " + state);
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
            BowlerInnings bi = getBowlerInnings(e.bowler());
            if (bi == null) {
                bi = BowlerInnings.newInnings(e.bowler());
                bowlerInningses = bowlerInningses.add(bi);
            }

            if (e.striker() == null || e.nonStriker() == null) {
                Optional<Over> previousOver = overs.last();
                if (previousOver.isPresent()) {
                    striker = previousOver.get().nonStriker();
                    nonStriker = previousOver.get().striker();
                } else {
                    striker = this.batters.get(0);
                    nonStriker = this.batters.get(1);
                }
            }
            if (e.striker() != null) {
                striker = findBatterInnings(e.striker());
            }
            if (e.nonStriker() != null) {
                nonStriker = findBatterInnings(e.nonStriker());
            }


            currentOver = Over.newOver(overs.size(), striker, nonStriker, e.bowler(), e.ballsInOver(), e.startTime());
            overs = overs.add(currentOver);
            newState = State.IN_PROGRESS;
        } else if (event instanceof BallCompletedEvent) {
            newState = State.IN_PROGRESS;
            if (currentStriker().isEmpty() || currentNonStriker().isEmpty())
                throw new IllegalStateException("There is only one batter in. Make sure an "
                    + OverStartingEvent.class.getSimpleName() + " has been raised at the beginning of the over and that a "
                    + BatterInningsStartingEvent.class.getSimpleName() + " is called after a wicket");
            BallCompletedEvent e = (BallCompletedEvent) event;
            striker = e.striker().isEmpty() ? currentStriker().get() : findBatterInnings(e.striker().get());
            nonStriker = e.nonStriker().isEmpty() ? currentNonStriker().get() : findBatterInnings(e.nonStriker().get());

            Player bowler = e.bowler().isEmpty() ? currentOver().get().bowler() : e.bowler().get();
            Player fielder = e.fielder().orElse(null);
            Dismissal dismissal = e.dismissal().isEmpty() ? null : new Dismissal(e.dismissal().get(), e.dismissedBatter().orElse(striker.player()), bowler, fielder);
            Ball ball = new Ball(balls.size() + 1, striker.player(), nonStriker.player(), overs.last().get().legalBalls() + 1, bowler,
                e.delivery().orElse(null), e.swing().orElse(null), e.trajectoryAtImpact().orElse(null), e.runsScored(), dismissal, e.playersCrossed(), fielder, e.dateCompleted());
            balls = balls.add(ball);

            currentOver = currentOver().get().onBall(ball);
            overs = overs.removeLast().copy().add(currentOver);

            Partnership currentPartnership = currentPartnership();
            partnerships = partnerships.removeLast().copy().add(currentPartnership.onBall(ball));

            BowlerInnings bi = getBowlerInnings(bowler);
            if (bi == null) {
                bi = BowlerInnings.newInnings(bowler).onBall(currentOver, ball);
                bowlerInningses = bowlerInningses.add(bi);
            } else {
                bi = bi.onBall(currentOver, ball);
                bowlerInningses = new ImmutableList<>();
                for (BowlerInnings existing : this.bowlerInningses) {
                    if (existing.bowler().equals(bowler)) {
                        bowlerInningses = bowlerInningses.add(bi);
                    } else {
                        bowlerInningses = bowlerInningses.add(existing);
                    }
                }
            }

            striker = striker.onBall(ball);
            nonStriker = nonStriker.onBall(ball);
            batters = new ImmutableList<>();
            for (BatterInnings existing : this.batterInningsList()) {
                if (existing.isSameInnings(striker)) {
                    batters = batters.add(striker);
                } else if (existing.isSameInnings(nonStriker)) {
                    batters = batters.add(nonStriker);
                } else {
                    batters = batters.add(existing);
                }
            }

            if (dismissal != null) {
                if (dismissal.batter().equals(striker.player())) {
                    striker = null;
                } else if (dismissal.batter().equals(nonStriker.player())) {
                    nonStriker = null;
                }
            }

            if (e.playersCrossed()) {
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
            endTime = ((InningsCompletedEvent) event).time();
            striker = null;
            nonStriker = null;
        } else if (event instanceof BatterInningsStartingEvent) {
            BatterInningsStartingEvent e = (BatterInningsStartingEvent) event;
            Player batter = e.batter() != null ? e.batter() : yetToBat.get(0);
            yetToBat = yetToBat.stream().filter(p -> !p.equals(batter)).collect(toImmutableList());
            BatterInnings newBatterInnings = BatterInnings.newInnings(batter, batters.size());
            batters = batters.add(newBatterInnings);
            Partnership newPartnership = Partnership.newPartnership(partnerships.size(), striker == null ? nonStriker : striker, newBatterInnings);
            partnerships = partnerships.add(newPartnership);
            if (striker == null) {
                striker = newBatterInnings;
            } else {
                nonStriker = newBatterInnings;
            }
        }

        return new Innings(data, partnerships, striker, nonStriker, batters, yetToBat, overs, currentOver, endTime, balls, bowlerInningses, newState);
    }

    private static class FixedData {
        private final Match match;
        private final LineUp battingTeam;
        private final LineUp bowlingTeam;
        private final int inningsNumber;
        private final Instant startTime;
        private final int numberOfScheduledOvers;

        private FixedData(Match match, LineUp battingTeam, LineUp bowlingTeam, int inningsNumber, Instant startTime, int numberOfScheduledOvers) {
            this.match = match;
            this.battingTeam = battingTeam;
            this.bowlingTeam = bowlingTeam;
            this.inningsNumber = inningsNumber;
            this.startTime = startTime;
            this.numberOfScheduledOvers = numberOfScheduledOvers;
        }
    }


    public Optional<Over> currentOver() {
        return Optional.ofNullable(currentOver);
    }

    public Optional<BatterInnings> currentStriker() {
        return Optional.ofNullable(currentStriker);
    }

    public Optional<BatterInnings> currentNonStriker() {
        return Optional.ofNullable(currentNonStriker);
    }

    public Partnership currentPartnership() {
        return partnerships.last().get();
    }


    public ImmutableList<Partnership> partnerships() {
        return partnerships;
    }

    public ImmutableList<BatterInnings> batterInningsList() {
        return batters;
    }

    public ImmutableList<Player> yetToBat() {
        return yetToBat;
    }

    public ImmutableList<Over> overs() {
        return overs;
    }

    public Balls balls() {
        return balls;
    }

    public boolean allOut() {
        return yetToBat.size() == 0 && (currentStriker().isEmpty() || currentNonStriker().isEmpty());
    }

    public int wicketsRemaining() {
        return yetToBat.size() + 1;
    }

    public State state() {
        return state;
    }

    private Innings(FixedData fixedData, ImmutableList<Partnership> partnerships, BatterInnings currentStriker, BatterInnings currentNonStriker, ImmutableList<BatterInnings> batters, ImmutableList<Player> yetToBat, ImmutableList<Over> overs, Over currentOver, Instant endTime, Balls balls, ImmutableList<BowlerInnings> bowlerInningses, State state) {
        if (currentStriker != null && currentNonStriker != null && currentStriker.isSameInnings(currentNonStriker)) {
            throw new IllegalArgumentException("The striker and non-striker were both set to " + currentStriker);
        }
        this.data = requireNonNull(fixedData);
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

    static Innings newInnings(Match match, LineUp battingTeam, LineUp bowlingTeam, ImmutableList<Player> openers, int inningsNumber, Instant startTime, int numberOfScheduledOvers) {
        FixedData fixedData = new FixedData(match, battingTeam, bowlingTeam, inningsNumber, startTime, numberOfScheduledOvers);

        BatterInnings currentStriker = BatterInnings.newInnings(openers.get(0), 1);
        BatterInnings currentNonStriker = BatterInnings.newInnings(openers.get(1), 2);

        ImmutableList<Partnership> partnerships = ImmutableList.of(Partnership.newPartnership(1, currentStriker, currentNonStriker));
        ImmutableList<BatterInnings> batters = ImmutableList.of(currentStriker, currentNonStriker);

        ImmutableList<Player> yetToBat = battingTeam.getPlayers().stream().filter(p -> !openers.contains(p)).collect(toImmutableList());
        return new Innings(fixedData, partnerships, currentStriker, currentNonStriker, batters, yetToBat, new ImmutableList<>(), null, null, new Balls(), new ImmutableList<>(), State.NOT_STARTED);
    }

    public ImmutableList<BowlerInnings> bowlerInningsList() {
        return bowlerInningses;
    }


    public int numberOfBallsRemaining() {
        int totalBalls = numberOfScheduledOvers() * 6;
        return totalBalls - balls().score().balls();
    }

    public boolean isFinished() {
        return allOut() || numberOfBallsRemaining() == 0;
    }

    public int getNumberOfScheduledBalls() {
        return numberOfScheduledOvers() * 6;
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

    public Match match() {
        return data.match;
    }

    public LineUp battingTeam() {
        return data.battingTeam;
    }

    public LineUp bowlingTeam() {
        return data.bowlingTeam;
    }

    public int inningsNumber() {
        return data.inningsNumber;
    }

    public Instant startTime() {
        return data.startTime;
    }

    public Instant endTime() {
        return endTime;
    }

    public int numberOfScheduledOvers() {
        return data.numberOfScheduledOvers;
    }

    public int maidens() {
        return (int) overs.stream().filter(Over::isMaiden).count();
    }

}


