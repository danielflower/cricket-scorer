package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.PlayerTest.aPlayer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BallTest {


    @Test
    public void canCreateBalls() {
        BallAtCompletion ball = aBall().withId(2).withNumberInOver(3).build();
        assertThat(ball.id(), is(2));
        assertThat(ball.getNumberInOver(), is(3));
        assertThat(ball.getNonStriker().getPlayer().familyName(), is("Guptil"));
    }


    public static BallBuilder aBall() {
        return new BallBuilder()
            .withId(1)
            .withDateCompleted(Instant.now())
            .withBowlingSpell(BowlingSpellTest.aBowlingSpell().build())
            .withDelivery(DeliveryTest.aDelivery().build())
            .withDismissal(Optional.empty())
            .withFielder(Optional.empty())
            .withStriker(BatsmanInningsTest.aBatsmanInnings().build())
            .withNonStriker(BatsmanInningsTest.aBatsmanInnings().setPlayer(aPlayer().withName("Martin Guptil")).build())
            .withNumberInOver(1)
            .withSwing(SwingTest.aSwing().build())
            .withTrajectoryAtImpact(TrajectoryTest.aTrajectory().build())
            .withScore(ScoreBuilder.Empty)
            .withPlayersCrossed(false)
            ;
    }

}