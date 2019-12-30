package e2e;

import com.danielflower.crickam.scorer.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import scaffolding.ScorecardLoader;

import java.util.TimeZone;

import static com.danielflower.crickam.scorer.Venue.venue;
import static com.danielflower.crickam.scorer.data.England.*;
import static com.danielflower.crickam.scorer.data.SouthAfrica.*;
import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InProgressTest {

    private static final TimeZone JOHANNESBURG = TimeZone.getTimeZone("Africa/Johannesburg");
    private MatchControl control;

    @Test
    public void canScoreTestMatch() {

        // A recreation of https://www.espncricinfo.com/series/19286/scorecard/1185304/south-africa-vs-england-1st-test-england-in-sa-2019-20

        LineUp sa = LineUp.lineUp()
            .withTeam(Team.team()
                .withShortName("SA")
                .withName("South Africa")
                .withLevel(TeamLevel.INTERNATIONAL)
                .build())
            .withBattingOrder(ImmutableList.of(
                ELGAR, MARKRAM, HAMZA, DU_PLESSIS, VAN_DER_DUSSEN, DE_KOCK, PRETORIUS, PHILANDER, MAHARAJ, RABADA, NORTJE
            ))
            .withCaptain(DU_PLESSIS)
            .withWicketKeeper(DE_KOCK)
            .build();

        LineUp eng = LineUp.lineUp()
            .withTeam(Team.team()
                .withShortName("ENG")
                .withName("England")
                .withLevel(TeamLevel.INTERNATIONAL)
                .build())
            .withBattingOrder(ImmutableList.of(
                BURNS, SIBLEY, DENLY, ROOT, STOKES, BAIRSTOW, BUTTLER, SAM_CURRAN, ARCHER, BROAD, ANDERSON
            ))
            .withCaptain(ROOT)
            .withWicketKeeper(BUTTLER)
            .build();

        control = MatchControl.newMatch(
            matchStarting(MatchType.TEST)
                .withTeams(ImmutableList.of(sa, eng))
                .withTime(Crictils.localTime(JOHANNESBURG, 2019, 12, 26, 10, 0))
                .withScheduledStartTime(Crictils.localTime(JOHANNESBURG, 2019, 12, 26, 10, 0))
                .withVenue(venue()
                    .withName("Centurion")
                    .withCity("Johannesburg")
                    .withTerritory("South Africa")
                    .withTimeZone(JOHANNESBURG)
                    .build()
                )
                .build()
        );

        control = control.onEvent(inningsStarting()
            .withBattingTeam(sa)
            .withTime(control.localTime(14, 0, 0))
        );


        // Beginning of innings 1

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Buttler")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("3"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Bairstow")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1w"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("3nb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Stokes")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Root")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Root")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Root")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Root")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("6"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Root")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Root")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("3"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1nb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Root")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1w"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Denly")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Denly")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Buttler")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Stokes")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Archer")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Denly")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Anderson")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(ballCompleted("1w"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("b")));
        control = control.onEvent(batterInningsStarting().withTime(Crictils.localTime(JOHANNESBURG, 2019, 12, 27, 10, 0)));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Curran")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Broad")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Buttler")));
        control = control.onEvent(inningsCompleted().withTime(control.localTime(10, 10, 0)));

        // End of innings 1

        // Start of innings 2
        control = control.onEvent(inningsStarting().withBattingTeam(eng).withTime(control.localTime(15, 55, 0)));

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1nb"));
        control = control.onEvent(ballCompleted("5nb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4lb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1lb"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Maharaj")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Maharaj")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4b"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Maharaj")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("6"));
        control = control.onEvent(ballCompleted("6"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("5w"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Maharaj")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("b")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Pretorius")));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Nortje")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("4"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Zubayr Hamza")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("de Kock")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Rabada")));
        control = control.onEvent(ballCompleted("2"));
        control = control.onEvent(ballCompleted("1"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("c")).withFielder(findFielder("Elgar")));
        control = control.onEvent(batterInningsStarting());
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(overCompleted());

        control = control.onEvent(overStarting(findFielder("Philander")));
        control = control.onEvent(ballCompleted("0"));
        control = control.onEvent(ballCompleted("W").withDismissal(DismissalType.fromAbbreviation("b")));

        control = control.onEvent(inningsCompleted().withTime(control.localTime(15, 45, 0)));
        // End of innings 2

//        control = control.onEvent(matchCompleted().withTime(control.localTime(18, 38, 0)));


        Match matchAtEnd = control.match();

        String actual = AsciiScorecardRenderer.toString(matchAtEnd);
        System.out.println(actual);
        assertEquals(ScorecardLoader.load("sa-vs-eng-test-in-progress.txt"), actual);
    }

    @NotNull
    private Player findFielder(String name) {
        return T20.findFielder(control, name);
    }

}
