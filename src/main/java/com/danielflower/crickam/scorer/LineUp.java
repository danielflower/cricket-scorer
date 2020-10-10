package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A team line up
 */
public interface LineUp<P extends Player> {

    /**
     * @return The playing team, in their beginning batting order
     */
    @Nonnull
	ImmutableList<P> battingOrder();

    /**
     * @return The captain of the team, if known
     */
    @Nullable
	P captain();

    /**
     * @return The primary wicket-keeper in this line up, if known
     */
    @Nullable
	P wicketKeeper();

    /**
     * @return The name of the team
     */
    @Nonnull
    String teamName();

    /**
     * Checks to see if two line ups are representing the same team.
     * <p>This may return a different result than calling {@link Object#equals(Object)} because the batting order or
     * captain may change, however it is still the same team.</p>
     * @param other The line up to compare again
     * @return Returns true if the 2 line-ups are the same team
     */
    default boolean sameTeam(@Nullable LineUp<?> other) {
        return this.equals(other);
    }


}


