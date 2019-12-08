package com.danielflower.crickam.scorer;

public class Guard {

    public static void notNull(String name, Object value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " should not be null");
        }
    }

}
