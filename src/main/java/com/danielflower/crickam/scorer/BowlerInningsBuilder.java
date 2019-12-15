package com.danielflower.crickam.scorer;

public class BowlerInningsBuilder {
    private Player player;

    public BowlerInningsBuilder withBowler(Player player) {
        this.player = player;
        return this;
    }

    public BowlerInnings build() {
        return new BowlerInnings(player);
    }
}