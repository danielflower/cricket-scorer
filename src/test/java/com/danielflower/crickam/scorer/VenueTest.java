package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class VenueTest {

    @Test
    public void itCanBeBuilt() {
        Venue venue = aVenue()
            .withName("Lords")
            .withCity("London")
            .withTerritory("United Kingdom")
            .withTimeZone(TimeZone.getTimeZone("Europe/London"))
            .build();
        assertThat(venue.name(), is("Lords"));
        assertThat(venue.city(), is("London"));
        assertThat(venue.territory(), is("United Kingdom"));
        assertThat(venue.timeZone().getID(), is("Europe/London"));
    }

    public static Venue.Builder aVenue() {
        return new Venue.Builder()
            .withName("Eden Park")
            .withCity("Auckland")
            .withTerritory("New Zealand")
            .withTimeZone(TimeZone.getTimeZone("Pacific/Auckland"));
    }

}