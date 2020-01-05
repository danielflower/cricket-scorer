package com.danielflower.crickam.scorer;


import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A cricket player
 * <p>To create players, use {@link #player()} to get a new {@link Builder}.</p>
 */
public final class Player {

	private final Gender gender;
	private final String id;
	private final String familyName;
    private final String givenName;
    private final ImmutableList<String> formalGivenNames;
    private final String fullName;
	private final Handedness battingHandedness;
    private final Handedness bowlingHandedness;
	private final PlayingRole playingRole;
    private final String initials;

    Player(String id, Gender gender, String givenName, ImmutableList<String> formalGivenNames, String familyName,
           String fullName, Handedness battingHandedness, Handedness bowlingHandedness,
           PlayingRole playingRole, String initials) {
        this.id = requireNonNull(id);
        this.gender = gender;
        this.givenName = givenName;
        this.formalGivenNames = requireNonNull(formalGivenNames);
        this.fullName = requireNonNull(fullName);
        this.battingHandedness = battingHandedness;
        this.familyName = requireNonNull(familyName);
        this.bowlingHandedness = bowlingHandedness;
        this.playingRole = playingRole;
        this.initials = initials;
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

    public Optional<Gender> gender() {
        return Optional.ofNullable(gender);
    }

    public String id() {
        return id;
    }

    public String givenName() {
        return givenName;
    }

    public ImmutableList<String> formalGivenNames() {
	    return formalGivenNames;
    }

    public String formalName() {
        return formalGivenNames.stream().collect(Collectors.joining(" ")) + " " + familyName();
    }

    public String familyName() {
        return familyName;
    }

    public String fullName() {
        return fullName;
    }

    public Optional<Handedness> battingHandedness() {
        return Optional.ofNullable(battingHandedness);
    }

    public Optional<Handedness> bowlingHandedness() {
        return Optional.ofNullable(bowlingHandedness);
    }

    public Optional<PlayingRole> playingRole() {
        return Optional.ofNullable(playingRole);
    }

    public String initialsWithSurname() {
	    return initials + " " + familyName;
    }

    public String initials() {
        return initials;
    }

    /**
     * Creates a player builder. Consider using {@link #player(String)} instead.
     * @return A player builder.
     */
    public static Builder player() {
	    return new Builder();
    }

    /**
     * @param name The full name of the player, such as &quot;Luteru Ross Poutoa Lote Taylor&quot;
     * @return A player builder formal given names and family name set.
     */
    public static Builder player(String name) {
	    return player(name.split("\\s+"));
    }

    /**
     * @param names The full name of the player, such as <code>&quot;Luteru&quot;, &quot;Ross&quot;, &quot;Poutoa&quot;, &quot;Lote&quot;, &quot;Taylor&quot;</code>
     * @return A player builder formal given names and family name set.
     */
    public static Builder player(String... names) {
	    if (names.length < 2) {
            throw new IllegalArgumentException("Names should have at least two items");
        }
        ImmutableList<String> list = ImmutableList.of(names);
        Builder builder = player();
        int size = list.size();
        if (size >= 4 && list.get(size - 3).equals("van") && list.get(size - 2).equals("der")) {
            list = list.subList(0, size - 4).add(list.get(size - 3) + " " + list.get(size - 2) + " " + list.get(size - 1));
        } else if (size >= 3 && list.get(size - 2).matches("d[aeiou]")) {
            list = list.subList(0, size - 3).add(list.get(size - 2) + " " + list.get(size - 1));
        }
        return builder
            .withFormalGivenNames(list.removeLast())
            .withFamilyName(list.last().orElseThrow());
    }

    /**
     * A builder of {@link Player} objects.
     */
    public static final class Builder {
        private String id;
        private ImmutableList<String> formalGivenNames;
        private String givenName;
        private String familyName;
        private String fullName;
        private Handedness battingHandedness;
        private Handedness bowlingHandedness;
        private PlayingRole playingRole;
        private Gender gender;
        private String initials;

        /**
         * @param formalGivenNames The formal given names of the player, such as <code>&quot;Luteru&quot;, &quot;Ross&quot;, &quot;Poutoa&quot;, &quot;Lote&quot;</code> for Ross Taylor.
         * @return This builder
         */
        public Builder withFormalGivenNames(ImmutableList<String> formalGivenNames) {
            this.formalGivenNames = formalGivenNames;
            return this;
        }

        public Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        /**
         * @param familyName The family name of the player, for example &quot;Taylor&quot; for Ross Taylor.
         * @return This builder
         */
        public Builder withFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        /**
         * Leave unset to infer this from the given name and full name.
         * @param fullName The name this player is known as, for example &quot;Ross Taylor&quot;
         * @return This builder
         */
        public Builder withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        /**
         * @param handedness the hand that this person predominantly bats and bowls with.
         * @return This builder
         */
        public Builder withHandedness(Handedness handedness) {
            return withBattingHandedness(handedness)
                .withBowlingHandedness(handedness);
        }

        public Builder withBattingHandedness(Handedness battingHandedness) {
            this.battingHandedness = battingHandedness;
            return this;
        }

        public Builder withPlayingRole(PlayingRole playingRole) {
            this.playingRole = playingRole;
            return this;
        }

        public Builder withBowlingHandedness(Handedness bowlingHandedness) {
            this.bowlingHandedness = bowlingHandedness;
            return this;
        }

        /**
         * Associates a unique ID for this player
         * @param id A unique ID that identifies this player
         * @return This builder
         */
        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        /**
         * @param givenName The given name that this person is generally known as, for example &quot;Ross&quot; for Luteru Ross Poutoa Lote Taylor
         * @return This builder
         */
        public Builder withGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        /**
         * @param initials The initials of the player's formal given names. This is inferred if unset.
         * @return This builder
         */
        public Builder withInitials(String initials) {
            this.initials = initials;
            return this;
        }

        public Player build() {
            String id = this.id != null ? this.id : UUID.randomUUID().toString();
            Objects.requireNonNullElse(this.givenName, this.formalGivenNames);
            ImmutableList<String> givenNames = this.formalGivenNames != null ? this.formalGivenNames : ImmutableList.of(this.givenName);
            String givenName = this.givenName != null ? this.givenName : givenNames.get(0);
            String fullName = this.fullName != null ? this.fullName : givenName + " " + familyName;
            String initials = this.initials != null ? this.initials : formalGivenNames.stream().map(s -> String.valueOf(s.charAt(0))).collect(Collectors.joining());
            return new Player(id, gender, givenName, givenNames, familyName, fullName, battingHandedness, bowlingHandedness, playingRole, initials);
        }
    }
}