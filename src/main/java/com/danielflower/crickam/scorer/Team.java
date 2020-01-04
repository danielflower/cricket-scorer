package com.danielflower.crickam.scorer;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

/**
 * A cricket team
 * <p>Note that when a team plays in a match, the actual batting order is specified with the {@link LineUp} class.</p>
 * <p>Use {@link #team()} to get a {@link Builder} to create a team.</p>
 */
public class Team {
    private final String id;
    private final String shortName;
    private final TeamLevel level;
    private final String name;
    private final String teamColour;

    public TeamLevel level() {
        return level;
    }

    public String name() {
        return name;
    }

    private Team(String id, String name, String shortName, TeamLevel level, String teamColour) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.shortName = requireNonNull(shortName);
        this.level = requireNonNull(level);
        this.teamColour = teamColour;
    }

    public String id() {
        return id;
    }

    public String toString() {
        return name;
    }

    /**
     * @return An abbreviation of the team, such as &quot;NZL&quot;
     */
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

        /**
         * @param shortName An abbreviation of the team, such as &quot;NZL&quot;
         * @return This builder
         */
        public Builder withShortName(String shortName) {
            this.shortName = shortName;
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
            String id = requireNonNullElseGet(this.id, () -> UUID.randomUUID().toString());
            return new Team(id, name, shortName, level, teamColour);
        }
    }
}


