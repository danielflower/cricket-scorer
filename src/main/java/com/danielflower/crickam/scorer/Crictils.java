package com.danielflower.crickam.scorer;

import java.util.OptionalInt;

public class Crictils {

    public static OptionalInt toOptional(Integer value) {
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    static Integer toInteger(OptionalInt value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return value.getAsInt();
        }
    }
}
