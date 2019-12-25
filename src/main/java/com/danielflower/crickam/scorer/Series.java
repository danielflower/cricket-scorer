package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public final class Series {

	public final String id;
	public final ImmutableList<Team> teams;
	public final String name;
	public final ImmutableList<Match> matches;

	public Series(String id, ImmutableList<Team> teams, String name, ImmutableList<Match> matches) {
		this.id = id;
        this.teams = teams;
        this.name = name;
        this.matches = matches;
    }

	public int gameNumberInSeries(String matchId) {
		int counter = 0;
		for (Match match : matches) {
			counter++;
			if (match.matchID().equals(matchId)) {
				return counter;
			}
		}
		throw new RuntimeException("The match " + matchId + " does not appear to be in this series");
	}

}
