package com.danielflower.crickam.scorer;

public final class VenueBuilder {
    private String name;
    private String location;

    public VenueBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public VenueBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public Venue build() {
        return new Venue(name, location);
    }
}