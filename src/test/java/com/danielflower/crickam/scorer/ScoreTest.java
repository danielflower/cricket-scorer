package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ScoreTest {

    @Test
    void battersAndBowlersAndTheTeamAreAscribedDifferentScores() {
        Score score = ScoreBuilder.score()
            .withByes(1)
            .withLegByes(2)
            .withNoBalls(3)
            .withPenaltyRuns(4)
            .withWides(5)
            .withWickets(2)
            .withValidBalls(67)
            .withDots(7)
            .withSingles(8)
            .withTwos(9)
            .withThrees(10)
            .withFours(11)
            .withSixes(12)
            .withRunsFromBat(172)
            .build();

        assertThat(score.teamRuns(), is(172+1+2+3+4+5));
        assertThat(score.batterRuns(), is(172));
        assertThat(score.bowlerRuns(), is(172+3+4+5));
    }

}