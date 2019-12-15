package com.danielflower.crickam.scorer;

public class Venue {

	private final String name;
	private final String location;

	public Venue(String name, String location) {
		this.name = name;
		this.location = location;
	}

    public String name() {
        return name;
    }

    public String location() {
        return location;
    }
}
