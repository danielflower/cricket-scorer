package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;
import com.danielflower.crickam.utils.ImmutableListCollector;

import java.util.stream.Stream;

public final class PlayerBuilder {
	private String id;
	private ImmutableList<String> givenNames;
	private String familyName;
	private String fullName;
	private Handedness battingHandedness = Handedness.RightHanded;
	private BowlingStyle bowlingStyle;
	private PlayingRole playingRole = PlayingRole.ALL_ROUNDER;
	private Gender gender = Gender.OTHER;

	public PlayerBuilder setGivenNames(ImmutableList<String> givenNames) {
		this.givenNames = givenNames;
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

	public PlayerBuilder setId(String id) {
		this.id = id;
		return this;
	}

    public PlayerBuilder withName(String name) {
        String[] bits = name.split(" ");
        return setFullName(name)
            .setFamilyName(bits[bits.length - 1])
            .setGivenNames(Stream.of(bits).skip(1).collect(ImmutableListCollector.toImmutableList()));
    }

	public Player build() {
		fullName = (fullName == null) ? String.join(" ", givenNames) + " " + familyName : fullName;
		BowlingStyle bowlingStyleToUse = (bowlingStyle == null) ? BowlingStyleBuilder.medium().setHandedness(battingHandedness).build() : bowlingStyle;
		return new Player(id, gender, givenNames, familyName, fullName, battingHandedness, bowlingStyleToUse, playingRole);
	}
}