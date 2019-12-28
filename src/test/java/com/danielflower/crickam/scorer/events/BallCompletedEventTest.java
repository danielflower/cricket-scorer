package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.DismissalType;
import com.danielflower.crickam.scorer.Score;
import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.events.BallCompletedEvent.ballCompleted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BallCompletedEventTest {

    @Test
    public void itGuessesIfTheBattersCrossedIfNotSet() {
        assertThat(crossedFor(Score.DOT_BALL), is(false));
        assertThat(crossedFor(Score.SINGLE), is(true));
        assertThat(crossedFor(Score.TWO), is(false));
        assertThat(crossedFor(Score.THREE), is(true));
        assertThat(crossedFor(Score.FOUR), is(false));
        assertThat(crossedFor(Score.SIX), is(false));
        assertThat(crossedFor(Score.BYE), is(true));
        assertThat(crossedFor(Score.LEG_BYE), is(true));
        assertThat(crossedFor(Score.WICKET), is(false));
        assertThat(crossedFor(Score.NO_BALL), is(false));
        assertThat(crossedFor(Score.WIDE), is(false));
        assertThat(crossedFor(Score.LEG_BYE), is(true));
        assertThat(crossedFor(Score.BYE), is(true));
        assertThat(crossedFor(Score.EMPTY), is(false));
        assertThat(crossedFor(Score.score().withPenaltyRuns(1).withValidDeliveries(1).withFacedByBatter(1).build()), is(false));
        assertThat(crossedFor(Score.score().withWides(5).withWideDeliveries(1).build()), is(false));
        assertThat(crossedFor(Score.score().withNoBalls(1).withBatterRuns(4).withFacedByBatter(1).build()), is(false));
        assertThat(crossedFor(Score.parse("2w").get()), is(true));
    }

    private static boolean crossedFor(Score score) {
        BallCompletedEvent.Builder builder = ballCompleted().withRunsScored(score);
        if (score.wickets() > 0) {
            builder.withDismissal(DismissalType.Bowled);
        }
        return builder.build().playersCrossed();
    }

}