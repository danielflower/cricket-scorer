package com.danielflower.crickam.scorer;

public class Venue {

	public final String name;
	public final String location;
	public final Integer averageFirstInningsScore;
	public final Integer averageSecondInningsScore;

	public Venue(String name, String location, Integer averageFirstInningsScore, Integer averageSecondInningsScore) {
		this.name = name;
		this.location = location;
		this.averageFirstInningsScore = averageFirstInningsScore;
		this.averageSecondInningsScore = averageSecondInningsScore;
	}

}
