package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.TimeZone;

/**
 * A stadium or ground where a match is played.
 * <p>Use {@link #venue()} to create a builder.</p>
 */
@Immutable
public class Venue {

	private final String name;
	private final String city;
	private final String territory;
	private final TimeZone timeZone;

    protected Venue(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.city = builder.city;
        this.territory = builder.territory;
        this.timeZone = Objects.requireNonNull(builder.timeZone);
    }

    /**
     * @return The name of the venue, such as &quot;Eden Park&quot;
     */
    @Nonnull
    public String name() {
        return name;
    }

    /**
     * @return The city (or town) that this venue is in.
     */
    @Nullable
    public String city() {
        return city;
    }

    /**
     * @return The country or territory that the venue is in
     */
    @Nullable
    public String territory() {
        return territory;
    }

    /**
     * @return The timezone at the venue.
     */
    @Nonnull
    public TimeZone timeZone() {
        return timeZone;
    }

    /**
     * @return A new venue builder.
     */
    public static @Nonnull Builder venue() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String city;
        private String territory;
        private TimeZone timeZone;

        /**
         * @param name The name of the venue, such as &quot;Eden Park&quot;
         * @return This builder
         */
        public @Nonnull Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param city The city (or town) that this venue is in, such as &quot;Auckland&quot;
         * @return This builder
         */
        public @Nonnull Builder withCity(@Nullable String city) {
            this.city = city;
            return this;
        }

        /**
         * @param territory The country or territory that the venue is in, such as
         * @return This builder
         */
        public @Nonnull Builder withTerritory(@Nullable String territory) {
            this.territory = territory;
            return this;
        }

        /**
         * @param timeZone The timezone at the venue, for example <code>TimeZone.getTimeZone(&quot;Pacific/Auckland&quot;)</code>
         * @return This builder
         */
        public @Nonnull Builder withTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * @return A venue
         */
        public @Nonnull Venue build() {
            return new Venue(this);
        }
    }
}
