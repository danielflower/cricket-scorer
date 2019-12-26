package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.utils.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TeamTest {

    @Test
    public void canBeMade() {
        Team team = aTeam().withName("Blah").build();
        assertThat(team.name(), is("Blah"));
    }

    public static Team.Builder aTeam() {
        return new Team.Builder()
            .withId(UUID.randomUUID().toString())
            .withLevel(TeamLevel.International)
            .withName("New Zealand")
            .withShortName("NZL")
            .withTeamColour("#000000")
            .withSquad(ImmutableList.of(
                PlayerTest.aPlayer().build()
            ));
    }

}