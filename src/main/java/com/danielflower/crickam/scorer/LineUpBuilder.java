package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;

public final class LineUpBuilder {
    private ImmutableList<Player> players;
    private Team team;
    private boolean isPlayingAtHome;
    private Player captain;
    private Player wicketKeeper;

    public LineUpBuilder withBattingOrder(ImmutableList<Player> players) {
        this.players = players;
        return this;
    }

    public LineUpBuilder withTeam(Team team) {
        this.team = team;
        return this;
    }

    public LineUpBuilder withPlayingAtHome(boolean isPlayingAtHome) {
        this.isPlayingAtHome = isPlayingAtHome;
        return this;
    }

    public LineUpBuilder withCaptain(Player captain) {
        this.captain = captain;
        return this;
    }

    public LineUpBuilder withWicketKeeper(Player wicketKeeper) {
        this.wicketKeeper = wicketKeeper;
        return this;
    }

    public LineUp build() {
        return new LineUp(players, team, isPlayingAtHome, captain, wicketKeeper);
    }
}