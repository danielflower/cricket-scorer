package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.danielflower.crickam.scorer.Score.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ScoreTest {

    private Score score = score()
        .withByes(1)
        .withLegByes(2)
        .withNoBalls(3)
        .withPenaltyRuns(4)
        .withWides(5)
        .withWideDeliveries(4)
        .withWickets(15)
        .withValidDeliveries(67)
        .withDots(7)
        .withSingles(8)
        .withTwos(9)
        .withThrees(10)
        .withFours(11)
        .withSixes(12)
        .withBatterRuns(172)
        .build();

    @Test
    void battersAndBowlersAndTheTeamAreAscribedDifferentScores() {
        assertThat(score.teamRuns(), is(172+1+2+3+4+5));
        assertThat(score.batterRuns(), is(172));
        assertThat(score.bowlerRuns(), is(172+3+4+5));
    }

    @Test
    void bowlingExtrasAreWidesAndNoBalls() {
        assertThat(score.bowlingExtras(), is(8));
    }

    @Test
    void fieldingExtrasAreByesAndLegByesAndPenalties() {
        assertThat(score.fieldingExtras(), is(7));
    }

    @Test
    void extrasAreFieldingExtrasPlusBowlingExtras() {
        assertThat(score.extras(), is(15));
    }

    @Test
    public void scoresCanBeAddedAndSubtracted() {
        Score operand = score()
            .withByes(1)
            .withLegByes(1)
            .withNoBalls(1)
            .withPenaltyRuns(1)
            .withWides(1)
            .withWideDeliveries(1)
            .withWickets(3)
            .withValidDeliveries(1)
            .withDots(1)
            .withSingles(1)
            .withTwos(1)
            .withThrees(1)
            .withFours(1)
            .withSixes(1)
            .withBatterRuns(16)
            .build();

        Score added = score.add(operand);
        assertThat(added, is(score()
            .withByes(1+1)
            .withLegByes(1+2)
            .withNoBalls(1+3)
            .withPenaltyRuns(1+4)
            .withWides(1+5)
            .withWideDeliveries(1+4)
            .withWickets(3+15)
            .withValidDeliveries(1+67)
            .withDots(1+7)
            .withSingles(1+8)
            .withTwos(1+9)
            .withThrees(1+10)
            .withFours(1+11)
            .withSixes(1+12)
            .withBatterRuns(16+172)
            .build()));

        assertThat(added.subtract(operand), is(score));
    }

    @Test
    void strikeRateIsBasedOnBatterRunsAndBatterFaces() {
        assertThat(score().withWides(10).withNoBalls(2).withValidDeliveries(8).withSingles(5).withBatterRuns(5).build().battingStrikeRate(), is(50.0));
        assertThat(score().withWides(10).withNoBalls(2).withValidDeliveries(8).build().battingStrikeRate(), is(0.0));
        assertThat(score().withWides(10).build().battingStrikeRate(), is(nullValue()));
    }

    @Test
    void averageIsBasedOnBatterRunsAndWickets() {
        assertThat(score().withWides(10).withNoBalls(3).withValidDeliveries(10).withSingles(5).withBatterRuns(5).withWickets(2).build().battingAverage(), is(2.5));
        assertThat(score().withWides(10).withNoBalls(3).withValidDeliveries(10).withSingles(5).withBatterRuns(5).withWickets(0).build().battingAverage(), is(nullValue()));
    }

    @Test
    public void bowlerStrikeRateIsBasedOnValidDeliveriesBowled() {
        assertThat(score().withWides(10).withNoBalls(3).withValidDeliveries(10).withSingles(1).withBatterRuns(1).withWickets(2).build().bowlingStrikeRate(), is(5.0));
        assertThat(score().withWides(10).withNoBalls(3).withValidDeliveries(10).withSingles(5).withBatterRuns(5).withWickets(0).build().bowlingStrikeRate(), is(nullValue()));
    }

    @Test
    void rpoIsZeroIfNoBallsBowled() {
        assertThat(Score.EMPTY.runsPerOver().toString(), is("0.0"));
        assertThat(Score.EMPTY.bowlerEconomyRate().toString(), is("0.0"));
    }

    @Test
    void rpoIsTotalRunsByValidDeliveries() {
        assertThat(score().withWides(1).withNoBalls(1).withLegByes(2).withValidDeliveries(9)
            .withSingles(2).withBatterRuns(2).withWickets(2).build().runsPerOver().toString(), is("4.0"));
    }

    @Test
    public void bowlerEconomyRateExcludesFieldingExtras(){
        assertThat(score().withWides(1).withNoBalls(1).withPenaltyRuns(1).withLegByes(2).withValidDeliveries(9)
            .withSingles(2).withBatterRuns(2).withWickets(2).build().bowlerEconomyRate().toString(), is("2.6"));
    }

    @Test
    void whenMankadedAWicketIsTakenWithNoDeliveriesMade() {
        Score mankaded = score().withWickets(1).build();
        assertThat(mankaded.validDeliveries(), is(0));
        assertThat(mankaded.wickets(), is(1));
        assertThat(mankaded.dots(), is(0));
    }

    @Test
    public void cachedVersionsAreReturnedForCommonOnes() {
        assertThat(score().withValidDeliveries(1).withSingles(1).withBatterRuns(1).build(), is(sameInstance(SINGLE)));
        assertThat(score().withValidDeliveries(1).withDots(1).build(), is(sameInstance(DOT_BALL)));
        assertThat(score().withValidDeliveries(1).withBatterRuns(6).withSixes(1).build(), is(sameInstance(SIX)));
        assertThat(score().withValidDeliveries(1).withBatterRuns(4).withFours(1).build(), is(sameInstance(FOUR)));
        assertThat(score().withValidDeliveries(1).withThrees(1).withBatterRuns(3).build(), is(sameInstance(THREE)));
        assertThat(score().withValidDeliveries(1).withTwos(1).withBatterRuns(2).build(), is(sameInstance(TWO)));
        assertThat(score().withValidDeliveries(1).withSingles(1).withBatterRuns(1).build(), is(sameInstance(SINGLE)));
        assertThat(score().withWides(1).withWideDeliveries(1).build(), is(sameInstance(WIDE)));
        assertThat(score().withNoBalls(1).build(), is(sameInstance(NO_BALL)));
        assertThat(score().withValidDeliveries(1).withDots(1).withWickets(1).build(), is(sameInstance(WICKET)));
        assertThat(score().build(), is(sameInstance(EMPTY)));
    }

    @Test
    public void stumpingFromWideHasRunsAndWicket() {
        Score stumpingFromWide = score().withWides(1).withWickets(1).build();
        assertThat(stumpingFromWide.teamRuns(), is(1));
        assertThat(stumpingFromWide.wickets(), is(1));
        assertThat(stumpingFromWide.extras(), is(1));
        assertThat(stumpingFromWide.bowlerRuns(), is(1));
        assertThat(stumpingFromWide.batterRuns(), is(0));
        assertThat(stumpingFromWide.validDeliveries(), is(0));
    }

    @Test
    public void aBuilderCanBeCreatedFromAScore() {
        assertThat(Builder.from(score).build(), equalTo(score));
    }

    @Test
    public void scorecardBallsCanBeParsed() {
        assertThat(Score.parse("."), is(sameInstance(DOT_BALL)));
        assertThat(Score.parse("0"), is(sameInstance(DOT_BALL)));
        assertThat(Score.parse("1"), is(sameInstance(SINGLE)));
        assertThat(Score.parse("2"), is(sameInstance(TWO)));
        assertThat(Score.parse("3"), is(sameInstance(THREE)));
        assertThat(Score.parse("4"), is(sameInstance(FOUR)));
        assertThat(Score.parse("6"), is(sameInstance(SIX)));
        assertThat(Score.parse("1b"), is(sameInstance(BYE)));
        assertThat(Score.parse("1lb"), is(sameInstance(LEG_BYE)));
        assertThat(Score.parse("4lb"), is(score().withValidDeliveries(1).withLegByes(4).withDots(1).build()));
        assertThat(Score.parse("1nb"), is(sameInstance(NO_BALL)));
        assertThat(Score.parse("1w"), is(sameInstance(WIDE)));
        assertThat(Score.parse("2w"), is(score().withWides(2).withWideDeliveries(1).build()));
        assertThat(Score.parse("W"), is(sameInstance(WICKET)));
        assertThat(Score.parse("5w"), is(score().withWides(5).withWideDeliveries(1).build()));
        assertThat(Score.parse("5nb"), is(score().withNoBalls(1).withBatterRuns(4).withFours(1).build()));
        assertThat(Score.parse("7nb"), is(score().withNoBalls(1).withBatterRuns(6).withSixes(1).build()));
    }

    @Test
    public void invalidDeliveriesAreInferred() {
        assertThat(score().withWides(5).withWideDeliveries(1).withNoBalls(2).build().invalidDeliveries(), is(3));
    }

}