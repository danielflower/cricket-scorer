package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class Team {
	private final String id;
	public final String shortName;
	private final TeamLevel level;
	private final ImmutableList<Player> squad;
	private final String name;
	public final String teamColour;

	public TeamLevel level() {
        return level;
    }

    public ImmutableList<Player> squad() {
        return squad;
    }
    public String name() {
        return name;
    }

	private Team(String id, String name, String shortName, ImmutableList<Player> squad, TeamLevel level, String teamColour) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.shortName = requireNonNull(shortName);
        this.level = requireNonNull(level);
        this.squad = requireNonNull(squad);
		this.teamColour = teamColour;
    }

	public String id() {
		return id;
	}

    public String toString() {
		return name;
    }

    public String shortName() {
        return shortName;
    }

    public Optional<String> teamColour() {
        return Optional.ofNullable(teamColour);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static Builder team() {
	    return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String shortName;
        private ImmutableList<Player> players;
        private TeamLevel level;
        private String teamColour;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder withSquad(ImmutableList<Player> players) {
            this.players = players;
            return this;
        }

        public Builder withLevel(TeamLevel level) {
            this.level = level;
            return this;
        }

        public Builder withTeamColour(String teamColour) {
            this.teamColour = teamColour;
            return this;
        }

        public Team build() {
            return new Team(id, name, shortName, players, level, teamColour);
        }
    }
}


