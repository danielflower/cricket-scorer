package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.*;

import java.util.UUID;

import static com.danielflower.crickam.scorer.Player.player;

public class NewZealand {

    public static final Player GEORGE_WORKER = player("George Worker").build();
    public static final Player NEIL_WAGNER = player("Neil Wagner").build();
    public static final Player TIM_SOUTHEE = player("Timothy Grant Southee").withGivenName("Tim").build();
    public static final Player TIM_SEIFERT = player("Tim Louis Seifert").build();
    public static final Player JEET_RAVAL = player("Jeet Raval").build();
    public static final Player HENRY_NICHOLLS = player("Henry Nicholls").build();
    public static final Player DARYL_MITCHELL = player("Daryl Mitchell").build();
    public static final Player MATT_HENRY = player("Matt Henry").build();
    public static final Player COLIN_DE_GRANDHOMME = player("Colin de Grandhomme").build();
    public static final Player DOUG_BRACEWELL = player("Doug Bracewell").build();
    public static final Player KANE_WILLIAMSON = player("Kane Williamson").build();
    public static final Player BLAIR_TICKNER = player("Blair Marshall Tickner").build();
    public static final Player WILLIAM_SOMERVILLE = player("William Somerville").build();
    public static final Player MITCHELL_SANTNER = player("Mitchell Josef Santner").build();
    public static final Player SETH_RANCE = player("Seth Rance").build();
    public static final Player JAMES_NEESHAM = player("James Douglas Sheahan Neesham").build();
    public static final Player TOM_LATHAM = player("Tom Latham").build();
    public static final Player MARTIN_GUPTILL = player("Martin James Guptill").build();
    public static final Player MARK_CHAPMAN = player("Mark Chapman").build();
    public static final Player TRENT_BOULT = player("Trent Boult").build();
    public static final Player BJ_WATLING = player("Bradley-John Watling").withGivenName("BJ").withInitials("BJ").build();
    public static final Player ROSS_TAYLOR = player("Luteru Ross Poutoa Lote Taylor").withGivenName("Ross").build();
    public static final Player ISH_SODHI = player("Inderbir Singh Sodhi").withGivenName("Ish").build();
    public static final Player HAMISH_RUTHERFORD = player("Hamish Rutherford").build();
    public static final Player AJAZ_PATEL = player("Ajaz Patel").build();
    public static final Player COLIN_MUNRO = player("Colin Munro").build();
    public static final Player SCOTT_KUGGELEIJN = player("Scott Kuggeleijn").build();
    public static final Player LOCKIE_FERGUSON = player("Lachlan Hammond Ferguson").withGivenName("Lockie").build();
    public static final Player TOM_BRUCE = player("Tom Bruce").build();
    public static final Player TODD_ASTLE = player("Todd Astle").build();


    public static Team.Builder team() {
        return Team.team()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#000000")
            .withShortName("NZL")
            .withName("New Zealand")
            .withLevel(TeamLevel.INTERNATIONAL);
    }

    public static LineUp.Builder t20LineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TIM_SEIFERT)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, COLIN_MUNRO, KANE_WILLIAMSON, ROSS_TAYLOR, TIM_SEIFERT,
                COLIN_DE_GRANDHOMME, JAMES_NEESHAM, MITCHELL_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON));
    }

    public static LineUp.Builder oneDayLineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(TOM_LATHAM)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(MARTIN_GUPTILL, HENRY_NICHOLLS, KANE_WILLIAMSON, ROSS_TAYLOR, TOM_LATHAM,
                JAMES_NEESHAM, COLIN_DE_GRANDHOMME, MITCHELL_SANTNER, MATT_HENRY, TRENT_BOULT, LOCKIE_FERGUSON));
    }

    public static LineUp.Builder testLineUp() {
        return LineUp.lineUp()
            .withCaptain(KANE_WILLIAMSON)
            .withWicketKeeper(BJ_WATLING)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(JEET_RAVAL, TOM_LATHAM, KANE_WILLIAMSON, ROSS_TAYLOR, HENRY_NICHOLLS,
                BJ_WATLING, COLIN_DE_GRANDHOMME, MITCHELL_SANTNER, TIM_SOUTHEE, NEIL_WAGNER, LOCKIE_FERGUSON));
    }

}
