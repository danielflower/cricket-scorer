package com.danielflower.crickam.scorer;

import java.util.Date;

import static com.danielflower.crickam.scorer.PlayerBuilder.aPlayer;

public class BatsmanInningsBuilder {
	private Player player = aPlayer().build();
	private Balls ballsSoFarInInnings = new Balls();
	private int numberCameIn = 1;
	private Date inningsStartTime = new Date();

	private BatsmanInningsBuilder() {}

	public static BatsmanInningsBuilder aBatsmanInnings() {
		return new BatsmanInningsBuilder();
	}

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

	public BatsmanInningsBuilder setInningsStartTime(Date inningsStartTime) {
		this.inningsStartTime = inningsStartTime;
		return this;
	}


	public BatsmanInnings build() {
		return new BatsmanInnings(player, ballsSoFarInInnings, numberCameIn, inningsStartTime);
	}
}