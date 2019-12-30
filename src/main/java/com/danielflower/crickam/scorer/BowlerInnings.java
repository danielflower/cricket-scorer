package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * A record of the overs bowled by a bowler in a single innings.
 */
public final class BowlerInnings {
    private final Player bowler;
    private final Balls balls;
    private final ImmutableList<BowlingSpell> spells;
    private final ImmutableList<Over> overs;
    private final int wickets;

    /**
     * @return The bowler
     */
    public Player bowler() {
        return bowler;
    }

    /**
     * @return All the balls in this innings
     */
    public Balls balls() {
        return balls;
    }

    /**
     * @return All the overs in this innings
     */
    public ImmutableList<Over> overs() {
        return overs;
    }

    /**
     * @return The spells this bowler has bowled. Any overs bowled consecutively by a bowler (i.e. where there is a
     * gap of just one over between two overs) are considered part of a single spell.
     */
    public ImmutableList<BowlingSpell> spells() {
        return spells;
    }

    /**
     * The number of wickets credited to the bowler, which may differ from the wickets reported by {@link #score()}
     * which includes dismissals such as run-outs
     *
     * @return The number of wickets credited to the bowler
     */
    public int wickets() {
        return wickets;
    }

    /**
     * @return The total score in this bowler's innings.
     */
    public Score score() {
        return balls.score();
    }


    /**
     * @return The number of maidens bowled in this innings.
     */
    public int maidens() {
        return (int) overs.stream().filter(over -> over.isMaiden() && over.balls().list().get(0).bowler().equals(bowler)).count();
    }

    private BowlerInnings(Player bowler, Balls balls, ImmutableList<BowlingSpell> spells, ImmutableList<Over> overs, int wickets) {
        this.bowler = bowler;
        this.balls = balls;
        this.spells = spells;
        this.overs = overs;
        this.wickets = wickets;
    }

    static BowlerInnings newInnings(Over over, Player bowler) {
        BowlingSpell spell = new BowlingSpell(bowler, 1, ImmutableList.of(over), new Balls(), 0);
        ImmutableList<BowlingSpell> spells = new ImmutableList<>();
        return new BowlerInnings(bowler, new Balls(), spells.add(spell), ImmutableList.of(over), 0);
    }

    BowlerInnings onBall(Over over, BallCompletedEvent ball) {
        BowlingSpell bowlingSpell = this.spells.last().get();
        Optional<Over> previousOver = bowlingSpell.overs().last();
        ImmutableList<BowlingSpell> spells;
        if (previousOver.isPresent() && (over.overNumber() - previousOver.get().overNumber()) > 2) {
            spells = this.spells.add(new BowlingSpell(bowler, bowlingSpell.spellNumber() + 1, ImmutableList.of(over), new Balls(), wickets).onBall(over, ball));
        } else {
            spells = this.spells.removeLast().add(bowlingSpell.onBall(over, ball));
        }
        int wickets = this.wickets;
        if (ball.dismissal().isPresent() && ball.dismissal().get().type().creditedToBowler()) {
            wickets++;
        }
        ImmutableList<Over> newOvers = addOverWithPreviousRemovedIfSame(overs, over);
        return new BowlerInnings(bowler, balls.add(ball), spells, newOvers, wickets);
    }

    @NotNull
    static ImmutableList<Over> addOverWithPreviousRemovedIfSame(ImmutableList<Over> overs, Over toAddOrReplace) {
        ImmutableList<Over> newOvers = overs;
        if (overs.last().get().overNumber() == toAddOrReplace.overNumber()) {
            newOvers = newOvers.subList(0, overs.size() - 2);
        }
        newOvers = newOvers.add(toAddOrReplace);
        return newOvers;
    }

    /**
     * @return The number of overs bowled, for example &quot;2&quot; if 2 complete overs have been bowled;
     * or &quot;2.3&quot; if 2 overs and 3 balls have been bowled.
     */
    public String overDotBallString() {
        Over over = overs.last().get();
        int count = overs.size();
        if (over.isComplete()) {
            return String.valueOf(count);
        } else {
            return (count - 1) + "." + over.validDeliveries();
        }
    }

    @Override
    public String toString() {
        Score s = balls.score();
        return bowler + "    " + overs.size() + " Overs; " + s.teamRuns() + " Runs; " + wickets() + " Wkts; " + s.runsPerOver() + " RPO; " + s.dots() + " 0s; " + s.fours() + " 4s; " + s.sixes() + " 6s";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BowlerInnings that = (BowlerInnings) o;
        return wickets == that.wickets &&
            Objects.equals(bowler, that.bowler) &&
            Objects.equals(balls, that.balls) &&
            Objects.equals(spells, that.spells) &&
            Objects.equals(overs, that.overs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bowler, balls, spells, overs, wickets);
    }
}


