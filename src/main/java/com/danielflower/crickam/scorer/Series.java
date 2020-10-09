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
	private final String name;

	private Series(String id, String name) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
    }

    public @Nonnull String id() {
        return id;
    }

    public @Nonnull String name() {
        return name;
    }

    public static @Nonnull Builder series() {
        return new Builder();
    }

    public static final class Builder {
        public String id;
        public String name;

        public @Nonnull Builder withId(String id) {
            this.id = id;
            return this;
        }

        public @Nonnull Builder withName(String name) {
            this.name = name;
            return this;
        }

        public @Nonnull Series build() {
            return new Series(id, name);
        }
    }

}
