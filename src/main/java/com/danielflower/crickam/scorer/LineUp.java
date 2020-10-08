package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The selected players from a team for match.
 * <p>Use {@link #lineUp()} to create a builder.</p>
 * <p>This class is designed to be inherited if you wish to add custom data to the model.</p>
 */
@Immutable
public class LineUp {
	private final ImmutableList<Player> players;
	private final Team team;
	private final Player captain;
	private final Player wicketKeeper;

    protected LineUp(Builder builder) {
        this.players = requireNonNull(builder.players, "players");
        this.team = requireNonNull(builder.team, "team");
        this.captain = requireNonNull(builder.captain, "captain");
        this.wicketKeeper = requireNonNull(builder.wicketKeeper, "wicketKeeper");
    }

    /**
     * @return The expected batting order
     */
    public @Nonnull ImmutableList<Player> battingOrder() {
        return players;
    }

    /**
     * @return The team this line-up is for
     */
    public @Nonnull Team team() {
        return team;
    }

    /**
     * @return The designated captain
     */
    public @Nonnull Player captain() {
        return captain;
    }

    /**
     * @return The designated wicket keeper
     */
    public @Nonnull Player wicketKeeper() {
        return wicketKeeper;
    }

    @Override
    public String toString() {
        return team.name();
    }

    /**
     * @return A new builder
     */
    public static @Nonnull Builder lineUp() {
        return new Builder();
    }

    /**
     * Tries to find a player based on their name
     * @param name The {@link Player#name()} value
     * @return The found player, or null if unsure
     */
    public @Nullable Player findPlayer(String name) {
        Player exact = players.stream().filter(p ->
            p.name().equalsIgnoreCase(name)
        ).findFirst().orElse(null);
        if (exact != null) {
            return exact;
        }
        String surname = name.split(" ")[name.split(" ").length - 1];
        List<Player> partials = players.stream().filter(p ->
            p.name().toLowerCase().endsWith(" " + surname.toLowerCase())
        ).collect(Collectors.toList());
        return partials.isEmpty() ? null : partials.get(0);
    }

    public static class Builder {
        private ImmutableList<Player> players;
        private Team team;
        private Player captain;
        private Player wicketKeeper;

        /**
         * @param players The players in the order they are expected to bat in
         * @return This builder
         */
        public @Nonnull Builder withBattingOrder(ImmutableList<Player> players) {
            this.players = players;
            return this;
        }

        /**
         * @param team The team this line up is for
         * @return This builder
         */
        public @Nonnull Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        /**
         * @param captain The designated captain for this match
         * @return This builder
         */
        public @Nonnull Builder withCaptain(Player captain) {
            this.captain = captain;
            return this;
        }

        /**
         * @param wicketKeeper The designated wicket keeper for this match
         * @return Thie builder
         */
        public @Nonnull Builder withWicketKeeper(Player wicketKeeper) {
            this.wicketKeeper = wicketKeeper;
            return this;
        }

        /**
         * @return A newly created {@code LineUp}
         */
        public @Nonnull LineUp build() {
            return new LineUp(this);
        }
    }
}


