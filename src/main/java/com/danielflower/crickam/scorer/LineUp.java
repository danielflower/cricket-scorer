package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public final class LineUp {
	public final ImmutableList<Player> players;
	public final Team team;
	public final boolean isPlayingAtHome;
	public final Player captain;
	public final Player wicketKeeper;

    public LineUp(ImmutableList<Player> players, Team team, boolean isPlayingAtHome, Player captain, Player wicketKeeper) {
        this.players = players;
        this.team = team;
        this.isPlayingAtHome = isPlayingAtHome;
        this.captain = captain;
        this.wicketKeeper = wicketKeeper;
    }

    public ImmutableList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return team.getName();
    }

}


