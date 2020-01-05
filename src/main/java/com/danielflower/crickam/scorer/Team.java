package com.danielflower.crickam.scorer;

import static java.util.Objects.requireNonNull;

/**
 * A cricket team
 * <p>Note that when a team plays in a match, the actual batting order is specified with the {@link LineUp} class.</p>
 * <p>Use {@link #team()} to get a {@link Builder} to create a team.</p>
 * <p>This class is designed to be inherited if you wish to add custom data to the model.</p>
 */
public class Team {
    private final String shortName;
    private final String name;

    protected Team(Builder builder) {
        this.name = requireNonNull(builder.name);
        this.shortName = requireNonNull(builder.shortName);
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

    public static Builder team() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String shortName;

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


