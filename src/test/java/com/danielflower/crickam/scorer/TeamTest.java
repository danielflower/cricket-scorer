package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class TeamTest {

    @Test
    public void canBeMade() {

    }

    public static TeamBuilder aTeam() {
        return new TeamBuilder()
            .withId(UUID.randomUUID().toString())
            .withLevel(TeamLevel.International)
            .withName("New Zealand")
            .withShortName("NZL")
            .withTeamColour("#000000")
            .withPlayers(List.of(
                PlayerTest.aPlayer(),
            ));
    }

}