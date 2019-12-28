package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.*;

import java.util.UUID;

import static com.danielflower.crickam.scorer.Handedness.LEFT_HANDED;
import static com.danielflower.crickam.scorer.PlayingRole.*;
import static com.danielflower.crickam.scorer.data.DataUtil.player;

public class Australia {

    public static final Player SEAN_ABBOT = player("Sean Abbot").withPlayingRole(BOWLER).build();
    public static final Player JASON_BEHRENDORFF = player("Jason Behrendorff").withPlayingRole(BOWLER).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player PAT_CUMMINS = player("Pat Cummins").withPlayingRole(BOWLER).build();
    public static final Player MARCUS_HARRIS = player("Marcus Harris").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player USMAN_KHAWAJA = player("Usman Khawaja").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player BEN_MC_DERMOTT = player("Ben McDermott").withPlayingRole(WICKET_KEEPER).build();
    public static final Player TIM_PAINE = player("Tim Paine").withPlayingRole(WICKET_KEEPER).build();
    public static final Player PETER_SIDDLE = player("Peter Siddle").withPlayingRole(BOWLER).build();
    public static final Player MITCHELL_STARC = player("Mitchell Starc").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player MATTHEW_WADE = player("Matthew Wade").withPlayingRole(WICKET_KEEPER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player ASHTON_AGAR = player("Ashton Agar").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player JOE_BURNS = player("Joe Burns").withPlayingRole(BATTER).build();
    public static final Player AARON_FINCH = player("Aaron Finch").withPlayingRole(BATTER).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player JOSH_HAZLEWOOD = player("Josh Hazlewood").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player MARNUS_LABUSCHAGNE = player("Marnus Labuschagne").withPlayingRole(BATTER).build();
    public static final Player MITCHELL_MARSH = player("Mitchell Marsh").withPlayingRole(ALL_ROUNDER).build();
    public static final Player JAMES_PATTINSON = player("James Pattinson").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player STEVEN_SMITH = player("Steven Smith").withPlayingRole(BATTER).build();
    public static final Player MARCUS_STOINIS = player("Marcus Stoinis").withPlayingRole(BOWLER).build();
    public static final Player DAVID_WARNER = player("David Warner").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player CAMERON_BANCROFT = player("Cameron Bancroft").withPlayingRole(WICKET_KEEPER).build();
    public static final Player ALEX_CAREY = player("Alex Carey").withPlayingRole(WICKET_KEEPER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player PETER_HANDSCOMB = player("Peter Handscomb").withPlayingRole(BATTER).build();
    public static final Player TRAVIS_HEAD = player("Travis Head").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player NATHON_LYON = player("Nathon Lyon").withPlayingRole(BOWLER).build();
    public static final Player GLENN_MAXWELL = player("Glenn Maxwell").withPlayingRole(ALL_ROUNDER).build();
    public static final Player KANE_RICHARDSON = player("Kane Richardson").withPlayingRole(BOWLER).build();
    public static final Player BILLY_STANLAKE = player("Billy Stanlake").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player ASHTON_TURNER = player("Ashton Turner").withPlayingRole(BATTER).build();
    public static final Player ADAM_ZAMPA = player("Adam Zampa").withPlayingRole(BOWLER).build();

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
