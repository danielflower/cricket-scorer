package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public class BatsmanInningsBuilder {
	private Player player;
	private Balls ballsSoFarInInnings;
	private int numberCameIn;
	private Instant inningsStartTime;
    private Balls balls = new Balls();
    private Instant inningsEndTime;
    private Dismissal dismissal;

    public BatsmanInningsBuilder setPlayer(PlayerBuilder player) {
		this.player = player.build();
		return this;
	}

	public BatsmanInningsBuilder setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public BatsmanInningsBuilder setBallsSoFarInInnings(Balls ballsSoFarInInnings) {
		this.ballsSoFarInInnings = ballsSoFarInInnings;
		return this;
	}

	public BatsmanInningsBuilder setNumberCameIn(int numberCameIn) {
		this.numberCameIn = numberCameIn;
		return this;
	}

	public BatsmanInningsBuilder setInningsStartTime(Instant inningsStartTime) {
		this.inningsStartTime = inningsStartTime;
		return this;
	}

    public BatsmanInningsBuilder withBalls(Balls balls) {
        this.balls = balls;
        return this;
    }

    public BatsmanInningsBuilder withInningsEndTime(Instant inningsEndTime) {
        this.inningsEndTime = inningsEndTime;
        return this;
    }

    public BatsmanInningsBuilder withDismissal(Dismissal dismissal) {
        this.dismissal = dismissal;
        return this;
    }

    public BatsmanInnings build() {
		return new BatsmanInnings(player, ballsSoFarInInnings, balls, numberCameIn, inningsStartTime, inningsEndTime, Optional.ofNullable(dismissal));
	}
}