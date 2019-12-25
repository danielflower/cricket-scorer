package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;

import java.util.Optional;

/**
 * A record of the overs bowled by a bowler in a single innings.
 */
public final class BowlerInnings {
    private final Player player;
    private final Balls balls;
    private final ImmutableList<BowlingSpell> spells;
    private final ImmutableList<Over> overs;

    /**
     * @return The bowler
     */
    public Player bowler() {
        return player;
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
     * @return The total score in this bowler's innings.
     */
    public Score score() {
        return balls.score();
    }


    /**
     * @return The number of maidens bowled in this innings.
     */
    public int maidens() {
        return (int) overs.stream().filter(Over::isMaiden).count();
    }

    private BowlerInnings(Player player, Balls balls, ImmutableList<BowlingSpell> spells, ImmutableList<Over> overs) {
        this.player = player;
        this.balls = balls;
        this.spells = spells;
        this.overs = overs;
    }

    static BowlerInnings newInnings(Player player) {
        BowlingSpell spell = new BowlingSpell(player, 1, new ImmutableList<>(), new Balls());
        ImmutableList<BowlingSpell> spells = new ImmutableList<>();
        return new BowlerInnings(player, new Balls(), spells.add(spell), new ImmutableList<>());
    }

    BowlerInnings onBall(Over over, Ball ball) {
        BowlingSpell bowlingSpell = this.spells.last().get();
        Optional<Over> previousOver = bowlingSpell.overs().last();
        ImmutableList<BowlingSpell> spells;
        if (previousOver.isPresent() && (over.numberInInnings() - previousOver.get().numberInInnings()) > 2) {
            spells = this.spells.add(new BowlingSpell(player, bowlingSpell.spellNumber() + 1, new ImmutableList<>(), new Balls()).onBall(over, ball));
        } else {
            spells = this.spells.removeLast().copy().add(bowlingSpell.onBall(over, ball));
        }
        return new BowlerInnings(player, balls.add(ball), spells, overs.add(over));
    }

    @Override
    public String toString() {
        Score s = balls.score();
        return player + "    " + overs.size() + " Overs; " + s.teamRuns() + " Runs; " + s.wickets() + " Wkts; " + s.runsPerOver() + " RPO; " + s.dots() + " 0s; " + s.fours() + " 4s; " + s.sixes() + " 6s";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlerInnings that = (BowlerInnings) o;

        if (!player.equals(that.player)) return false;
        if (!balls.equals(that.balls)) return false;
        if (!spells.equals(that.spells)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + balls.hashCode();
        result = 31 * result + spells.hashCode();
        return result;
    }
}


