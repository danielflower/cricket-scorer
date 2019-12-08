package com.danielflower.crickam.scorer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Series {

	public final String id;
	public final Set<Team> teams = new HashSet<>();
	public final String name;
	public final List<Match> matches = new ArrayList<>();

	public Series(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public int gameNumberInSeries(String matchId) {
		int counter = 0;
		for (Match match : matches) {
			counter++;
			if (match.matchID.equals(matchId)) {
				return counter;
			}
		}
		throw new RuntimeException("The match " + matchId + " does not appear to be in this series");
	}

}
