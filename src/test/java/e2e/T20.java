package e2e;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.events.BallCompletedEvent;
import com.danielflower.crickam.scorer.events.OverStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static com.danielflower.crickam.scorer.Venue.venue;
import static com.danielflower.crickam.scorer.data.England.*;
import static com.danielflower.crickam.scorer.data.NewZealand.*;
import static com.danielflower.crickam.scorer.events.InningsCompletedEvent.inningsCompleted;
import static com.danielflower.crickam.scorer.events.InningsStartingEvent.inningsStarting;
import static com.danielflower.crickam.scorer.events.MatchStartingEvent.matchStarting;
import static com.danielflower.crickam.scorer.events.OverCompletedEvent.overCompleted;

public class T20 {

    public static final TimeZone NZ_TIME = TimeZone.getTimeZone("Pacific/Auckland");

    @Test
    public void canScoreT20Games() {

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

        MatchControl control = MatchControl.newMatch(
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
        control.onEvent(overStarting(eng, "SM Curran"));
        control.onEvent(ball("1"));
        control.onEvent(overCompleted());


        control.onEvent(inningsCompleted().withTime(localTime(15, 38)));
        control.onEvent(inningsStarting()
            .withBattingTeam(eng)
            .withBowlingTeam(nz)
            .withTime(localTime(14, 0))
        );


    }

    private BallCompletedEvent.Builder ball(String scoreText) {
        Score score = Score.parse(scoreText).orElseThrow(() -> new RuntimeException("Unknown score: " + scoreText));
        return BallCompletedEvent.ballCompleted()
            .withRunsScored(score);
    }

    @NotNull
    private OverStartingEvent.Builder overStarting(LineUp eng, String name) {
        Optional<Player> bowler = eng.findPlayer(name);
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
