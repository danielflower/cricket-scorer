package com.danielflower.crickam.scorer;

import java.time.Instant;

public class BatsmanInningsBuilder {
	private Player player;
	private Balls ballsSoFarInInnings;
	private int numberCameIn;
	private Instant inningsStartTime;

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


	public BatsmanInnings build() {
		return new BatsmanInnings(player, ballsSoFarInInnings, numberCameIn, inningsStartTime);
	}
}