package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchEvents;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MatchResultTest {
    private static final LineUp aus = Australia.oneDayLineUp().build();
    private static final Player ausBowler = aus.battingOrder().get(10);
    private static final LineUp nz = NewZealand.oneDayLineUp().build();
    private static final Player nzBowler = nz.battingOrder().get(10);

    @Test
    void limitedOversCanBeWonOrTied() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting()
            .withMatchID("1")
            .withTeams(ImmutableList.of(aus, nz))
            .withMatchType(MatchType.ODI)
            .withNumberOfInningsPerTeam(1)
            .withOversPerInnings(1)
            .withBallsPerInnings(3).build()
        );

        assertThat(MatchResult.fromMatch(control.match()).toString(), is("No result"));
        control = control.onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting());

        assertThat(control.match().currentInnings().get().target(), is(OptionalInt.empty()));

        control = control.onEvent(overStarting(nzBowler).withBallsInOver(3))
            .onEvent(ballCompleted("3"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted())
            .onEvent(inningsCompleted())
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting(ausBowler).withBallsInOver(2))
            .onEvent(ballCompleted("1"));

        assertThat(control.match().currentInnings().get().target(), is(OptionalInt.of(4)));

        MatchControl twoToWin = control;
        assertThat(MatchResult.fromMatch(twoToWin.match()).toString(), is("No result"));


        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("0"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
                .match()
            ).toString(),
            is("Australia won by 2 runs"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("1"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
                .match()
            ).toString(),
            is("Australia won by 1 run"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("2"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
                .match()
            ).toString(),
            is("Match tied"));

        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("3")).match()
            ).toString(),
            is("New Zealand won by 10 wickets"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("4")).match()
            ).toString(),
            is("New Zealand won by 10 wickets"));
    }

    @Test
    void firstClassMatchesCanBeWonOrTiedOrDrawn() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting(MatchType.TEST)
            .withTeams(ImmutableList.of(aus, nz))
            .build());
        assertThat(MatchResult.fromMatch(control.match()).toString(), is("No result"));
        control = control.onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("4"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(ausBowler))
            .onEvent(ballCompleted("6"));

        assertThat(control.match().currentInnings().get().target(), is(OptionalInt.empty()));

        MatchControl threeToWin = control.onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("4"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(ausBowler).withBallsInOver(12));

        assertThat(threeToWin.currentInnings().target(), is(OptionalInt.of(3)));
        assertThat(MatchResult.fromMatch(threeToWin.match()).toString(), is("No result"));

        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(inningsCompleted())
                .match()
            ).toString(),
            is("Australia won by 2 runs"));

        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("2"))
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(inningsCompleted())
                .match()
            ).toString(),
            is("Match tied"));

        assertThat(MatchResult.fromMatch(threeToWin.onEvent(ballCompleted("3")).match()).toString(),
            is("New Zealand won by 10 wickets"));
        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
                .onEvent(batterInningsStarting())
                .onEvent(ballCompleted("4"))
                .match()
            ).toString(),
            is("New Zealand won by 9 wickets"));
    }

    @Test
    void knowsAboutBeingBeatenByAnInnings() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting(MatchType.TEST)
            .withTeams(ImmutableList.of(aus, nz))
            .build())
            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("0"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(ausBowler))
            .onEvent(ballCompleted("6"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
            .onEvent(overStarting().withBowler(nzBowler).withBallsInOver(20))
            .onEvent(ballCompleted("2"))
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED))
            .onEvent(inningsCompleted());

        assertThat(MatchResult.fromMatch(control.match()).toString(), is("New Zealand won by an innings and 4 runs"));
    }

}