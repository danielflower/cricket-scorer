package com.danielflower.crickam.scorer;

public class PlayerBuilder {
	private int id;
	private String givenName;
	private String familyName;
	private String fullName;
	private Handedness battingHandedness = Handedness.RightHanded;
	private BowlingStyle bowlingStyle;
	private PlayingRole playingRole = PlayingRole.ALL_ROUNDER;
	private Gender gender = Gender.OTHER;

	public PlayerBuilder setGivenName(String givenName) {
		this.givenName = givenName;
		return this;
	}

	public PlayerBuilder setGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public PlayerBuilder setFamilyName(String familyName) {
		this.familyName = familyName;
		return this;
	}

	public PlayerBuilder setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public PlayerBuilder setBattingHandedness(Handedness battingHandedness) {
		this.battingHandedness = battingHandedness;
		return this;
	}

	public PlayerBuilder setPlayingRole(PlayingRole playingRole) {
		this.playingRole = playingRole;
		return this;
	}

	public PlayerBuilder setBowlingStyle(BowlingStyle bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
		return this;
	}

	public PlayerBuilder setBowlingStyle(BowlingStyleBuilder bowlingStyle) {
		return setBowlingStyle(bowlingStyle.build());
	}

	public PlayerBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public Player build() {
		fullName = (fullName == null) ? givenName + " " + familyName : fullName;
		bowlingStyle = (bowlingStyle == null) ? bowlingStyle : BowlingStyleBuilder.medium().setHandedness(battingHandedness).build();
		return new Player(id, gender, givenName, familyName, fullName, battingHandedness, bowlingStyle, playingRole);
	}

	public static PlayerBuilder aPlayer() {
		return new PlayerBuilder();
	}
}