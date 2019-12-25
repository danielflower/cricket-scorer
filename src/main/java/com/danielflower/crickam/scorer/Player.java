package com.danielflower.crickam.scorer;


import com.danielflower.crickam.scorer.utils.ImmutableList;
import com.danielflower.crickam.scorer.utils.ImmutableListCollector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A cricket player
 * <p>To create players, use {@link #player()} to get a new {@link Builder}.</p>
 */
public final class Player {

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
        this.id = requireNonNull(id);
        this.gender = requireNonNull(gender);
        this.givenNames = requireNonNull(givenNames);
        this.fullName = requireNonNull(fullName);
        this.battingHandedness = requireNonNull(battingHandedness);
        this.familyName = requireNonNull(familyName);
        this.bowlingStyle = requireNonNull(bowlingStyle);
        this.playingRole = requireNonNull(playingRole);
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

    public static Builder player() {
	    return new Builder();
    }

    /**
     * A builder of {@link Player} objects.
     */
    public static final class Builder {
        private String id;
        private ImmutableList<String> givenNames;
        private String familyName;
        private String fullName;
        private Handedness battingHandedness = Handedness.RightHanded;
        private BowlingStyle bowlingStyle;
        private PlayingRole playingRole = PlayingRole.ALL_ROUNDER;
        private Gender gender;

        public Builder withGivenNames(ImmutableList<String> givenNames) {
            this.givenNames = givenNames;
            return this;
        }

        public Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder withBattingHandedness(Handedness battingHandedness) {
            this.battingHandedness = battingHandedness;
            return this;
        }

        public Builder withPlayingRole(PlayingRole playingRole) {
            this.playingRole = playingRole;
            return this;
        }

        public Builder withBowlingStyle(BowlingStyle bowlingStyle) {
            this.bowlingStyle = bowlingStyle;
            return this;
        }

        public Builder withBowlingStyle(BowlingStyle.Builder bowlingStyle) {
            return withBowlingStyle(bowlingStyle.build());
        }

        /**
         * Associates a unique ID for this player
         * @param id A unique ID
         * @return This builder
         */
        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            String[] bits = name.split(" ");
            return withFullName(name)
                .withFamilyName(bits[bits.length - 1])
                .withGivenNames(Stream.of(bits).skip(1).collect(ImmutableListCollector.toImmutableList()));
        }

        public Player build() {
            fullName = (fullName == null) ? String.join(" ", givenNames) + " " + familyName : fullName;
            BowlingStyle bowlingStyleToUse = (bowlingStyle == null) ? BowlingStyle.Builder.medium().withHandedness(battingHandedness).build() : bowlingStyle;
            return new Player(id, gender, givenNames, familyName, fullName, battingHandedness, bowlingStyleToUse, playingRole);
        }
    }
}