package com.danielflower.crickam.scorer;

import java.util.List;

public class Team {
	private final String id;
	public final String shortName;
	private final TeamLevel level;
	private final List<Player> players;
	private final String name;
	public final String teamColour;

	public TeamLevel getLevel() {
        return level;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public String getName() {
        return name;
    }

	public Team(String id, String name, String shortName, List<Player> players, TeamLevel level, String teamColour) {
		this.id = id;
		this.name = name;
        this.shortName = shortName;
        this.level = level;
		this.players = players;
		this.teamColour = teamColour;
    }

    public LineUp odiSquad() {
        return LineUp.random(this);
    }

	public String getId() {
		return id;
	}

    public String toString() {
		return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}


