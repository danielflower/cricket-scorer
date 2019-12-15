package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TeamTest {

    @Test
    public void canBeMade() {
        Team team = aTeam().withName("Blah").build();
        assertThat(team.getName(), is("Blah"));
    }

    public static TeamBuilder aTeam() {
        return new TeamBuilder()
            .withId(UUID.randomUUID().toString())
            .withLevel(TeamLevel.International)
            .withName("New Zealand")
            .withShortName("NZL")
            .withTeamColour("#000000")
            .withSquad(Set.of(
                PlayerTest.aPlayer().build()
            ));
    }

}