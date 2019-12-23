package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompleteEvent;
import com.danielflower.crickam.scorer.events.OverCompletedEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.danielflower.crickam.utils.ImmutableListCollector.toImmutableList;

public class Innings {

    private final ImmutableList<Partnership> partnerships;
    private final ImmutableList<BatsmanInnings> batters;
    private final ImmutableList<Player> yetToBat;
    private final ImmutableList<Over> overs;
    private final Over currentOver;
    private final Date endTime;
    private final Balls balls;
    private final ImmutableList<BowlerInnings> bowlerInningses;
    private final ImmutableList<BowlingSpell> spells;
    private final FixedData data;

    Innings onEvent(MatchEvent event) {
        ImmutableList<Partnership> partnerships = this.partnerships;
        ImmutableList<BatsmanInnings> batters = this.batters;
        ImmutableList<Player> yetToBat = this.yetToBat;
        ImmutableList<Over> overs = this.overs;
        Date endTime = this.endTime;
        Balls balls = this.balls;
        ImmutableList<BowlerInnings> bowlerInningses = this.bowlerInningses;
        ImmutableList<BowlingSpell> spells = this.spells;

        Over currentOver = this.currentOver;
        if (event instanceof OverStartingEvent) {
            OverStartingEvent e = (OverStartingEvent) event;
            BowlerInnings bi = new BowlerInnings(e.bowler(), new Balls(), new ImmutableList<>());
            spells = spells.add(new BowlingSpell(bi, 1, new ImmutableList<>(), new Balls()));
            currentOver = Over.newOver(overs.size(), getBatsmanInnings(e.striker()), getBatsmanInnings(e.nonStriker()), spells.last().get(), e.ballsInOver());
            overs = overs.add(currentOver);
        }
        if (event instanceof BallCompleteEvent) {
            BallCompleteEvent e = (BallCompleteEvent) event;
            BatsmanInnings striker = e.striker() == null ? currentStriker() : getBatsmanInnings(e.striker());
            BatsmanInnings nonStriker = e.nonStriker() == null ? currentNonStriker() : getBatsmanInnings(e.nonStriker());
            BowlingSpell bowlingSpell = spells.last().get();
            Dismissal dismissal = e.dismissal() == null ? null : new Dismissal(e.dismissal(), striker, bowlingSpell, e.fielder());
            Ball ball = new Ball(balls.size() + 1, striker, nonStriker, overs.last().get().legalBalls() + 1, bowlingSpell,
                e.delivery(), e.swing(), e.trajectoryAtImpact(), e.runsScored(), dismissal, e.playersCrossed(), e.fielder(), e.dateCompleted());
            balls = balls.add(ball);

            currentOver = currentOver().get().onBall(ball);
            overs = overs.removeLast().copy().add(currentOver);

            Partnership currentPartnership = currentPartnership();
            partnerships = partnerships.removeLast().copy().add(currentPartnership.onBall(ball));
        }
        if (event instanceof OverCompletedEvent) {
            currentOver = null;
        }

        return new Innings(data, partnerships, batters, yetToBat, overs, currentOver, endTime, balls, bowlerInningses, spells);
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

    public BatsmanInnings currentStriker() {
        if (currentOver().isPresent()) {
            return currentOver().get().striker();
        } else {
            return batters.get(0);
        }
    }

    public BatsmanInnings currentNonStriker() {
        if (currentOver().isPresent()) {
            return currentOver().get().nonStriker();
        } else {
            return batters.get(1);
        }
    }

    public Partnership currentPartnership() {
        return partnerships.last().get();
    }


    public ImmutableList<Partnership> partnerships() {
        return partnerships;
    }

    public ImmutableList<BatsmanInnings> batterInningsList() {
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
        return yetToBat.size() == 0 && (currentStriker() == null || currentNonStriker() == null);
    }

    public int wicketsRemaining() {
        return yetToBat.size() + 1;
    }

    private Innings(FixedData fixedData, ImmutableList<Partnership> partnerships, ImmutableList<BatsmanInnings> batters, ImmutableList<Player> yetToBat, ImmutableList<Over> overs, Over currentOver, Date endTime, Balls balls, ImmutableList<BowlerInnings> bowlerInningses, ImmutableList<BowlingSpell> spells) {
        this.data = fixedData;
        this.partnerships = partnerships;
        this.batters = batters;
        this.overs = overs;
        this.currentOver = currentOver;
        this.endTime = endTime;
        this.balls = balls;
        this.bowlerInningses = bowlerInningses;
        this.spells = spells;
        this.yetToBat = yetToBat;
    }

    static Innings newInnings(Match match, LineUp battingTeam, LineUp bowlingTeam, ImmutableList<Player> openers, int inningsNumber, Instant startTime, int numberOfScheduledOvers) {
        FixedData fixedData = new FixedData(match, battingTeam, bowlingTeam, inningsNumber, startTime, numberOfScheduledOvers);

        BatsmanInnings currentStriker = BatsmanInnings.newInnings(openers.get(0), 1);
        BatsmanInnings currentNonStriker = BatsmanInnings.newInnings(openers.get(1), 2);

        ImmutableList<Partnership> partnerships = ImmutableList.of(Partnership.newPartnership(1, currentStriker, currentNonStriker));
        ImmutableList<BatsmanInnings> batters = ImmutableList.of(currentStriker, currentNonStriker);

        ImmutableList<Player> yetToBat = battingTeam.getPlayers().stream().filter(p -> !openers.contains(p)).collect(toImmutableList());
        return new Innings(fixedData, partnerships, batters, yetToBat, new ImmutableList<Over>(), null, null, new Balls(), new ImmutableList<>(), new ImmutableList<>());
    }



    private BatsmanInnings other(BatsmanInnings batsmanInnings) {
        return currentStriker() == batsmanInnings ?
            currentNonStriker() : currentStriker();
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


    public BatsmanInnings getBatsmanInnings(Player target) {
        Objects.requireNonNull((Object) target, "target");
        for (BatsmanInnings batter : batters) {
            if (target.equals(batter.getPlayer())) {
                return batter;
            }
        }
        throw new IllegalStateException("Could not find innings for " + target);
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

    public int numberOfScheduledOvers() {
        return data.numberOfScheduledOvers;
    }
}


