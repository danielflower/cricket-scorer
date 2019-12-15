package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TrajectoryTest {

    @Test
    public void canCreate() {
        Trajectory trajectory = aTrajectory().setDirectionInDegreesRelativeToBatter(-90.0).build();
        assertThat(trajectory.getDirectionInDegreesRelativeToBatter(), is(-90.0));
    }

    public static TrajectoryBuilder aTrajectory() {
        return new TrajectoryBuilder()
            .setDistance(10.0)
            .setElevationInDegrees(12.0)
            .setSpeedInKms(123)
            .setDirectionInDegreesRelativeToBatter(90.0);
    }

}