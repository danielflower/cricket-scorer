package com.danielflower.crickam.scorer;

import java.util.Objects;
import java.util.TimeZone;

/**
 * A stadium or ground where a match is played.
 * <p>Use {@link #venue()} to create a builder.</p>
 */
public final class Venue {

	private final String name;
	private final String city;
	private final String territory;
	private final TimeZone timeZone;

	Venue(String name, String city, String territory, TimeZone timeZone) {
        this.name = Objects.requireNonNull(name);
		this.city = city;
        this.territory = territory;
        this.timeZone = Objects.requireNonNull(timeZone);
    }

    /**
     * @return The name of the venue, such as &quot;Eden Park&quot;
     */
    public String name() {
        return name;
    }

    /**
     * @return The city (or town) that this venue is in.
     */
    public String city() {
        return city;
    }

    /**
     * @return The country or territory that the venue is in
     */
    public String territory() {
        return territory;
    }

    /**
     * @return The timezone at the venue.
     */
    public TimeZone timeZone() {
        return timeZone;
    }

    /**
     * @return A new venue builder.
     */
    public static Builder venue() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String city;
        private String territory;
        private TimeZone timeZone;

        /**
         * @param name The name of the venue, such as &quot;Eden Park&quot;
         * @return This builder
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param city The city (or town) that this venue is in, such as &quot;Auckland&quot;
         * @return This builder
         */
        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        /**
         * @param territory The country or territory that the venue is in, such as
         * @return This builder
         */
        public Builder withTerritory(String territory) {

            this.territory = territory;
            return this;
        }

        /**
         * @param timeZone The timezone at the venue, for example <code>TimeZone.getTimeZone(&quot;Pacific/Auckland&quot;)</code>
         * @return This builder
         */
        public Builder withTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * @return A venue
         */
        public Venue build() {
            return new Venue(name, city, territory, timeZone);
        }
    }
}
