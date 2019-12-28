package e2e;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static com.danielflower.crickam.scorer.AsciiScorecardRenderer.NEWLINE;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class T20 {

    public static final TimeZone NZ_TIME = TimeZone.getTimeZone("Pacific/Auckland");
    private MatchControl control;

    @Test
    public void canScoreT20Games() throws IOException {

        // A recreation of https://www.espncricinfo.com/series/19297/scorecard/1187667/new-zealand-vs-england-3rd-t20i-england-in-new-zealand-2019-20

        LineUp nz = LineUp.lineUp()
            .withTeam(Team.team()
                .withId("NZMEN")
                .withTeamColour("#000000")
                .withShortName("NZL")
                .withName("New Zealand")
                .withLevel(TeamLevel.International)
                .withSquad(ImmutableList.of(
                    TODD_ASTLE, TOM_BRUCE, LOCKIE_FERGUSON, SCOTT_KUGGELEIJN, COLIN_MUNRO, AJAZ_PATEL, HAMISH_RUTHERFORD,
                    ISH_SODHI, ROSS_TAYLOR, BJ_WATLING, TRENT_BOULT, MARK_CHAPMAN, MARTIN_GUPTILL, TOM_LATHAM,
                    JAMES_NEESHAM, SETH_RANCE, MITCH_SANTNER, WILLIAM_SOMERVILLE, BLAIR_TICKNER, KANE_WILLIAMSON,
                    DOUG_BRACEWELL, COLIN_DE_GRANDHOMME, MATT_HENRY, DARYL_MITCHELL, HENRY_NICHOLLS, JEET_RAVAL,
                    TIM_SEIFERT, TIM_SOUTHEE, NEIL_WAGNER, GEORGE_WORKER
                )).build())
            .withBattingOrder(ImmutableList.of(COLIN_MUNRO, MARTIN_GUPTILL, TIM_SEIFERT, COLIN_DE_GRANDHOMME, ROSS_TAYLOR,
                JAMES_NEESHAM, MITCH_SANTNER, TIM_SOUTHEE, ISH_SODHI, LOCKIE_FERGUSON, BLAIR_TICKNER))
            .withCaptain(TIM_SOUTHEE)
            .withWicketKeeper(TIM_SEIFERT)
            .build();

        LineUp eng = LineUp.lineUp()
            .withTeam(Team.team()
                .withId(UUID.randomUUID().toString())
                .withTeamColour("#FFFFFF")
                .withShortName("ENG")
                .withName("England")
                .withLevel(TeamLevel.International)
                .withSquad(ImmutableList.of(
                    ALI, ANDERSON, ARCHER, BAIRSTOW, BANTON, BILLINGS, BROAD, BROWN, BURNS, BUTTLER, CRAWLEY, SAM_CURRAN,
                    TOM_CURRAN, DENLY, GREGORY, JORDAN, LEACH, MAHMOOD, MALAN, MORGAN, OVERTON, PARKINSON, POPE, RASHID,
                    ROOT, ROY, SIBLEY, STOKES, VINCE, WOAKES
                )).build())
            .withBattingOrder(ImmutableList.of(
                BANTON, MALAN, VINCE, MORGAN, BILLINGS, SAM_CURRAN, GREGORY, TOM_CURRAN, MAHMOOD, BROWN, PARKINSON
            ))
            .withCaptain(MORGAN)
            .withWicketKeeper(BILLINGS)
            .build();

        control = MatchControl.newMatch(
            matchStarting()
                .withMatchType(MatchType.T20I)
                .withOversPerInnings(20)
                .withNumberOfInningsPerTeam(1)
                .withMatchID("1187667")
                .withTeams(ImmutableList.of(nz, eng))
                .withTime(localTime(13, 0))
                .withScheduledStartTime(localTime(14, 0))
                .withVenue(
                    venue()
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
            .withBowlingTeam(eng)
            .withTime(localTime(14, 0))
        );

        control.onEvent(overStarting("SM Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("TK Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Mahmood"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Brown"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(TOM_CURRAN));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("TK Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(MAHMOOD));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Brown"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Mahmood"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Parkinson"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Bowled));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("SM Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Parkinson"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("SM Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Gregory"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Brown"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Gregory"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("5nb"));
        control.onEvent(ballCompleted("1nb"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("TK Curran"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(BANTON));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Mahmood"));
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

        control.onEvent(overStarting("Brown"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Mahmood"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.LegBeforeWicket));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("SM Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Bowled));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting("TK Curran"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.RunOut).withDismissedBatter(MITCH_SANTNER).withFielder(BILLINGS));
        control.onEvent(overCompleted());


        control.onEvent(inningsCompleted().withTime(localTime(15, 38)));
        control.onEvent(inningsStarting()
            .withBattingTeam(eng)
            .withBowlingTeam(nz)
            .withTime(localTime(14, 0))
        );

        control.onEvent(overStarting("Southee"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Ferguson"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1lb"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Tickner"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Bowled));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Ferguson"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Tickner"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Southee"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Sodhi"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Santner"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Neesham"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Santner"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Sodhi"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(MARTIN_GUPTILL));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Santner"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Tickner"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("4"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Ferguson"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Santner"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(COLIN_MUNRO));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Southee"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.RunOut).withFielder(COLIN_MUNRO));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Tickner"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(TIM_SOUTHEE));
        control.onEvent(batterInningsStarting());
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Ferguson"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Bowled));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("W").withDismissal(DismissalType.Caught).withFielder(COLIN_MUNRO));
        control.onEvent(batterInningsStarting());
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Sodhi"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("1w"));
        control.onEvent(ballCompleted("6"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(overCompleted());

        control.onEvent(overStarting("Southee"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("0"));
        control.onEvent(ballCompleted("1"));
        control.onEvent(ballCompleted("2"));
        control.onEvent(overCompleted());

        control.onEvent(inningsCompleted().withTime(localTime(18, 38)));
        control.onEvent(matchCompleted().withTime(localTime(18, 38)));

        Match matchAtEnd = control.current();

        String actual = AsciiScorecardRenderer.toString(matchAtEnd);
        String expected = "NEW ZEALAND vs ENGLAND" + NEWLINE +
            "======================" + NEWLINE +
            "" + NEWLINE +
            "T20I at Saxton Oval, Nelson, Nov 5 2019" + NEWLINE +
            "" + NEWLINE +
            "New Zealand won by 14 runs" + NEWLINE +
            "" + NEWLINE +
            "New Zealand Innings (20 overs maximum)" + NEWLINE +
            "--------------------------------------" + NEWLINE +
            "" + NEWLINE +
            "BATTER                                        R   M   B 4s 6s     SR" + NEWLINE +
            "C Munro          c Mahmood b Curran           6       8  0  0   75.0" + NEWLINE +
            "M Guptill        c Curran b Brown            33      17  7  0  194.1" + NEWLINE +
            "T Seifert        b Parkinson                  7      12  1  0   58.3" + NEWLINE +
            "C de Grandhomme  c Banton b Curran           55      35  5  3  157.1" + NEWLINE +
            "R Taylor         lbw b Mahmood               27      24  2  1  112.5" + NEWLINE +
            "J Neesham        b Curran                    20      15  2  1  133.3" + NEWLINE +
            "M Santner        run out (Billings)          15       9  2  0  166.7" + NEWLINE +
            "T Southee        not out                      1       2  0  0   50.0" + NEWLINE +
            "Extras           (lb 6, w 8, nb 2)           16" + NEWLINE +
            "TOTAL            (7 wkts; 20.0 overs)       180" + NEWLINE +
            "" + NEWLINE +
            "Did not bat: I Sodhi, L Ferguson, B Tickner" + NEWLINE +
            "" + NEWLINE +
            "Fall of wickets: 1-40 (M Guptill), 2-42 (C Munro), 3-69 (T Seifert)," + NEWLINE +
            "4-135 (C de Grandhomme), 5-162 (R Taylor), 6-171 (J Neesham), 7-180 (M Santner)" + NEWLINE +
            "" + NEWLINE +
            "BOWLING                O   M   R   W   Econ 0s 4s 6s WD NB" + NEWLINE +
            "S Curran               4   0  29   1    7.2  8  3  0  2  0" + NEWLINE +
            "T Curran               4   0  29   2    7.2  9  4  0  1  0" + NEWLINE +
            "S Mahmood              4   0  49   1   12.2  6  6  2  3  0" + NEWLINE +
            "P Brown                4   0  34   1    8.5 10  5  1  0  0" + NEWLINE +
            "M Parkinson            2   0  14   1    7.0  5  0  1  1  0" + NEWLINE +
            "L Gregory              2   0  19   0    9.5  4  1  1  0  2" + NEWLINE +
            "" + NEWLINE +
            "England Innings (20 overs maximum)" + NEWLINE +
            "----------------------------------" + NEWLINE +
            "" + NEWLINE +
            "BATTER                                        R   M   B 4s 6s     SR" + NEWLINE +
            "T Banton         b Tickner                   18      10  2  1  180.0" + NEWLINE +
            "D Malan          c Guptill b Sodhi           55      34  8  1  161.8" + NEWLINE +
            "J Vince          c Munro b Santner           49      39  4  1  125.6" + NEWLINE +
            "E Morgan         c Southee b Tickner         18      13  0  2  138.4" + NEWLINE +
            "S Billings       run out (Munro)              1       2  0  0   50.0" + NEWLINE +
            "S Curran         c Munro b Ferguson           2       6  0  0   33.3" + NEWLINE +
            "L Gregory        b Ferguson                   0       2  0  0    0.0" + NEWLINE +
            "T Curran         not out                     14      10  0  1  140.0" + NEWLINE +
            "S Mahmood        not out                      3       4  0  0   75.0" + NEWLINE +
            "Extras           (lb 1, w 5)                  6" + NEWLINE +
            "TOTAL            (7 wkts; 20.0 overs)       166" + NEWLINE +
            "" + NEWLINE +
            "Did not bat: P Brown, M Parkinson" + NEWLINE +
            "" + NEWLINE +
            "Fall of wickets: 1-27 (T Banton), 2-90 (D Malan), 3-139 (E Morgan)," + NEWLINE +
            "4-142 (S Billings), 5-147 (J Vince), 6-148 (L Gregory), 7-149 (S Curran)" + NEWLINE +
            "" + NEWLINE +
            "BOWLING                O   M   R   W   Econ 0s 4s 6s WD NB" + NEWLINE +
            "T Southee              4   0  28   0    7.0  9  4  0  0  0" + NEWLINE +
            "L Ferguson             4   0  25   2    6.2 11  2  1  1  0" + NEWLINE +
            "B Tickner              4   0  25   2    6.2 11  1  1  1  0" + NEWLINE +
            "I Sodhi                3   0  30   1   10.0  4  3  1  1  0" + NEWLINE +
            "M Santner              4   0  41   1   10.2  7  3  2  2  0" + NEWLINE +
            "J Neesham              1   0  16   0   16.0  0  1  1  0  0" + NEWLINE;
        assertEquals(
            expected,
            actual);

    }

    @NotNull
    private OverStartingEvent.Builder overStarting(String name) {
        LineUp team = control.current().currentInnings().get().bowlingTeam();
        Optional<Player> bowler = team.findPlayer(name);
        if (bowler.isEmpty()) {
            throw new RuntimeException("Not sure who " + name + " is");
        }
        return OverStartingEvent.overStarting()
            .withBowler(bowler.get());
    }

    private static Instant localTime(int hour, int minute) {
        return LocalDateTime.of(2019, 11, 5, hour, minute)
            .atZone(NZ_TIME.toZoneId())
            .toInstant();
    }

}
