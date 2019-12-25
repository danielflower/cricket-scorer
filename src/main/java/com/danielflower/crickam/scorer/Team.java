package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public final class Team {
	private final String id;
	public final String shortName;
	private final TeamLevel level;
	private final ImmutableList<Player> squad;
	private final String name;
	public final String teamColour;

	public TeamLevel getLevel() {
        return level;
    }

    public ImmutableList<Player> getSquad() {
        return squad;
    }
    public void addPlayer(Player player) {
        squad.add(player);
    }
    public String getName() {
        return name;
    }

	public Team(String id, String name, String shortName, ImmutableList<Player> squad, TeamLevel level, String teamColour) {
		this.id = id;
		this.name = name;
        this.shortName = shortName;
        this.level = level;
		this.squad = squad;
		this.teamColour = teamColour;
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


