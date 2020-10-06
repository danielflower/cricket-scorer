package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static java.util.Objects.requireNonNull;

/**
 * The series that a match is part of
 */
@Immutable
public final class Series {

	private final String id;
	private final ImmutableList<Team> teams;
	private final String name;

	private Series(String id, ImmutableList<Team> teams, String name) {
        this.id = requireNonNull(id);
        this.teams = requireNonNull(teams);
        this.name = requireNonNull(name);
    }

    public @Nonnull String id() {
        return id;
    }

    public @Nonnull ImmutableList<Team> teams() {
        return teams;
    }

    public @Nonnull String name() {
        return name;
    }

    public static @Nonnull Builder series() {
        return new Builder();
    }

    public static final class Builder {
        public String id;
        public ImmutableList<Team> teams;
        public String name;

        public @Nonnull Builder withId(String id) {
            this.id = id;
            return this;
        }

        public @Nonnull Builder withTeams(ImmutableList<Team> teams) {
            this.teams = teams;
            return this;
        }

        public @Nonnull Builder withName(String name) {
            this.name = name;
            return this;
        }

        public @Nonnull Series build() {
            return new Series(id, teams, name);
        }
    }

}
