package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;
import com.danielflower.crickam.scorer.data.Australia;
import com.danielflower.crickam.scorer.data.NewZealand;
import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.events.MatchEvents.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class BallCompletedEventTest {

    private final SimpleLineUp nz = NewZealand.oneDayLineUp().build();
    private final SimpleLineUp aus = Australia.oneDayLineUp().build();
    private final Match match = MatchControl.newMatch(
        MatchEvents.matchStarting(1, 50).withTeamLineUps(ImmutableList.of(nz, aus)).build()
    )
        .onEvent(inningsStarting().withBattingTeam(nz))
        .onEvent(batterInningsStarting())
        .onEvent(batterInningsStarting())
        .onEvent(overStarting(aus.battingOrder().last()))
        .match();

    @Test
    public void canBeRebuilt() {
        BallCompletedEvent original = new BallCompletedEvent.Builder()
            .withOverNumber(1)
            .withNumberInOver(3)
            .withNumberInMatch(29)
            .withBowler(nz.battingOrder().get(7))
            .withDelivery(Delivery.delivery().withDeliveryType(DeliveryType.ARM_BALL).withBowledFrom(WicketSide.AROUND).withChangeInLineAfterBounceInDegrees(123.0).withPositionOfBounce(8.0).withHorizontalPitchInMeters(0.5).withChangeInLineAfterBounceInDegrees(3.0).withSpeedInKilometers(86).build())
            .withDismissedBatter(aus.battingOrder().get(1))
            .withDismissal(DismissalType.CAUGHT)
            .withFielder(nz.battingOrder().get(1))
            .withStriker(aus.battingOrder().get(1))
            .withNonStriker(aus.battingOrder().get(5))
            .withPlayersCrossed(true)
            .withRunsScored(Score.WICKET)
            .withSwing(Swing.swing().withImpact(ImpactOnBat.EDGED).withPower(0.65).withFootDirection(0.53).withTiming(0.5).withShotType(ShotType.CUT).build())
            .withTrajectoryAtImpact(Trajectory.trajectory().withDirectionInDegreesRelativeToBatter(12.0).withDistanceInMeters(20.0).withLaunchAngle(33.0).withSpeedInKms(123).build())
            .withCustomData("Some-custom-data")
            .build();
        assertThat(original.newBuilder().build(), equalTo(original));
    }

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
        assertThat(crossedFor(Score.score().withPenaltyRuns(1).withValidDeliveries(1).build()), is(false));
        assertThat(crossedFor(Score.score().withWides(5).withWideDeliveries(1).build()), is(false));
        assertThat(crossedFor(Score.score().withNoBalls(1).withBatterRuns(4).build()), is(false));
        assertThat(crossedFor(Score.parse("2w")), is(true));
    }

    private boolean crossedFor(Score score) {
        BallCompletedEvent.Builder builder = ballCompleted().withRunsScored(score);
        if (score.wickets() > 0) {
            builder.withDismissal(DismissalType.BOWLED);
        }
        return builder.apply(match).build().playersCrossed();
    }

}