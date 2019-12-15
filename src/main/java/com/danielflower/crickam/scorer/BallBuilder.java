package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public class BallBuilder {
    private BatsmanInnings striker;
    private BatsmanInnings nonStriker;
    private BowlingSpell bowlingSpell;
    private int numberInOver;
    private Score score;
    private boolean playersCrossed;
    private Optional<Dismissal> dismissal = Optional.empty();
    private Delivery delivery;
    private Swing swing;
    private Trajectory trajectoryAtImpact;
    private int id;
    private Optional<Player> fielder = Optional.empty();
    private Instant dateCompleted = Instant.now();

    public BallBuilder withStriker(BatsmanInnings striker) {
        this.striker = striker;
        return this;
    }

    public BallBuilder withNonStriker(BatsmanInnings nonStriker) {
        this.nonStriker = nonStriker;
        return this;
    }

    public BallBuilder withBowlingSpell(BowlingSpell bowlingSpell) {
        this.bowlingSpell = bowlingSpell;
        return this;
    }

    public BallBuilder withNumberInOver(int numberInOver) {
        this.numberInOver = numberInOver;
        return this;
    }

    public BallBuilder withScore(Score score) {
        this.score = score;
        return this;
    }

    public BallBuilder withPlayersCrossed(boolean playersCrossed) {
        this.playersCrossed = playersCrossed;
        return this;
    }

    public BallBuilder withDismissal(Optional<Dismissal> dismissal) {
        this.dismissal = dismissal;
        return this;
    }

    public BallBuilder withDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public BallBuilder withSwing(Swing swing) {
        this.swing = swing;
        return this;
    }

    public BallBuilder withTrajectoryAtImpact(Trajectory trajectoryAtImpact) {
        this.trajectoryAtImpact = trajectoryAtImpact;
        return this;
    }

    public BallBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public BallBuilder withFielder(Optional<Player> fielder) {
        this.fielder = fielder;
        return this;
    }

    public BallBuilder withDateCompleted(Instant dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    public BallAtCompletion build() {
        return new Ball(id, striker, nonStriker, numberInOver, bowlingSpell).engage(delivery, swing, trajectoryAtImpact).complete(score, dismissal, playersCrossed, fielder, dateCompleted);
    }
}
