package com.danielflower.crickam.scorer;

import java.util.Optional;

/**
 * A swing of the bat
 */
public final class Swing {

    private final ShotType shotType;
    private final ImpactOnBat impactOnBat;
    private final Double timing;
    private final Double footDirection;
    private final Double power;

    public Swing(ShotType shotType, ImpactOnBat impactOnBat, Double timing, Double footDirection, Double power) {
        this.shotType = shotType;
        this.impactOnBat = impactOnBat;
        this.timing = timing;
        this.footDirection = footDirection;
        this.power = power;
    }

    public Optional<ShotType> shotType() {
        return Optional.ofNullable(shotType);
    }

    public Optional<ImpactOnBat> impactOnBat() {
        return Optional.ofNullable(impactOnBat);
    }

    /**
     * @return A number between -1 and 1 where -1 means the shot was much to early; 0 is perfectly timed; 1 is very late.
     */
    public Optional<Double> timing() {
        return Optional.ofNullable(timing);
    }

    /**
     * A number between -1 and 1 where -1 indicates the foot went back to the wicket; 0 is at the crease; 1 is a
     * long way stretched forward;
     */
    public Optional<Double> footDirection() {
        return Optional.ofNullable(footDirection);
    }

    /**
     * A number between 0 and 1, where 0 is no movement of the bat; 0.5 is a normal shot; and 1 is the batter's
     * maximum power.
     */
    public Optional<Double> power() {
        return Optional.ofNullable(power);
    }

    @Override
    public String toString() {
        return "Swing{" +
                "shotType=" + shotType +
                ", impact=" + impactOnBat +
                ", timing=" + timing +
                ", footDirection=" + footDirection +
                ", power=" + power +
                '}';
    }

    public static Builder swing() {
        return new Builder();
    }


    public static final class Builder {
        private ShotType shotType;
        private ImpactOnBat impact;
        private Double timing;
        private Double footDirection;
        private Double power;

        public Builder withShotType(ShotType shotType) {
            this.shotType = shotType;
            return this;
        }

        public Builder withImpact(ImpactOnBat impact) {
            this.impact = impact;
            return this;
        }

        public Builder withTiming(Double timing) {
            this.timing = timing;
            return this;
        }

        public Builder withFootDirection(Double footDirection) {
            this.footDirection = footDirection;
            return this;
        }

        public Builder withPower(Double power) {
            this.power = power;
            return this;
        }

        public Swing build() {
            return new Swing(shotType, impact, timing, footDirection, power);
        }

    }
}
