package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A cricket player
 * <p>The API is designed so that you can specify your own implementation, however for a very simple implementation
 * you can use {@link SimplePlayer}.</p>
 */
public interface Player {

    /**
     * @return The full (informal) name of the player, such as "Ross Taylor"
     */
    @Nonnull String name();

    /**
     * @return The name of the player to use on the scorecard, e.g. "LRPL Taylor"
     */
    default @Nonnull String scorecardName() {
        String[] bits = name().split(" ");
        if (bits.length == 0 || bits[0].length() < 1) return name();
        return bits[0].charAt(0) + " " + bits[bits.length - 1];
    }

    /**
     * Checks to see if two players are in fact the same person
     * @param other The other player to check, which may be null
     * @return True if they are the same player, otherwise false
     */
    default boolean samePlayer(@Nullable Player other) {
        return this.equals(other);
    }
}
