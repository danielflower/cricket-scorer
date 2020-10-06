package com.danielflower.crickam.scorer;


import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A cricket player
 * <p>To create players, use {@link #player()} to get a new {@link Builder}.</p>
 * <p>This class is designed to be inherited if you wish to add custom data to the model.</p>
 */
@Immutable
public class Player {

    private final String familyName;
    private final String givenName;
    private final ImmutableList<String> formalGivenNames;
    private final String fullName;
    private final String initials;

	protected Player(Builder builder) {
        Objects.requireNonNullElse(builder.givenName, builder.formalGivenNames);
        this.formalGivenNames = builder.formalGivenNames != null ? builder.formalGivenNames : ImmutableList.of(builder.givenName);
        this.givenName = builder.givenName != null ? builder.givenName : builder.formalGivenNames.get(0);
        this.fullName = builder.fullName != null ? builder.fullName : givenName + " " + builder.familyName;
        this.initials = builder.initials != null ? builder.initials : builder.formalGivenNames.stream().map(s -> String.valueOf(s.charAt(0))).collect(Collectors.joining());
        this.familyName = requireNonNull(builder.familyName);
    }

	public @Nonnull String toString() {
		return initialsWithSurname();
	}

    public @Nonnull String givenName() {
        return givenName;
    }

    public @Nonnull ImmutableList<String> formalGivenNames() {
	    return formalGivenNames;
    }

    public @Nonnull String formalName() {
        return formalGivenNames.stream().collect(Collectors.joining(" ")) + " " + familyName();
    }

    public @Nonnull String familyName() {
        return familyName;
    }

    public @Nonnull String fullName() {
        return fullName;
    }

    public @Nonnull String initialsWithSurname() {
	    return initials + " " + familyName;
    }

    public @Nonnull String initials() {
        return initials;
    }

    /**
     * Creates a player builder. Consider using {@link #player(String)} instead.
     * @return A player builder.
     */
    public static @Nonnull Builder player() {
	    return new Builder();
    }

    /**
     * @param name The full name of the player, such as &quot;Luteru Ross Poutoa Lote Taylor&quot;
     * @return A player builder formal given names and family name set.
     */
    public static @Nonnull Builder player(String name) {
	    return player(name.split("\\s+"));
    }

    /**
     * @param names The full name of the player, such as <code>&quot;Luteru&quot;, &quot;Ross&quot;, &quot;Poutoa&quot;, &quot;Lote&quot;, &quot;Taylor&quot;</code>
     * @return A player builder formal given names and family name set.
     */
    public static @Nonnull Builder player(String... names) {
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
            .withFamilyName(list.last());
    }

    /**
     * A builder of {@link Player} objects.
     */
    public static class Builder {
        private ImmutableList<String> formalGivenNames;
        private String givenName;
        private String familyName;
        private String fullName;
        private String initials;

        /**
         * @param formalGivenNames The formal given names of the player, such as <code>&quot;Luteru&quot;, &quot;Ross&quot;, &quot;Poutoa&quot;, &quot;Lote&quot;</code> for Ross Taylor.
         * @return This builder
         */
        public @Nonnull Builder withFormalGivenNames(ImmutableList<String> formalGivenNames) {
            this.formalGivenNames = formalGivenNames;
            return this;
        }

        /**
         * @param familyName The family name of the player, for example &quot;Taylor&quot; for Ross Taylor.
         * @return This builder
         */
        public @Nonnull Builder withFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        /**
         * Leave unset to infer this from the given name and full name.
         * @param fullName The name this player is known as, for example &quot;Ross Taylor&quot;
         * @return This builder
         */
        public @Nonnull Builder withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        /**
         * @param givenName The given name that this person is generally known as, for example &quot;Ross&quot; for Luteru Ross Poutoa Lote Taylor
         * @return This builder
         */
        public @Nonnull Builder withGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        /**
         * @param initials The initials of the player's formal given names. This is inferred if unset.
         * @return This builder
         */
        public @Nonnull Builder withInitials(String initials) {
            this.initials = initials;
            return this;
        }

        public @Nonnull Player build() {
            return new Player(this);
        }
    }
}