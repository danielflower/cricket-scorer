package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlayerTest {

    @Test
    public void canCreate() {
        Player player = aPlayer().withGender(Gender.MALE)
            .withGivenNames(ImmutableList.of("Kane", "Stuart"))
            .withFamilyName("Williamson")
            .build();
        assertThat(player.gender(), is(Gender.MALE));
        assertThat(player.familyName(), is("Williamson"));
        assertThat(player.givenName(), is("Kane"));
        assertThat(player.fullName(), is("Kane Stuart Williamson"));
    }


    public static Player.Builder aPlayer() {
        return new Player.Builder()
            .withId(UUID.randomUUID().toString())
            .withBattingHandedness(Handedness.RightHanded)
            .withBowlingHandedness(Handedness.RightHanded)
            .withFamilyName("Taylor")
            .withGivenNames(ImmutableList.of("Ross"))
            .withGender(Gender.MALE)
            .withPlayingRole(PlayingRole.BATTER);
    }



}