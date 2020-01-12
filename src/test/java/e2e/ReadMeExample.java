package e2e;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.MatchEvents;

import static com.danielflower.crickam.scorer.data.England.*;
import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.MatchEvents.*;

public class ReadMeExample {

    public static void main(String[] args) {

        LineUp nz = LineUp.lineUp()
            .withTeam(Team.team()
                .withShortName("NZL")
                .withName("New Zealand")
                .build())
            .withBattingOrder(ImmutableList.of(COLIN_MUNRO, MARTIN_GUPTILL, TIM_SEIFERT, COLIN_DE_GRANDHOMME, ROSS_TAYLOR,
                JAMES_NEESHAM, MITCHELL_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON, BLAIR_TICKNER))
            .withCaptain(TIM_SOUTHEE)
            .withWicketKeeper(TIM_SEIFERT)
            .build();

        LineUp eng = LineUp.lineUp()
            .withTeam(Team.team()
                .withShortName("ENG")
                .withName("England")
                .build())
            .withBattingOrder(ImmutableList.of(
                BURNS, SIBLEY, DENLY, ROOT, STOKES, BAIRSTOW, BUTTLER, SAM_CURRAN, ARCHER, BROAD, ANDERSON
            ))
            .withCaptain(ROOT)
            .withWicketKeeper(BUTTLER)
            .build();

        MatchControl control = MatchControl.newMatch(
            MatchEvents.matchStarting(MatchType.ODI).withTeamLineUps(ImmutableList.of(nz, eng))
        );

        control = control.onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting(eng.battingOrder().get(10)))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("4"))
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("1"))
            .onEvent(ballCompleted("2"))
            .onEvent(overCompleted());

        System.out.println(AsciiScorecardRenderer.toString(control));


        MatchControl matchAfterFirstSingle = control.asAt(
            control.eventStream(BallCompletedEvent.class)
                .filter(b -> b.score().batterRuns() == 1)
                .findFirst()
                .get());
        Score scoreAfterFirstSingle = matchAfterFirstSingle.currentInnings().score();
        System.out.println("The team score after the first single was " +
            scoreAfterFirstSingle.teamRuns() + " runs for " + scoreAfterFirstSingle.wickets());

        control = control
            .onEvent(overStarting().withBowler(eng.battingOrder().get(9)))
            .onEvent(ballCompleted()
                .withRunsScored(Score.score().withWickets(1).withValidDeliveries(1).withBatterRuns(1).withSingles(1).build())
                .withDismissal(DismissalType.RUN_OUT)
                .withDismissedBatter(MARTIN_GUPTILL)
                .withFielder(ROOT)
                .withPlayersCrossed(true))
            .onEvent(batterInningsStarting());

        System.out.println();
        System.out.println("Scorecard after running out the batter");
        System.out.println(AsciiScorecardRenderer.toString(control));

    }
}
