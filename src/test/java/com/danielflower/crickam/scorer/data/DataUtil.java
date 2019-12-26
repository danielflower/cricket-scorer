package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Gender;
import com.danielflower.crickam.scorer.Handedness;
import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.Player;

import java.util.UUID;

class DataUtil {
    static Player.Builder player(String name) {
        String[] names = name.split(" ");
        return new Player.Builder()
            .withGivenNames(ImmutableList.of(names[0]))
            .withFamilyName(names[names.length - 1])
            .withFullName(name)
            .withBattingHandedness(Handedness.RightHanded)
            .withBowlingHandedness(Handedness.RightHanded)
            .withGender(Gender.MALE)
            .withId(UUID.randomUUID().toString());

    }
}
