package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public interface BallAtEngagement extends BallAtStart {
    Delivery getDelivery();
    Swing getSwing();
    Trajectory getTrajectoryAtImpact();

    BallAtCompletion complete(Score score, Optional<Dismissal> dismissal, boolean playersCrossed, Optional<Player> fielder, Instant dateCompleted);
}
