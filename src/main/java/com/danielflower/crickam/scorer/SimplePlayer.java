package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * A very simple implementation of a player that just holds the player's name.
 */
@Immutable
public class SimplePlayer implements Player {

    private final String fullName;

    public SimplePlayer(String fullName) {
        this.fullName = fullName;
    }

    @Nonnull
    @Override
    public String name() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
