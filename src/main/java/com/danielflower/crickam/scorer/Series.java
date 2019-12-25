package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;

import static java.util.Objects.requireNonNull;

/**
 * The series that a match is part of
 */
public final class Series {

	public final String id;
	public final ImmutableList<Team> teams;
	public final String name;

	private Series(String id, ImmutableList<Team> teams, String name) {
        this.id = requireNonNull(id);
        this.teams = requireNonNull(teams);
        this.name = requireNonNull(name);
    }

	public static Builder series() {
	    return new Builder();
    }

	public static class Builder {
        public String id;
        public ImmutableList<Team> teams;
        public String name;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTeams(ImmutableList<Team> teams) {
            this.teams = teams;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Series build() {
            return new Series(id, teams, name);
        }
    }

}
