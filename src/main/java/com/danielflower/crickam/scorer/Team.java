package com.danielflower.crickam.scorer;

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
    private final String name;

    protected Team(Builder builder) {
        this.id = requireNonNullElseGet(builder.id, () -> UUID.randomUUID().toString());
        this.name = requireNonNull(builder.name);
        this.shortName = requireNonNull(builder.shortName);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    /**
     * @return An abbreviation of the team, such as &quot;NZL&quot;
     */
    public String shortName() {
        return shortName;
    }

    public String toString() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static Builder team() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String shortName;

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

        public Team build() {
            return new Team(this);
        }
    }
}


