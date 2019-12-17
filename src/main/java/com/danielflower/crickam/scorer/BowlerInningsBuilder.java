package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class BowlerInningsBuilder {
    private Player player;
    private Balls balls;
    private ImmutableList<BowlingSpell> spells;

    public BowlerInningsBuilder withBowler(Player player) {
        this.player = player;
        return this;
    }

    public BowlerInnings build() {
        return new BowlerInnings(player, balls, spells);
    }
}