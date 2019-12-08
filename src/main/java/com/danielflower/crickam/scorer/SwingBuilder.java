package com.danielflower.crickam.scorer;

import java.util.Optional;

public class SwingBuilder {
    private Optional<ShotType> shotType = Optional.empty();
    private Optional<Impact> impact = Optional.empty();
    private Optional<Double> timing = Optional.empty();
    private Optional<Double> footDirection = Optional.empty();
    private Optional<Double> power = Optional.empty();

    public SwingBuilder setShotType(Optional<ShotType> shotType) {
        this.shotType = shotType;
        return this;
    }

    public SwingBuilder setImpact(Optional<Impact> impact) {
        this.impact = impact;
        return this;
    }

    public SwingBuilder setTiming(Optional<Double> timing) {
        this.timing = timing;
        return this;
    }

    public SwingBuilder setFootDirection(Optional<Double> footDirection) {
        this.footDirection = footDirection;
        return this;
    }

    public SwingBuilder setPower(Optional<Double> power) {
        this.power = power;
        return this;
    }

    public Swing build() {
        return new Swing(shotType, impact, timing, footDirection, power);
    }

    public static SwingBuilder aSwing() {
        return new SwingBuilder();
    }
}
