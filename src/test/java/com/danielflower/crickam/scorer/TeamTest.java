package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

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
            .withName("New Zealand")
            .withShortName("NZL");
    }

}