package com.danielflower.crickam.scorer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The selected players from a team for match.
 * <p>Use {@link #lineUp()} to create a builder.</p>
 */
public final class LineUp {
	private final ImmutableList<Player> players;
	private final Team team;
	private final Player captain;
	private final Player wicketKeeper;

    private LineUp(ImmutableList<Player> players, Team team, Player captain, Player wicketKeeper) {
        this.players = requireNonNull(players, "players");
        this.team = requireNonNull(team, "team");
        this.captain = requireNonNull(captain, "captain");
        this.wicketKeeper = requireNonNull(wicketKeeper, "wicketKeeper");
    }

    /**
     * @return The expected batting order
     */
    public ImmutableList<Player> battingOrder() {
        return players;
    }

    /**
     * @return The team this line-up is for
     */
    public Team team() {
        return team;
    }

    /**
     * @return The designated captain
     */
    public Player captain() {
        return captain;
    }

    /**
     * @return The designated wicket keeper
     */
    public Player wicketKeeper() {
        return wicketKeeper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineUp lineUp = (LineUp) o;
        return Objects.equals(players, lineUp.players) &&
            Objects.equals(team, lineUp.team) &&
            Objects.equals(captain, lineUp.captain) &&
            Objects.equals(wicketKeeper, lineUp.wicketKeeper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players, team, captain, wicketKeeper);
    }

    @Override
    public String toString() {
        return team.name();
    }

    /**
     * @return A new builder
     */
    public static Builder lineUp() {
        return new Builder();
    }

    /**
     * Tries to find a player based on their name
     * @param name A surname, or given name plus surname, or initial plus surname
     * @return The found player, or empty if unsure
     */
    public Optional<Player> findPlayer(String name) {
        String[] bits = name.trim().split("\\W+");
        String familyName = bits[bits.length - 1];
        List<Player> matchingSurnames = players.stream().filter(p -> p.familyName().equalsIgnoreCase(familyName)).collect(Collectors.toList());
        if (matchingSurnames.isEmpty()) {
            return Optional.empty();
        } else if (matchingSurnames.size() == 1) {
            return Optional.of(matchingSurnames.get(0));
        }
        return matchingSurnames.stream()
            .filter(p -> p.givenName().toLowerCase().charAt(0) == bits[0].toLowerCase().charAt(0))
            .findFirst();
    }

    public static final class Builder {
        private ImmutableList<Player> players;
        private Team team;
        private Player captain;
        private Player wicketKeeper;

        /**
         * @param players The players in the order they are expected to bat in
         * @return This builder
         */
        public Builder withBattingOrder(ImmutableList<Player> players) {
            this.players = players;
            return this;
        }

        /**
         * @param team The team this line up is for
         * @return This builder
         */
        public Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        /**
         * @param captain The designated captain for this match
         * @return This builder
         */
        public Builder withCaptain(Player captain) {
            this.captain = captain;
            return this;
        }

        /**
         * @param wicketKeeper The designated wicket keeper for this match
         * @return Thie builder
         */
        public Builder withWicketKeeper(Player wicketKeeper) {
            this.wicketKeeper = wicketKeeper;
            return this;
        }

        /**
         * @return A newly created {@code LineUp}
         */
        public LineUp build() {
            return new LineUp(players, team, captain, wicketKeeper);
        }
    }
}


