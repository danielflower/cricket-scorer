package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.events.BallCompletedEvent.ballCompleted;
import static com.danielflower.crickam.scorer.events.InningsCompletedEvent.inningsCompleted;
import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MatchResultTest {
    private static final LineUp aus = Australia.oneDayLineUp().build();
    private static final Player ausBowler = aus.battingOrder().get(10);
    private static final LineUp nz = NewZealand.oneDayLineUp().build();
    private static final Player nzBowler = nz.battingOrder().get(10);
    private Match match;

    @Test
    void limitedOversMatchesAreWonIfSecondInningsScoreIsHigherThanFirst() {
        match = MatchControl.newMatch(MatchStartingEvent.matchStarting()
            .withMatchID("1")
            .withTeams(ImmutableList.of(aus, nz))
            .withMatchType(MatchType.ODI)
            .withNumberOfInningsPerTeam(1)
            .withOversPerInnings(1)
            .withBallsPerInnings(3)
            .build()).current();
        assertThat(MatchResult.fromMatch(match).toString(), is("No result"));
        match = match.onEvent(inningsStarting().withBattingTeam(aus));

        assertThat(match.currentInnings().get().target(), is(OptionalInt.empty()));

        match = match.onEvent(overStarting().withBowler(nzBowler).withBallsInOver(3))
            .onEvent(ballCompleted("3"))
            .onEvent(ballCompleted("0"))
            .onEvent(ballCompleted("0"))
            .onEvent(overCompleted())
            .onEvent(inningsCompleted())
            .onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(overStarting().withBallsInOver(2).withBowler(ausBowler))
            .onEvent(ballCompleted("1"));

        assertThat(match.currentInnings().get().target(), is(OptionalInt.of(4)));

        Match twoToWin = match;
        assertThat(MatchResult.fromMatch(twoToWin).toString(), is("No result"));



        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("0"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
            ).toString(),
            is("Australia won by 2 runs"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("1"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
            ).toString(),
            is("Australia won by 1 run"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("0"))
                .onEvent(ballCompleted("2"))
                .onEvent(overCompleted())
                .onEvent(inningsCompleted())
            ).toString(),
            is("Match tied"));

        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("3"))
            ).toString(),
            is("New Zealand won by 10 wickets"));
        assertThat(MatchResult.fromMatch(twoToWin
                .onEvent(ballCompleted("4"))
            ).toString(),
            is("New Zealand won by 10 wickets"));
    }

}