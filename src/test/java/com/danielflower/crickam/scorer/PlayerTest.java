package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PlayerTest {

    @Test
    public void canCreate() {
        Player player = aPlayer().setGender(Gender.MALE)
            .setGivenNames(List.of("Kane", "Stuart"))
            .setFamilyName("Williamson")
            .build();
        assertThat(player.gender(), is(Gender.MALE));
        assertThat(player.familyName(), is("Williamson"));
        assertThat(player.givenName(), is("Kane"));
        assertThat(player.fullName(), is("Kane Stuart Williamson"));
    }


    public static PlayerBuilder aPlayer() {
        return new PlayerBuilder()
            .setId(UUID.randomUUID().toString())
            .setBattingHandedness(Handedness.RightHanded)
            .setBowlingStyle(BowlingStyleBuilder.medium().setHandedness(Handedness.RightHanded).build())
            .setFamilyName("Taylor")
            .setGivenNames(List.of("Ross"))
            .setPlayingRole(PlayingRole.BATSMAN);
    }



}