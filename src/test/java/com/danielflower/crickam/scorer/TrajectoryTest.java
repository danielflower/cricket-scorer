package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TrajectoryTest {

    @Test
    public void canCreate() {
        Trajectory trajectory = aTrajectory().withDirectionInDegreesRelativeToBatter(-90.0).build();
        assertThat(trajectory.directionInDegreesRelativeToBatter(), is(Optional.of(-90.0)));
    }

    public static Trajectory.Builder aTrajectory() {
        return new Trajectory.Builder()
            .withDistanceInMeters(10.0)
            .withLaunchAngle(12.0)
            .withSpeedInKms(123)
            .withDirectionInDegreesRelativeToBatter(90.0);
    }

}