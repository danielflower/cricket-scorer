package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class TeamBuilder {
    private String id;
    private String name;
    private String shortName;
    private ImmutableList<Player> players;
    private TeamLevel level;
    private String teamColour;

    public TeamBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public TeamBuilder withSquad(ImmutableList<Player> players) {
        this.players = players;
        return this;
    }

    public TeamBuilder withLevel(TeamLevel level) {
        this.level = level;
        return this;
    }

    public TeamBuilder withTeamColour(String teamColour) {
        this.teamColour = teamColour;
        return this;
    }

    public Team build() {
        return new Team(id, name, shortName, players, level, teamColour);
    }
}