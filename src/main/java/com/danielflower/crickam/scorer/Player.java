package com.danielflower.crickam.scorer;


import java.util.Objects;


public class Player {
	public final Gender gender;
	public final int id;
	public final String givenName;
	public final String familyName;
	public final String fullName;
	public final Handedness battingHandedness;
	public final BowlingStyle bowlingStyle;
	public final PlayingRole playingRole;

	public Player() {
		this(-1, Gender.OTHER, null, null, null, null, null, null);
	}

	public Player(int id, Gender gender, String givenName, String familyName, String fullName, Handedness battingHandedness, BowlingStyle bowlingStyle, PlayingRole playingRole) {
		this.id = id;
		this.gender = gender;
		this.fullName = fullName;
		this.battingHandedness = battingHandedness;
		this.givenName = givenName;
		this.familyName = familyName;
		this.bowlingStyle = bowlingStyle;
		this.playingRole = playingRole;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getFullName() {
		return fullName;
	}

	public Handedness getBattingHandedness() {
		return battingHandedness;
	}

	public BowlingStyle getBowlingStyle() {
		return bowlingStyle;
	}

	public PlayingRole getPlayingRole() {
		return playingRole;
	}

	public String toString() {
		return familyName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return id == player.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}


