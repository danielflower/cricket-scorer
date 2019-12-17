package com.danielflower.crickam.scorer;


import com.danielflower.crickam.utils.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Player {

	private final Gender gender;
	private final String id;
	private final String familyName;
    private final ImmutableList<String> givenNames;
    private final String fullName;
	private final Handedness battingHandedness;
	private final BowlingStyle bowlingStyle;
	private final PlayingRole playingRole;

	Player(@NotNull String id, @NotNull Gender gender, @NotNull ImmutableList<String> givenNames, @NotNull String familyName,
                  @NotNull String fullName, @NotNull Handedness battingHandedness, @NotNull BowlingStyle bowlingStyle,
                  @NotNull PlayingRole playingRole) {
		this.id = id;
		this.gender = gender;
        this.givenNames = givenNames;
        this.fullName = fullName;
		this.battingHandedness = battingHandedness;
		this.familyName = familyName;
		this.bowlingStyle = bowlingStyle;
		this.playingRole = playingRole;
	}

	public String toString() {
		return familyName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return id().equals(player.id());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id());
	}

    public Gender gender() {
        return gender;
    }

    public String id() {
        return id;
    }

    public String givenName() {
        return givenNames.get(0);
    }

    public ImmutableList<String> givenNames() {
	    return givenNames;
    }

    public String familyName() {
        return familyName;
    }

    public String fullName() {
        return fullName;
    }

    public Handedness battingHandedness() {
        return battingHandedness;
    }

    public BowlingStyle bowlingStyle() {
        return bowlingStyle;
    }

    public PlayingRole playingRole() {
        return playingRole;
    }

}