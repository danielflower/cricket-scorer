package com.danielflower.crickam.scorer;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import static com.danielflower.crickam.scorer.Crictils.toOptional;

/**
 * The trajectory of the ball coming off the bat
 */
public final class Trajectory {
    private final Integer speedInKms;
    private final Double directionInDegreesRelativeToBatter;
    private final Double launchAngle;
    private final Double distanceInMeters;

    private Trajectory(Integer speedInKms, Double directionInDegreesRelativeToBatter, Double launchAngle, Double distanceInMeters) {
        this.speedInKms = speedInKms;
        this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
        this.launchAngle = launchAngle;
        this.distanceInMeters = distanceInMeters;
    }

    /**
     * The direction that the ball was hit in.
     * <ul>
     * <li>
     * 0 == 360 == straight behind to the wicket keeper;
     * </li>
     * <li>
     * 90 = square leg (for right hander);
     * </li>
     * <li>
     * 180 = straight back to bowler;
     * </li>
     * <li>
     * 270 = point (for right hander)
     * </li>
     * <li>
     * empty = unknown
     * </li>
     * </ul>
     * @return The direction that the ball was hit in
     */
    public Optional<Double> directionInDegreesRelativeToBatter() {
        return Optional.ofNullable(directionInDegreesRelativeToBatter);
    }

    /**
     * @return The angle the ball came off the bat. A negative number is hitting into the ground (with -90.0 being
     * directly downward); positive is in the air (with 90.0 being straight up).
     */
    public Optional<Double> launchAngle() {
        return Optional.ofNullable(launchAngle);
    }

    public OptionalInt speedInKms() {
        return toOptional(speedInKms);
    }

    public Optional<Double> distanceInMeters() {
        return Optional.ofNullable(distanceInMeters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trajectory that = (Trajectory) o;
        return Objects.equals(speedInKms, that.speedInKms) &&
            Objects.equals(directionInDegreesRelativeToBatter, that.directionInDegreesRelativeToBatter) &&
            Objects.equals(launchAngle, that.launchAngle) &&
            Objects.equals(distanceInMeters, that.distanceInMeters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speedInKms, directionInDegreesRelativeToBatter, launchAngle, distanceInMeters);
    }

    @Override
    public String toString() {
        return "Trajectory{" +
            "speedInKms=" + speedInKms +
            ", directionInDegreesRelativeToBatter=" + directionInDegreesRelativeToBatter +
            ", launchAngle=" + launchAngle +
            ", distance=" + distanceInMeters +
            '}';
    }

    public static Builder trajectory() {
        return new Builder();
    }

    public static final class Builder {
        private Integer speedInKms;
        private Double directionInDegreesRelativeToBatter;
        private Double launchAngle;
        private Double distanceInMeters;

        public Builder withSpeedInKms(Integer speedInKms) {
            this.speedInKms = speedInKms;
            return this;
        }

        /**
         * The direction that the ball was hit in.
         * <ul>
         * <li>
         * 0 == 360 == straight behind to the wicket keeper;
         * </li>
         * <li>
         * 90 = square leg (for right hander);
         * </li>
         * <li>
         * 180 = straight back to bowler;
         * </li>
         * <li>
         * 270 = point (for right hander)
         * </li>
         * <li>
         * null = unknown
         * </li>
         * </ul>
         * @param directionInDegreesRelativeToBatter The direction
         * @return This builder
         */
        public Builder withDirectionInDegreesRelativeToBatter(Double directionInDegreesRelativeToBatter) {
            this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
            return this;
        }

        /**
         * @param launchAngle The angle the ball came off the bat. A negative number is hitting into the ground
         *                    (with -90.0 being directly downward); positive is in the air (with 90.0 being straight up).
         * @return This builder
         */
        public Builder withLaunchAngle(Double launchAngle) {
            this.launchAngle = launchAngle;
            return this;
        }

        public Builder withDistanceInMeters(Double distanceInMeters) {
            this.distanceInMeters = distanceInMeters;
            return this;
        }

        public Trajectory build() {
            return new Trajectory(speedInKms, directionInDegreesRelativeToBatter, launchAngle, distanceInMeters);
        }

    }
}
