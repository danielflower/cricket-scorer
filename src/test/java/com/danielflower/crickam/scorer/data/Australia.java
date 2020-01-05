package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.*;

import java.util.UUID;

public class Australia {

    public static final Player SEAN_ABBOT = Player.player("Sean Abbot").build();
    public static final Player JASON_BEHRENDORFF = Player.player("Jason Behrendorff").build();
    public static final Player PAT_CUMMINS = Player.player("Pat Cummins").build();
    public static final Player MARCUS_HARRIS = Player.player("Marcus Harris").build();
    public static final Player USMAN_KHAWAJA = Player.player("Usman Khawaja").build();
    public static final Player BEN_MC_DERMOTT = Player.player("Ben McDermott").build();
    public static final Player TIM_PAINE = Player.player("Tim Paine").build();
    public static final Player PETER_SIDDLE = Player.player("Peter Siddle").build();
    public static final Player MITCHELL_STARC = Player.player("Mitchell Starc").build();
    public static final Player MATTHEW_WADE = Player.player("Matthew Wade").build();
    public static final Player ASHTON_AGAR = Player.player("Ashton Agar").build();
    public static final Player JOE_BURNS = Player.player("Joe Burns").build();
    public static final Player AARON_FINCH = Player.player("Aaron Finch").build();
    public static final Player JOSH_HAZLEWOOD = Player.player("Josh Hazlewood").build();
    public static final Player MARNUS_LABUSCHAGNE = Player.player("Marnus Labuschagne").build();
    public static final Player MITCHELL_MARSH = Player.player("Mitchell Marsh").build();
    public static final Player JAMES_PATTINSON = Player.player("James Pattinson").build();
    public static final Player STEVEN_SMITH = Player.player("Steven Smith").build();
    public static final Player MARCUS_STOINIS = Player.player("Marcus Stoinis").build();
    public static final Player DAVID_WARNER = Player.player("David Warner").build();
    public static final Player CAMERON_BANCROFT = Player.player("Cameron Bancroft").build();
    public static final Player ALEX_CAREY = Player.player("Alex Carey").build();
    public static final Player PETER_HANDSCOMB = Player.player("Peter Handscomb").build();
    public static final Player TRAVIS_HEAD = Player.player("Travis Head").build();
    public static final Player NATHON_LYON = Player.player("Nathon Lyon").build();
    public static final Player GLENN_MAXWELL = Player.player("Glenn Maxwell").build();
    public static final Player KANE_RICHARDSON = Player.player("Kane Richardson").build();
    public static final Player BILLY_STANLAKE = Player.player("Billy Stanlake").build();
    public static final Player ASHTON_TURNER = Player.player("Ashton Turner").build();
    public static final Player ADAM_ZAMPA = Player.player("Adam Zampa").build();

    public static Team.Builder team() {
        return Team.team()
            .withId(UUID.randomUUID().toString())
            .withTeamColour("#FF0000")
            .withShortName("AUS")
            .withName("Australia")
            .withLevel(TeamLevel.INTERNATIONAL);
    }

    public static LineUp.Builder t20LineUp() {
        return LineUp.lineUp()
            .withCaptain(AARON_FINCH)
            .withWicketKeeper(ALEX_CAREY)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, AARON_FINCH, STEVEN_SMITH, BEN_MC_DERMOTT, ASHTON_TURNER, ALEX_CAREY, ASHTON_AGAR,
                SEAN_ABBOT, MITCHELL_STARC, KANE_RICHARDSON, BILLY_STANLAKE
            ));
    }

    public static LineUp.Builder oneDayLineUp() {
        return LineUp.lineUp()
            .withCaptain(AARON_FINCH)
            .withWicketKeeper(ALEX_CAREY)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, AARON_FINCH, STEVEN_SMITH, PETER_HANDSCOMB, ALEX_CAREY, MARCUS_STOINIS, GLENN_MAXWELL,
                PAT_CUMMINS, MITCHELL_STARC, JASON_BEHRENDORFF, NATHON_LYON
            ));
    }

    public static LineUp.Builder testLineUp() {
        return LineUp.lineUp()
            .withCaptain(TIM_PAINE)
            .withWicketKeeper(TIM_PAINE)
            .withTeam(team().build())
            .withBattingOrder(ImmutableList.of(
                DAVID_WARNER, JOE_BURNS, MARNUS_LABUSCHAGNE, STEVEN_SMITH, MATTHEW_WADE, TRAVIS_HEAD, TIM_PAINE,
                PAT_CUMMINS, MITCHELL_STARC, NATHON_LYON, JOSH_HAZLEWOOD
            ));
    }

}
