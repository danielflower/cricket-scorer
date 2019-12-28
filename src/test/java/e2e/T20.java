package e2e;

import com.danielflower.crickam.scorer.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import scaffolding.ScorecardLoader;

import java.util.Optional;
import java.util.TimeZone;

import static com.danielflower.crickam.scorer.Venue.venue;
import static com.danielflower.crickam.scorer.data.England.*;
import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.BallCompletedEvent.ballCompleted;
import static com.danielflower.crickam.scorer.events.BatterInningsStartingEvent.batterInningsStarting;
import static com.danielflower.crickam.scorer.events.InningsCompletedEvent.inningsCompleted;
import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.MatchCompletedEvent.matchCompleted;
import static com.danielflower.crickam.scorer.events.MatchStartingEvent.matchStarting;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;
import static com.danielflower.crickam.scorer.events.OverStartingEvent.overStarting;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class T20 {

    public static final TimeZone NZ_TIME = TimeZone.getTimeZone("Pacific/Auckland");
    private MatchControl control;

    @Test
    public void canScoreT20Games() {

        // A recreation of https://www.espncricinfo.com/series/19297/scorecard/1187667/new-zealand-vs-england-3rd-t20i-england-in-new-zealand-2019-20

        LineUp nz = LineUp.lineUp()
            .withTeam(Team.team()
                .withShortName("NZL")
                .withName("New Zealand")
                .withLevel(TeamLevel.INTERNATIONAL)
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
                .withLevel(TeamLevel.INTERNATIONAL)
                .build())
            .withBattingOrder(ImmutableList.of(
                BANTON, MALAN, VINCE, MORGAN, BILLINGS, SAM_CURRAN, GREGORY, TOM_CURRAN, MAHMOOD, BROWN, PARKINSON
            ))
            .withCaptain(MORGAN)
            .withWicketKeeper(BILLINGS)
            .build();

        control = MatchControl.newMatch(
            matchStarting(MatchType.T20I)
                .withTeams(ImmutableList.of(nz, eng))
                .withTime(Crictils.localTime(NZ_TIME, 2019, 11, 5, 13, 0))
                .withScheduledStartTime(Crictils.localTime(NZ_TIME, 2019, 11, 5, 14, 0))
                .withVenue(venue()
                    .withName("Saxton Oval")
                    .withCity("Nelson")
                    .withTerritory("New Zealand")
                    .withTimeZone(NZ_TIME)
                    .build()
                )
                .build()
        );

        control.onEvent(inningsStarting()
            .withBattingTeam(nz)
            .withTime(control.localTime(14, 0, 0))
        );

        control.onEvent(overStarting(findFielder("SM Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("TK Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Mahmood")));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Brown")));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(TOM_CURRAN));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("TK Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(MAHMOOD));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Brown")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Mahmood")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Parkinson")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("SM Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Parkinson")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("SM Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Gregory")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Brown")));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Gregory")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("5nb"));
        control.onEvent(ballCompleted("1nb"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("TK Curran")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(BANTON));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Mahmood")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Brown")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Mahmood")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.LEG_BEFORE_WICKET));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("SM Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("TK Curran")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.RUN_OUT).withDismissedBatter(MITCHELL_SANTNER).withFielder(BILLINGS));
        control.onEvent(overCompleted());


        control.onEvent(inningsCompleted().withTime(control.localTime(15, 38, 0)));
        control.onEvent(inningsStarting()
            .withBattingTeam(eng)
            .withTime(control.localTime(14, 0, 0))
        );

        control.onEvent(overStarting(findFielder("Southee")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Ferguson")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Tickner")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Ferguson")));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Tickner")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Southee")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Sodhi")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Santner")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Neesham")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Santner")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Sodhi")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(MARTIN_GUPTILL).withPlayersCrossed(true));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Santner")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Tickner")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Ferguson")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Santner")));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(COLIN_MUNRO));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Southee")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.RUN_OUT).withFielder(COLIN_MUNRO));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Tickner")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(TIM_SOUTHEE));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Ferguson")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.BOWLED));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.CAUGHT).withFielder(COLIN_MUNRO).withPlayersCrossed(true));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Sodhi")));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting(findFielder("Southee")));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(inningsCompleted().withTime(control.localTime(18, 38, 0)));
        control.onEvent(matchCompleted().withTime(control.localTime(18, 38, 0)));

        Match matchAtEnd = control.current();

        String actual = AsciiScorecardRenderer.toString(matchAtEnd);
        assertEquals(ScorecardLoader.load("nz-vs-eng-t20i-complete.txt"), actual);
    }

    @NotNull
    private Player findFielder(String name) {
        return findFielder(control, name);
    }

    @NotNull
    static Player findFielder(MatchControl control, String name) {
        LineUp team = control.current().currentInnings().orElseThrow().bowlingTeam();
        Optional<Player> bowler = team.findPlayer(name);
        if (bowler.isEmpty()) {
            throw new RuntimeException("Not sure who " + name + " is");
        }
        return bowler.get();
    }


}
