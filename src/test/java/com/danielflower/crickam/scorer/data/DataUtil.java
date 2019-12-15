package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Gender;
import com.danielflower.crickam.scorer.Handedness;
import com.danielflower.crickam.scorer.PlayerBuilder;

import java.util.Collections;
import java.util.UUID;

class DataUtil {
    static PlayerBuilder player(String name) {
        String[] names = name.split(" ");
        return new PlayerBuilder()
            .setGivenNames(Collections.singletonList(names[0]))
            .setFamilyName(names[names.length - 1])
            .setFullName(name)
            .setBattingHandedness(Handedness.RightHanded)
            .setGender(Gender.MALE)
            .setId(UUID.randomUUID().toString());

    }
}
