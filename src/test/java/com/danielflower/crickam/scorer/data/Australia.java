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

public class Australia {

    public static final Player SEAN_ABBOT = player("Sean Abbot").setPlayingRole(BOWLER).setBowlingStyle(fastMedium()).build();
    public static final Player JASON_BEHRENDORFF = player("Jason Behrendorff").setPlayingRole(BOWLER).setBowlingStyle(fastMedium().setHandedness(LeftHanded)).build();
    public static final Player PAT_CUMMINS = player("Pat Cummins").setPlayingRole(BOWLER).setBowlingStyle(fast()).build();
    public static final Player MARCUS_HARRIS = player("Marcus Harris").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(medium().setHandedness(LeftHanded)).build();
    public static final Player USMAN_KHAWAJA = player("Usman Khawaja").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(medium()).build();
    public static final Player BEN_MC_DERMOTT = player("Ben McDermott").setPlayingRole(WICKET_KEEPER).setBowlingStyle(medium()).build();
    public static final Player TIM_PAINE = player("Tim Paine").setPlayingRole(WICKET_KEEPER).setBowlingStyle(medium()).build();
    public static final Player PETER_SIDDLE = player("Peter Siddle").setPlayingRole(BOWLER).setBowlingStyle(fastMedium()).build();
    public static final Player MITCHELL_STARC = player("Mitchell Starc").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(fast()).build();
    public static final Player MATTHEW_WADE = player("Matthew Wade").setPlayingRole(WICKET_KEEPER).setBattingHandedness(LeftHanded).setBowlingStyle(medium()).build();
    public static final Player ASHTON_AGAR = player("Ashton Agar").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player JOE_BURNS = player("Joe Burns").setPlayingRole(BATSMAN).setBowlingStyle(medium()).build();
    public static final Player AARON_FINCH = player("Aaron Finch").setPlayingRole(BATSMAN).setBowlingStyle(leftArmOrthodox()).build();
    public static final Player JOSH_HAZLEWOOD = player("Josh Hazlewood").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(fastMedium()).build();
    public static final Player MARNUS_LABUSCHAGNE = player("Marnus Labuschagne").setPlayingRole(BATSMAN).setBowlingStyle(legSpinner()).build();
    public static final Player MITCHELL_MARSH = player("Mitchell Marsh").setPlayingRole(ALL_ROUNDER).setBowlingStyle(medium()).build();
    public static final Player JAMES_PATTINSON = player("James Pattinson").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(fastMedium()).build();
    public static final Player STEVEN_SMITH = player("Steven Smith").setPlayingRole(BATSMAN).setBowlingStyle(legSpinner()).build();
    public static final Player MARCUS_STOINIS = player("Marcus Stoinis").setPlayingRole(BOWLER).setBowlingStyle(medium()).build();
    public static final Player DAVID_WARNER = player("David Warner").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(legSpinner()).build();
    public static final Player CAMERON_BANCROFT = player("Cameron Bancroft").setPlayingRole(WICKET_KEEPER).setBowlingStyle(medium()).build();
    public static final Player ALEX_CAREY = player("Alex Carey").setPlayingRole(WICKET_KEEPER).setBattingHandedness(LeftHanded).setBowlingStyle(medium()).build();
    public static final Player PETER_HANDSCOMB = player("Peter Handscomb").setPlayingRole(BATSMAN).setBowlingStyle(medium()).build();
    public static final Player TRAVIS_HEAD = player("Travis Head").setPlayingRole(BATSMAN).setBattingHandedness(LeftHanded).setBowlingStyle(offSpinner()).build();
    public static final Player NATHON_LYON = player("Nathon Lyon").setPlayingRole(BOWLER).setBowlingStyle(offSpinner()).build();
    public static final Player GLENN_MAXWELL = player("Glenn Maxwell").setPlayingRole(ALL_ROUNDER).setBowlingStyle(offSpinner()).build();
    public static final Player KANE_RICHARDSON = player("Kane Richardson").setPlayingRole(BOWLER).setBowlingStyle(fastMedium()).build();
    public static final Player BILLY_STANLAKE = player("Billy Stanlake").setPlayingRole(BOWLER).setBattingHandedness(LeftHanded).setBowlingStyle(fast()).build();
    public static final Player ASHTON_TURNER = player("Ashton Turner").setPlayingRole(BATSMAN).setBowlingStyle(offSpinner()).build();
    public static final Player ADAM_ZAMPA = player("Adam Zampa").setPlayingRole(BOWLER).setBowlingStyle(legSpinner()).build();

    public static TeamBuilder team() {
        return new TeamBuilder()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#FF0000")
            .withShortName("AUS")
            .withName("New Australia")
            .withLevel(TeamLevel.International)
            .withSquad(ImmutableList.of(
                SEAN_ABBOT, JASON_BEHRENDORFF, PAT_CUMMINS, MARCUS_HARRIS, USMAN_KHAWAJA, BEN_MC_DERMOTT, TIM_PAINE,
                PETER_SIDDLE, MITCHELL_STARC, MATTHEW_WADE, ASHTON_AGAR, JOE_BURNS, AARON_FINCH, JOSH_HAZLEWOOD,
                MARNUS_LABUSCHAGNE, MITCHELL_MARSH, JAMES_PATTINSON, STEVEN_SMITH, MARCUS_STOINIS, DAVID_WARNER,
                CAMERON_BANCROFT, ALEX_CAREY, PETER_HANDSCOMB, TRAVIS_HEAD, NATHON_LYON, GLENN_MAXWELL, KANE_RICHARDSON,
                BILLY_STANLAKE, ASHTON_TURNER, ADAM_ZAMPA
            ));
    }

    public static LineUpBuilder t20LineUp() {
        return new LineUpBuilder()
            .withCaptain(AARON_FINCH)
            .withWicketKeeper(ALEX_CAREY)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, AARON_FINCH, STEVEN_SMITH, BEN_MC_DERMOTT, ASHTON_TURNER, ALEX_CAREY, ASHTON_AGAR,
                SEAN_ABBOT, MITCHELL_STARC, KANE_RICHARDSON, BILLY_STANLAKE
            ));
    }

    public static LineUpBuilder oneDayLineUp() {
        return new LineUpBuilder()
            .withCaptain(AARON_FINCH)
            .withWicketKeeper(ALEX_CAREY)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, AARON_FINCH, STEVEN_SMITH, PETER_HANDSCOMB, ALEX_CAREY, MARCUS_STOINIS, GLENN_MAXWELL,
                PAT_CUMMINS, MITCHELL_STARC, JASON_BEHRENDORFF, NATHON_LYON
            ));
    }

    public static LineUpBuilder testLineUp() {
        return new LineUpBuilder()
            .withCaptain(TIM_PAINE)
            .withWicketKeeper(TIM_PAINE)
            .withPlayingAtHome(true)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, JOE_BURNS, MARNUS_LABUSCHAGNE, STEVEN_SMITH, MATTHEW_WADE, TRAVIS_HEAD, TIM_PAINE,
                PAT_CUMMINS, MITCHELL_STARC, NATHON_LYON, JOSH_HAZLEWOOD
            ));
    }

}
