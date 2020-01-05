package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.Team;

public class England {
    public static final Player ALI = Player.player("Moeen Ali").build();
    public static final Player ANDERSON = Player.player("James Anderson").build();
    public static final Player ARCHER = Player.player("Jofra Archer").build();
    public static final Player BAIRSTOW = Player.player("Jonny Bairstow").build();
    public static final Player BANTON = Player.player("Tom Banton").build();
    public static final Player BILLINGS = Player.player("Sam Billings").build();
    public static final Player BROAD = Player.player("Stuart Broad").build();
    public static final Player BROWN = Player.player("Pat Brown").build();
    public static final Player BURNS = Player.player("Rory Burns").build();
    public static final Player BUTTLER = Player.player("Jos Buttler").build();
    public static final Player CRAWLEY = Player.player("Zak Crawley").build();
    public static final Player SAM_CURRAN = Player.player("Sam Curran").build();
    public static final Player TOM_CURRAN = Player.player("Tom Curran").build();
    public static final Player DENLY = Player.player("Joe Denly").build();
    public static final Player GREGORY = Player.player("Lewis Gregory").build();
    public static final Player JORDAN = Player.player("Chris Jordan").build();
    public static final Player LEACH = Player.player("Jack Leach").build();
    public static final Player MAHMOOD = Player.player("Saqib Mahmood").build();
    public static final Player MALAN = Player.player("Dawid Malan").build();
    public static final Player MORGAN = Player.player("Eoin Morgan").build();
    public static final Player OVERTON = Player.player("Craig Overton").build();
    public static final Player PARKINSON = Player.player("Matt Parkinson").build();
    public static final Player POPE = Player.player("Ollie Pope").build();
    public static final Player RASHID = Player.player("Adil Rashid").build();
    public static final Player ROOT = Player.player("Joe Root").build();
    public static final Player ROY = Player.player("Jason Roy").build();
    public static final Player SIBLEY = Player.player("Dom Sibley").build();
    public static final Player STOKES = Player.player("Ben Stokes").build();
    public static final Player VINCE = Player.player("James Vince").build();
    public static final Player WOAKES = Player.player("Chris Woakes").build();

    public static Team.Builder team() {
        return Team.team()
            .withShortName("ENG")
            .withName("England");
    }
}
