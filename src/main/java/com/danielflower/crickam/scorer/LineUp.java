package com.danielflower.crickam.scorer;

import com.danielflower.crickam.stats.Distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LineUp {
	public final List<Player> players;
	public final Team team;
	public final boolean isPlayingAtHome;
	public final Player captain;
	public final Player wicketKeeper;

    public LineUp(List<Player> players, Team team, boolean isPlayingAtHome, Player captain, Player wicketKeeper) {
        this.players = players;
        this.team = team;
        this.isPlayingAtHome = isPlayingAtHome;
        this.captain = captain;
        this.wicketKeeper = wicketKeeper;
    }

    public static LineUp random(Team team) {
        Map<PlayingRole, List<Player>> playersByRole = team.getSquad().stream().collect(Collectors.groupingBy(Player::playingRole));
        List<Player> battingOrder = new ArrayList<>();
        allocatePlayers(team, playersByRole, battingOrder, PlayingRole.BATSMAN, 4);
        allocatePlayers(team, playersByRole, battingOrder, PlayingRole.ALL_ROUNDER, 6);
        allocatePlayers(team, playersByRole, battingOrder, PlayingRole.WICKET_KEEPER, 7);
        allocatePlayers(team, playersByRole, battingOrder, PlayingRole.BOWLER, 11);
        return new LineUpBuilder().withBattingOrder(battingOrder).withTeam(team).withPlayingAtHome(false).withCaptain(battingOrder.get(0)).withWicketKeeper(battingOrder.get(7)).build();
    }

    private static void allocatePlayers(Team team, Map<PlayingRole, List<Player>> playersByRole, List<Player> battingOrder, PlayingRole role, int stopWhenSize) {
        if (!playersByRole.containsKey(role)) {
            throw new RuntimeException(team + " has no " + role);
        }
        Distribution<Player> dist = Distribution.even(playersByRole.get(role));
        while (battingOrder.size() < stopWhenSize) {
            Player random = dist.random();
            if (!battingOrder.contains(random)) {
                battingOrder.add(random);
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return team.getName();
    }

}


