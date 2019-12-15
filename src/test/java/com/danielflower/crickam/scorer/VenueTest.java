package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class VenueTest {

    @Test
    public void itCanBeBuilt() {
        Venue venue = aVenue()
            .withName("Lords")
            .withLocation("London")
            .build();
        assertThat(venue.name(), is("Lords"));
        assertThat(venue.location(), is("London"));
    }

    public static VenueBuilder aVenue() {
        return new VenueBuilder()
            .withName("Eden Park")
            .withLocation("Auckland");
    }

}