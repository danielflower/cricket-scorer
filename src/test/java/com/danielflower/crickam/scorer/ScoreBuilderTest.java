package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.Score.*;
import static com.danielflower.crickam.scorer.ScoreBuilder.score;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

class ScoreBuilderTest {

    @Test
    public void cachedVersionsAreReturnedForCommonOnes() {
        assertThat(score().withValidBalls(1).withSingles(1).withRunsFromBat(1).build(), is(sameInstance(SINGLE)));
        assertThat(score().withValidBalls(1).withDots(1).build(), is(sameInstance(DOT_BALL)));
        assertThat(score().withValidBalls(1).withRunsFromBat(6).withSixes(1).build(), is(sameInstance(SIX)));
        assertThat(score().withValidBalls(1).withRunsFromBat(4).withFours(1).build(), is(sameInstance(FOUR)));
        assertThat(score().withValidBalls(1).withThrees(1).withRunsFromBat(3).build(), is(sameInstance(THREE)));
        assertThat(score().withValidBalls(1).withTwos(1).withRunsFromBat(2).build(), is(sameInstance(TWO)));
        assertThat(score().withValidBalls(1).withSingles(1).withRunsFromBat(1).build(), is(sameInstance(SINGLE)));
        assertThat(score().withWides(1).build(), is(sameInstance(WIDE)));
        assertThat(score().withNoBalls(1).build(), is(sameInstance(NO_BALL)));
        assertThat(score().withValidBalls(1).withDots(1).withWickets(1).build(), is(sameInstance(WICKET)));
    }

    @Test
    public void stumpingFromWideHasRunsAndWicket() {
        Score stumpingFromWide = score().withWides(1).withWickets(1).build();
        assertThat(stumpingFromWide.teamRuns(), is(1));
        assertThat(stumpingFromWide.wickets(), is(1));
        assertThat(stumpingFromWide.extras(), is(1));
        assertThat(stumpingFromWide.bowlerRuns(), is(1));
        assertThat(stumpingFromWide.batterRuns(), is(0));
    }

}