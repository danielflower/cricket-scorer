package com.danielflower.crickam.scorer;

import java.util.Optional;

public final class Swing {

    private final Optional<ShotType> shotType;
    private final Optional<Impact> impact;
    private final Optional<Double> timing;
    private final Optional<Double> footDirection;
    private final Optional<Double> power;

    public Swing(Optional<ShotType> shotType, Optional<Impact> impact, Optional<Double> timing, Optional<Double> footDirection, Optional<Double> power) {
        this.shotType = shotType;
        this.impact = impact;
        this.timing = timing;
        this.footDirection = footDirection;
        this.power = power;
    }

    public Optional<ShotType> getShotType() {
        return shotType;
    }

    public Optional<Impact> getImpact() {
        return impact;
    }

    /**
     * A number between -1 and 1 where -1 means the shot was much to early; 0 is perfectly timed; 1 is very late.
     */
    public Optional<Double> getTiming() {
        return timing;
    }

    /**
     * A number between -1 and 1 where -1 indicates the foot went back to the wicket; 0 is at the crease; 1 is a
     * long way stretched forward;
     */
    public Optional<Double> getFootDirection() {
        return footDirection;
    }

    /**
     * A number between 0 and 1, where 0 is no movement of the bat; 0.5 is a normal shot; and 1 is the batter's
     * maximum power.
     */
    public Optional<Double> getPower() {
        return power;
    }

    @Override
    public String toString() {
        return "Swing{" +
                "shotType=" + shotType +
                ", impact=" + impact +
                ", timing=" + timing +
                ", footDirection=" + footDirection +
                ", power=" + power +
                '}';
    }
}
