package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.*;

import java.util.UUID;

import static com.danielflower.crickam.scorer.Handedness.LeftHanded;
import static com.danielflower.crickam.scorer.PlayingRole.*;
import static com.danielflower.crickam.scorer.data.DataUtil.player;

public class NewZealand {

    public static final Player GEORGE_WORKER = player("George Worker").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LeftHanded).withBowlingHandedness(LeftHanded).build();
    public static final Player NEIL_WAGNER = player("Neil Wagner").withPlayingRole(BOWLER).withBattingHandedness(LeftHanded).withBowlingHandedness(LeftHanded).build();
    public static final Player TIM_SOUTHEE = player("Tim Southee").withPlayingRole(BOWLER).build();
    public static final Player TIM_SEIFERT = player("Tim Seifert").withPlayingRole(WICKET_KEEPER).build();
    public static final Player JEET_RAVAL = player("Jeet Raval").withPlayingRole(BATTER).withBattingHandedness(LeftHanded).build();
    public static final Player HENRY_NICHOLLS = player("Henry Nicholls").withPlayingRole(BATTER).withBattingHandedness(LeftHanded).build();
    public static final Player DARYL_MITCHELL = player("Daryl Mitchell").withPlayingRole(ALL_ROUNDER).build();
    public static final Player MATT_HENRY = player("Matt Henry").withPlayingRole(BOWLER).build();
    public static final Player COLIN_DE_GRANDHOMME = player("Colin de Grandhomme").withFamilyName("de Grandhomme").withPlayingRole(ALL_ROUNDER).build();
    public static final Player DOUG_BRACEWELL = player("Doug Bracewell").withPlayingRole(BOWLER).build();
    public static final Player KANE_WILLIAMSON = player("Kane Williamson").withPlayingRole(BATTER).build();
    public static final Player BLAIR_TICKNER = player("Blair Tickner").withPlayingRole(BOWLER).build();
    public static final Player WILLIAM_SOMERVILLE = player("William Somerville").withPlayingRole(BOWLER).build();
    public static final Player MITCH_SANTNER = player("Mitch Santner").withPlayingRole(ALL_ROUNDER).withBowlingHandedness(LeftHanded).build();
    public static final Player SETH_RANCE = player("Seth Rance").withPlayingRole(BOWLER).build();
    public static final Player JAMES_NEESHAM = player("James Neesham").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LeftHanded).build();
    public static final Player TOM_LATHAM = player("Tom Latham").withPlayingRole(WICKET_KEEPER).withBattingHandedness(LeftHanded).build();
    public static final Player MARTIN_GUPTILL = player("Martin Guptill").withPlayingRole(BATTER).build();
    public static final Player MARK_CHAPMAN = player("Mark Chapman").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LeftHanded).withBowlingHandedness(LeftHanded).build();
    public static final Player TRENT_BOULT = player("Trent Boult").withPlayingRole(BOWLER).withBowlingHandedness(LeftHanded).build();
    public static final Player BJ_WATLING = player("BJ Watling").withPlayingRole(WICKET_KEEPER).build();
    public static final Player ROSS_TAYLOR = player("Ross Taylor").withPlayingRole(BATTER).build();
    public static final Player ISH_SODHI = player("Ish Sodhi").withPlayingRole(BOWLER).build();
    public static final Player HAMISH_RUTHERFORD = player("Hamish Rutherford").withPlayingRole(BATTER).withBattingHandedness(LeftHanded).withBowlingHandedness(LeftHanded).build();
    public static final Player AJAZ_PATEL = player("Ajaz Patel").withPlayingRole(BOWLER).withBattingHandedness(LeftHanded).withBowlingHandedness(LeftHanded).build();
    public static final Player COLIN_MUNRO = player("Colin Munro").withPlayingRole(BATTER).withBattingHandedness(LeftHanded).build();
    public static final Player SCOTT_KUGGELEIJN = player("Scott Kuggeleijn").withPlayingRole(ALL_ROUNDER).build();
    public static final Player LOCKIE_FERGUSON = player("Lockie Ferguson").withPlayingRole(BOWLER).build();
    public static final Player TOM_BRUCE = player("Tom Bruce").withPlayingRole(BATTER).build();
    public static final Player TODD_ASTLE = player("Todd Astle").withPlayingRole(BOWLER).build();


    public static Team.Builder team() {
        return Team.team()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#000000")
            .withShortName("NZL")
            .withName("New Zealand")
            .withLevel(TeamLevel.International);
    }

    public static LineUp.Builder t20LineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TIM_SEIFERT)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, COLIN_MUNRO, KANE_WILLIAMSON, ROSS_TAYLOR, TIM_SEIFERT,
                COLIN_DE_GRANDHOMME, JAMES_NEESHAM, MITCH_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON));
    }

    public static LineUp.Builder oneDayLineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TOM_LATHAM)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR, TOM_LATHAM,
                JAMES_NEESHAM, COLIN_DE_GRANDHOMME, MITCH_SANTNER, MATT_HENRY, TRENT_BOULT, LOCKIE_FERGUSON));
    }

    public static LineUp.Builder testLineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(BJ_WATLING)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(JEET_RAVAL, TOM_LATHAM, KANE_WILLIAMSON, ROSS_TAYLOR, HENRY_NICHOLLS,
                BJ_WATLING, COLIN_DE_GRANDHOMME, MITCH_SANTNER, TIM_SOUTHEE, NEIL_WAGNER, LOCKIE_FERGUSON));
    }

}
