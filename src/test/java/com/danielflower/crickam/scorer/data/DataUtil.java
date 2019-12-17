package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Gender;
import com.danielflower.crickam.scorer.Handedness;
import com.danielflower.crickam.scorer.PlayerBuilder;
import com.danielflower.crickam.utils.ImmutableList;

import java.util.UUID;

class DataUtil {
    static PlayerBuilder player(String name) {
        String[] names = name.split(" ");
        return new PlayerBuilder()
            .setGivenNames(ImmutableList.of(names[0]))
            .setFamilyName(names[names.length - 1])
            .setFullName(name)
            .setBattingHandedness(Handedness.RightHanded)
            .setGender(Gender.MALE)
            .setId(UUID.randomUUID().toString());

    }
}
