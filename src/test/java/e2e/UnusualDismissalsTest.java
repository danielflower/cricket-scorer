package e2e;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UnusualDismissalsTest {

    private static final String WS = "[\\s]+";

    @Test
    public void unusualDimissalsCanBeRecorded() throws IOException {

        SimpleLineUp nz = NewZealand.oneDayLineUp()
            .withBattingOrder(ImmutableList.of(COLIN_MUNRO, MARTIN_GUPTILL, TIM_SEIFERT, COLIN_DE_GRANDHOMME, ROSS_TAYLOR,
                JAMES_NEESHAM, MITCHELL_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON, BLAIR_TICKNER))
            .withCaptain(TIM_SOUTHEE)
            .withWicketKeeper(TIM_SEIFERT)
            .build();

        SimpleLineUp eng = Australia.oneDayLineUp().build();
        Player bowler1 = eng.battingOrder().get(10);

        MatchControl control = MatchControl.newMatch(matchStarting(1, 50).withTeamLineUps(ImmutableList.of(nz, eng)).build());

        control = control.onEvent(inningsStarting().withBattingTeam(nz))
            .onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting())
        ;

        control = control.onEvent(overStarting(bowler1));

        // wickets from valid deliveries
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.HANDLED_THE_BALL))
            .onEvent(batterInningsCompleted())
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.HIT_THE_BALL_TWICE))
            .onEvent(batterInningsCompleted())
            .onEvent(batterInningsStarting())
            .onEvent(ballCompleted("W").withDismissal(DismissalType.OBSTRUCTING_THE_FIELD))
            .onEvent(batterInningsCompleted())
        ;

        // time out - batter didn't come out in time. This is the only case where a Dismissal object needs to
        // be manually created.
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(batterInningsCompleted(BattingState.DISMISSED).withDismissal(
            Dismissal.dismissal()
                .withType(DismissalType.TIMED_OUT)
                .withBatter(control.match().currentInnings().currentStriker().player())
                .build()))
        ;

        control = control.onEvent(batterInningsStarting());

        // the non-striker is the Guptill, who now retires
        control = control.onEvent(batterInningsCompleted(BattingState.RETIRED).withBatter(MARTIN_GUPTILL));


        assertThat(AsciiScorecardRenderer.toString(control), matchesRegex("Guptill" + WS + "retired" + WS + "0" + WS + "0" + WS));
        assertThat(AsciiScorecardRenderer.toString(control), matchesRegex("Neesham" + WS + "not out" + WS + "0" + WS + "0" + WS));

        // before the next batter comes in, Neesham retires
        control = control.onEvent(batterInningsCompleted(BattingState.RETIRED_OUT).withBatter(JAMES_NEESHAM));

        assertThat(control.match().currentInnings().currentStriker(), is(nullValue()));
        assertThat(control.match().currentInnings().currentNonStriker(), is(nullValue()));

        control = control.onEvent(batterInningsStarting())
            .onEvent(batterInningsStarting());

        // now the bowler mankades the non-striker
        control = control.onEvent(
            ballCompleted().withRunsScored(Score.score().withWickets(1).build())
                .withDismissal(DismissalType.RUN_OUT)
                .withFielder(bowler1)
                .withDismissedBatter(control.match().currentInnings().currentNonStriker().player()))
            .onEvent(batterInningsCompleted())
        ;


        AsciiScorecardRenderer.render(control, System.out);

        assertThat(AsciiScorecardRenderer.toString(control), allOf(
            containsString("(5 wkts; 0.3 overs)"),
            containsString("Yet to bat: I Sodhi, L Ferguson, B Tickner"),
            matchesRegex("Fall of wickets: 1-0 \\(Colin Munro, 0.1 ov\\)," + WS + "2-0 \\(Tim Seifert, 0.2 ov\\)," + WS
                + "3-0 \\(Colin de Grandhomme, 0.3 ov\\)," + WS + "4-0 \\(Ross Taylor, 0.3 ov\\)," + WS + "4-0\\* \\(Martin Guptill, retired not out\\),"
                + WS + "4-0\\* \\(Jimmy Neesham, retired out\\)," + WS + "5-0 \\(Tim Southee, 0.3 ov\\)"),
            matchesRegex("Lyon" + WS + "0.3" + WS + "0" + WS + "0" + WS + "0" + WS + "0.0" + WS + "3" + WS + "0" + WS + "0" + WS + "0" + WS + "0")
        ));

    }

    private static Matcher<String> matchesRegex(String pattern) {
        return matchesPattern(Pattern.compile(".*" + pattern + ".*", Pattern.DOTALL));
    }

}
