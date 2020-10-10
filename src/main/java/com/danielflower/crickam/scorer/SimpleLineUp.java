package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A very simple implementation of a line up that can be used instead of implementing your own {@link LineUp}
 */
public class SimpleLineUp implements LineUp<Player> {
    private final ImmutableList<Player> players;
    private final Player captain;
    private final Player wicketKeeper;
    private final String teamName;

    public SimpleLineUp(ImmutableList<Player> battingOrder, @Nullable Player captain, @Nullable Player wicketKeeper, String teamName) {
        this.players = battingOrder;
        this.captain = captain;
        this.wicketKeeper = wicketKeeper;
        this.teamName = teamName;
    }

    public static Builder lineUp() {
        return new Builder();
    }

    @Nonnull
    @Override
    public ImmutableList<Player> battingOrder() {
        return players;
    }

    @Nullable
    @Override
    public Player captain() {
        return captain;
    }

    @Nullable
    @Override
    public Player wicketKeeper() {
        return wicketKeeper;
    }

    @Nonnull
    @Override
    public String teamName() {
        return teamName;
    }

    /**
     * Tries to find a player based on their name
     * @param name The {@link Player#name()} value
     * @return The found player, or null if unsure
     */
    @Nullable
    public Player findPlayer(String name) {
        Player exact = battingOrder().stream().filter(p ->
            p.name().equalsIgnoreCase(name)
        ).findFirst().orElse(null);
        if (exact != null) {
            return exact;
        }
        String surname = name.split(" ")[name.split(" ").length - 1];
        List<Player> partials = battingOrder().stream().filter(p ->
            p.name().toLowerCase().endsWith(" " + surname.toLowerCase())
        ).collect(Collectors.toList());
        return partials.isEmpty() ? null : partials.get(0);
    }

    public static class Builder {

        private ImmutableList<Player> battingOrder;
        private Player captain;
        private Player wicketKeeper;
        private String teamName;

        public Builder withBattingOrder(ImmutableList<Player> battingOrder) {
            this.battingOrder = battingOrder;
            return this;
        }

        public Builder withCaptain(Player captain) {
            this.captain = captain;
            return this;
        }

        public Builder withWicketKeeper(Player wicketKeeper) {
            this.wicketKeeper = wicketKeeper;
            return this;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public SimpleLineUp build() {
            return new SimpleLineUp(battingOrder, captain, wicketKeeper, teamName);
        }
    }
}
