package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.Team;
import com.danielflower.crickam.scorer.TeamLevel;

import java.util.UUID;

import static com.danielflower.crickam.scorer.Handedness.LEFT_HANDED;
import static com.danielflower.crickam.scorer.PlayingRole.*;
import static com.danielflower.crickam.scorer.data.DataUtil.player;

public class England {
    public static final Player ALI = player("Moeen Ali").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player ANDERSON = player("James Anderson").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player ARCHER = player("Jofra Archer").withPlayingRole(BOWLER).build();
    public static final Player BAIRSTOW = player("Jonny Bairstow").withPlayingRole(WICKET_KEEPER).build();
    public static final Player BANTON = player("Tom Banton").withPlayingRole(BATTER).build();
    public static final Player BILLINGS = player("Sam Billings").withPlayingRole(WICKET_KEEPER).build();
    public static final Player BROAD = player("Stuart Broad").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player BROWN = player("Pat Brown").withPlayingRole(BOWLER).build();
    public static final Player BURNS = player("Rory Burns").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player BUTTLER = player("Jos Buttler").withPlayingRole(WICKET_KEEPER).build();
    public static final Player CRAWLEY = player("Zak Crawley").withPlayingRole(BATTER).build();
    public static final Player SAM_CURRAN = player("Sam Curran").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LEFT_HANDED).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player TOM_CURRAN = player("Tom Curran").withPlayingRole(ALL_ROUNDER).build();
    public static final Player DENLY = player("Joe Denly").withPlayingRole(BATTER).build();
    public static final Player GREGORY = player("Lewis Gregory").withPlayingRole(ALL_ROUNDER).build();
    public static final Player JORDAN = player("Chris Jordan").withPlayingRole(BOWLER).build();
    public static final Player LEACH = player("Jack Leach").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player MAHMOOD = player("Saqib Mahmood").withPlayingRole(BOWLER).build();
    public static final Player MALAN = player("Dawid Malan").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player MORGAN = player("Eoin Morgan").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player OVERTON = player("Craig Overton").withPlayingRole(BOWLER).build();
    public static final Player PARKINSON = player("Matt Parkinson").withPlayingRole(BOWLER).build();
    public static final Player POPE = player("Ollie Pope").withPlayingRole(BATTER).build();
    public static final Player RASHID = player("Adil Rashid").withPlayingRole(BOWLER).build();
    public static final Player ROOT = player("Joe Root").withPlayingRole(BATTER).build();
    public static final Player ROY = player("Jason Roy").withPlayingRole(BATTER).build();
    public static final Player SIBLEY = player("Dom Sibley").withPlayingRole(BATTER).build();
    public static final Player STOKES = player("Ben Stokes").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player VINCE = player("James Vince").withPlayingRole(BATTER).build();
    public static final Player WOAKES = player("Chris Woakes").withPlayingRole(ALL_ROUNDER).build();

    public static Team.Builder team() {
        return Team.team()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#FFFFFF")
            .withShortName("ENG")
            .withName("England")
            .withLevel(TeamLevel.INTERNATIONAL);
    }
}
