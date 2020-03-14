package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.data.Australia.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DismissalTest {

    private final LineUp aus = LineUp.lineUp()
        .withCaptain(AARON_FINCH)
        .withWicketKeeper(ALEX_CAREY)
        .withTeam(Team.team()
            .withShortName("AUS")
            .withName("Australia").build())
        .withBattingOrder(ImmutableList.of(
            DAVID_WARNER, AARON_FINCH, STEVEN_SMITH, PETER_HANDSCOMB, ALEX_CAREY, MARCUS_STOINIS, GLENN_MAXWELL,
            PAT_CUMMINS, MITCHELL_STARC, JASON_BEHRENDORFF, NATHON_LYON
        )).build();

    @Test
    void inScorecardsTheTeamIsOptional() {
        Dismissal dismissal = Dismissal.dismissal()
            .withType(DismissalType.CAUGHT)
            .withFielder(AARON_FINCH)
            .withBowler(Australia.ASHTON_TURNER)
            .withBatter(NewZealand.AJAZ_PATEL)
            .build();
        assertThat(dismissal.toScorecardString(null), is("c Finch b Turner"));
    }

    @Test
    void inScorecardsTheCatcherIsMarkedIfTeamSpecified() {
        Dismissal.Builder dismissal = Dismissal.dismissal()
            .withType(DismissalType.CAUGHT)
            .withBowler(NATHON_LYON)
            .withBatter(NewZealand.AJAZ_PATEL)
            ;
        assertThat(dismissal.withFielder(NATHON_LYON).build().toScorecardString(aus),
            is("c & b Lyon"));
        assertThat(dismissal.withFielder(DAVID_WARNER).build().toScorecardString(aus),
            is("c Warner b Lyon"));
        assertThat(dismissal.withFielder(aus.wicketKeeper()).build().toScorecardString(aus),
            is("c †Carey b Lyon"));
        assertThat(dismissal.withFielder(ASHTON_TURNER).build().toScorecardString(aus),
            is("c sub (Turner) b Lyon"));
    }

    @Test
    void stumpingsCanBeDoneByMultiplePeople() {
        Dismissal.Builder dismissal = Dismissal.dismissal()
            .withType(DismissalType.STUMPED)
            .withBowler(aus.battingOrder().get(10))
            .withBatter(NewZealand.AJAZ_PATEL)
            ;
        assertThat(dismissal.withFielder(aus.wicketKeeper()).build().toScorecardString(aus),
            is("st †Carey b Lyon"));
        assertThat(dismissal.withFielder(AARON_FINCH).build().toScorecardString(aus),
            is("st Finch b Lyon"));
        assertThat(dismissal.withFielder(ASHTON_TURNER).build().toScorecardString(aus),
            is("st sub (Turner) b Lyon"));
    }

    @Test
    void bowledIsBowled() {
        Dismissal.Builder dismissal = Dismissal.dismissal()
            .withType(DismissalType.BOWLED)
            .withBowler(aus.battingOrder().get(10))
            .withBatter(NewZealand.AJAZ_PATEL)
            ;
        assertThat(dismissal.build().toScorecardString(aus), is("b Lyon"));
        assertThat(dismissal.build().toScorecardString(null), is("b Lyon"));
    }
}