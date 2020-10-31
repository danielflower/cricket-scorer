package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchEvents;
import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class MatchResultTest {
    private static final SimpleLineUp aus = Australia.oneDayLineUp().build();
    private static final Player ausBowler = aus.battingOrder().get(10);
    private static final SimpleLineUp nz = NewZealand.oneDayLineUp().build();
    private static final Player nzBowler = nz.battingOrder().get(10);

    @Test
    void limitedOversCanBeWonOrTied() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting()
            .withTeamLineUps(ImmutableList.of(aus, nz))
            .withInningsPerTeam(1)
            .withOversPerInnings(1)
            .withBallsPerInnings(3)
        );

        assertThat(MatchResult.fromMatch(control.match()).toString(), is("No result"));
        control = control.onEvent(inningsStarting().withBattingTeam(aus));

        assertThat(control.match().currentInnings().target(), is(nullValue()));

        control = control.onEvent(overStarting(nzBowler).withBallsInOver(3))
            .onEvent(ballCompleted("3"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted())
            .onEvent(inningsCompleted())
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting(ausBowler).withBallsInOver(2))
            .onEvent(ballCompleted("1"));

        assertThat(control.match().currentInnings().target(), is(Integer.valueOf(4)));

        MatchControl threeToWin = control;
        assertThat(MatchResult.fromMatch(threeToWin.match()).toString(), is("No result"));


        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("0"))
                .match()
            ).toString(),
            is("Australia won by 2 runs"));
        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("1"))
                .match()
            ).toString(),
            is("Australia won by 1 run"));

        // match tied with no balls remaining
        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("2"))
                .match()
            ).toString(),
            is("Match tied"));

        // match tied with 1 ball remaining
        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("2"))
                .match()
            ).toString(),
            is("No result"));

        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("3")).match()
            ).toString(),
            is("New Zealand won by 10 wickets"));
        assertThat(MatchResult.fromMatch(threeToWin
                .onEvent(ballCompleted("4")).match()
            ).toString(),
            is("New Zealand won by 10 wickets"));
    }

    @Test
    void firstClassMatchesCanBeWonOrTiedOrDrawn() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting(5, null)
            .withTeamLineUps(ImmutableList.of(aus, nz)));
        assertThat(MatchResult.fromMatch(control.match()).toString(), is("No result"));
        control = control.onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("4"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting().withBowler(ausBowler))
            .onEvent(ballCompleted("6"));

        assertThat(control.match().currentInnings().target(), is(nullValue()));

        MatchControl threeToWin = control.onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("4"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting().withBowler(ausBowler).withBallsInOver(12));

        assertThat(threeToWin.currentInnings().target(), is(Integer.valueOf(3)));
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
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting(5, null)
            .withTeamLineUps(ImmutableList.of(aus, nz)))
            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("0"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting().withBowler(ausBowler))
            .onEvent(ballCompleted("6"))
            .onEvent(inningsCompleted().withDeclared(true))
            .onEvent(inningsStarting().withBattingTeam(aus))
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
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));

        assertThat(MatchResult.fromMatch(control.match()).toString(), is("New Zealand won by an innings and 4 runs"));
    }

    @Test
    void knowsAboutBeingBeatenByAnInningsAfterFollowOn() {
        MatchControl control = MatchControl.newMatch(MatchEvents.matchStarting(5, null)
            .withTeamLineUps(ImmutableList.of(aus, nz)))

            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting().withBowler(ausBowler))
            .onEvent(ballCompleted("6"))
            .onEvent(inningsCompleted().withDeclared(true))

            .onEvent(inningsStarting().withBattingTeam(aus))
            .onEvent(overStarting().withBowler(nzBowler))
            .onEvent(ballCompleted("0"))
            .onEvent(inningsCompleted().withDeclared(true))

            .onEvent(inningsStarting().withFollowingOn(true).withBattingTeam(aus))
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
            .onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));

        assertThat(MatchResult.fromMatch(control.match()).toString(), is("New Zealand won by an innings and 4 runs"));
    }

}