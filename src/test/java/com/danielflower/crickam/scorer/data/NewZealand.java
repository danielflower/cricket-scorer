package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.LineUpBuilder;
import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.TeamBuilder;
import com.danielflower.crickam.scorer.TeamLevel;
import com.danielflower.crickam.utils.ImmutableList;

import java.util.UUID;

import static com.danielflower.crickam.scorer.BowlingStyleBuilder.*;
import static com.danielflower.crickam.scorer.Handedness.LeftHanded;
import static com.danielflower.crickam.scorer.PlayingRole.*;
import static com.danielflower.crickam.scorer.data.DataUtil.player;

public class NewZealand {

    public static final Player GEORGE_WORKER = player("George Worker").setPlayingRole(ALL_ROUNDER).setBattingHandedness(LeftHanded).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player NEIL_WAGNER = player("Neil Wagner").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(mediumFast()).build();
    public static final Player TIM_SOUTHEE = player("Tim Southee").setPlayingRole(BOWLER).setBowlingStyle(mediumFast()).build();
    public static final Player TIM_SEIFERT = player("Tim Seifert").setPlayingRole(WICKET_KEEPER).setBowlingStyle(medium()).build();
    public static final Player JEET_RAVAL = player("Jeet Raval").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(legSpinner()).build();
    public static final Player HENRY_NICHOLLS = player("Henry Nicholls").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(offSpinner()).build();
    public static final Player DARYL_MITCHELL = player("Daryl Mitchell").setPlayingRole(ALL_ROUNDER).setBowlingStyle(medium()).build();
    public static final Player MATT_HENRY = player("Matt Henry").setPlayingRole(BOWLER).setBowlingStyle(fastMedium()).build();
    public static final Player COLIN_DE_GRANDHOMME = player("Colin de Grandhomme").setFamilyName("de Grandhomme").setPlayingRole(ALL_ROUNDER).setBowlingStyle(mediumFast()).build();
    public static final Player DOUG_BRACEWELL = player("Doug Bracewell").setPlayingRole(BOWLER).setBowlingStyle(fastMedium()).build();
    public static final Player KANE_WILLIAMSON = player("Kane Williamson").setPlayingRole(BATSMAN).setBowlingStyle(offSpinner()).build();
    public static final Player BLAIR_TICKNER = player("Blair Tickner").setPlayingRole(BOWLER).setBowlingStyle(mediumFast()).build();
    public static final Player WILLIAM_SOMERVILLE = player("William Somerville").setPlayingRole(BOWLER).setBowlingStyle(offSpinner()).build();
    public static final Player MITCH_SANTNER = player("Mitch Santner").setPlayingRole(ALL_ROUNDER).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player SETH_RANCE = player("Seth Rance").setPlayingRole(BOWLER).setBowlingStyle(medium()).build();
    public static final Player JAMES_NEESHAM = player("James Neesham").setPlayingRole(ALL_ROUNDER).setBattingHandedness(LeftHanded).setBowlingStyle(mediumFast()).build();
    public static final Player TOM_LATHAM = player("Tom Latham").setPlayingRole(WICKET_KEEPER).setBattingHandedness(LeftHanded).setBowlingStyle(medium()).build();
    public static final Player MARTIN_GUPTILL = player("Martin Guptill").setPlayingRole(BATSMAN).setBowlingStyle(offSpinner()).build();
    public static final Player MARK_CHAPMAN = player("Mark Chapman").setPlayingRole(ALL_ROUNDER).setBattingHandedness(LeftHanded).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player TRENT_BOULT = player("Trent Boult").setPlayingRole(BOWLER).setBowlingStyle(fastMedium().setHandedness(LeftHanded)).build();
    public static final Player BJ_WATLING = player("BJ Watling").setPlayingRole(WICKET_KEEPER).setBowlingStyle(medium()).build();
    public static final Player ROSS_TAYLOR = player("Ross Taylor").setPlayingRole(BATSMAN).setBowlingStyle(offSpinner()).build();
    public static final Player ISH_SODHI = player("Ish Sodhi").setPlayingRole(BOWLER).setBowlingStyle(legSpinner()).build();
    public static final Player HAMISH_RUTHERFORD = player("Hamish Rutherford").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player AJAZ_PATEL = player("Ajaz Patel").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player COLIN_MUNRO = player("Colin Munro").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(mediumFast()).build();
    public static final Player SCOTT_KUGGELEIJN = player("Scott Kuggeleijn").setPlayingRole(ALL_ROUNDER).setBowlingStyle(fastMedium()).build();
    public static final Player LOCKIE_FERGUSON = player("Lockie Ferguson").setPlayingRole(BOWLER).setBowlingStyle(fast()).build();
    public static final Player TOM_BRUCE = player("Tom Bruce").setPlayingRole(BATSMAN).setBowlingStyle(offSpinner()).build();
    public static final Player TODD_ASTLE = player("Todd Astle").setPlayingRole(BOWLER).setBowlingStyle(legSpinner()).build();


    public static TeamBuilder team() {
        return new TeamBuilder()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#000000")
            .withShortName("NZL")
            .withName("New Zealand")
            .withLevel(TeamLevel.International)
            .withSquad(ImmutableList.of(
                TODD_ASTLE, TOM_BRUCE, LOCKIE_FERGUSON, SCOTT_KUGGELEIJN, COLIN_MUNRO, AJAZ_PATEL, HAMISH_RUTHERFORD,
                ISH_SODHI, ROSS_TAYLOR, BJ_WATLING, TRENT_BOULT, MARK_CHAPMAN, MARTIN_GUPTILL, TOM_LATHAM,
                JAMES_NEESHAM, SETH_RANCE, MITCH_SANTNER, WILLIAM_SOMERVILLE, BLAIR_TICKNER, KANE_WILLIAMSON,
                DOUG_BRACEWELL, COLIN_DE_GRANDHOMME, MATT_HENRY, DARYL_MITCHELL, HENRY_NICHOLLS, JEET_RAVAL,
                TIM_SEIFERT, TIM_SOUTHEE, NEIL_WAGNER, GEORGE_WORKER
            ));
    }

    public static LineUpBuilder t20LineUp() {
        return new LineUpBuilder()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TIM_SEIFERT)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, COLIN_MUNRO, KANE_WILLIAMSON, ROSS_TAYLOR, TIM_SEIFERT,
                COLIN_DE_GRANDHOMME, JAMES_NEESHAM, MITCH_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON));
    }

    public static LineUpBuilder oneDayLineUp() {
        return new LineUpBuilder()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TOM_LATHAM)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR, TOM_LATHAM,
                JAMES_NEESHAM, COLIN_DE_GRANDHOMME, MITCH_SANTNER, MATT_HENRY, TRENT_BOULT, LOCKIE_FERGUSON));
    }

    public static LineUpBuilder testLineUp() {
        return new LineUpBuilder()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(BJ_WATLING)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(JEET_RAVAL, TOM_LATHAM, KANE_WILLIAMSON, ROSS_TAYLOR, HENRY_NICHOLLS,
                BJ_WATLING, COLIN_DE_GRANDHOMME, MITCH_SANTNER, TIM_SOUTHEE, NEIL_WAGNER, LOCKIE_FERGUSON));
    }

}
