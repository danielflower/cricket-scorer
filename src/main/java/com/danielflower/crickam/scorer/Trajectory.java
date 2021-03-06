package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * The trajectory of the ball coming off the bat
 */
@Immutable
public final class Trajectory {
    private final Integer speedInKms;
    private final Double directionInDegreesRelativeToBatter;
    private final Double launchAngle;
    private final Double distanceInMeters;

    private Trajectory(@Nullable Integer speedInKms, @Nullable Double directionInDegreesRelativeToBatter, @Nullable Double launchAngle, @Nullable Double distanceInMeters) {
        if (speedInKms != null && (speedInKms < 0 || speedInKms > 300)) {
            throw new IllegalArgumentException("speedInKms must be between 0 and 300, or null, but was " + speedInKms);
        }
        if (directionInDegreesRelativeToBatter != null && (directionInDegreesRelativeToBatter < 0 || directionInDegreesRelativeToBatter > 360)) {
            throw new IllegalArgumentException("directionInDegreesRelativeToBatter must be between 0 and 360, or null, but was " + directionInDegreesRelativeToBatter);
        }
        if (launchAngle != null && (launchAngle < -180 || launchAngle > 180)) {
            throw new IllegalArgumentException("launchAngle must be between -180 and 180, or null, but was " + launchAngle);
        }
        if (distanceInMeters != null && (distanceInMeters < 0 || distanceInMeters > 200)) {
            throw new IllegalArgumentException("distanceInMeters must be between 0 and 200, or null, but was " + distanceInMeters);
        }
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
     * null = unknown
     * </li>
     * </ul>
     * @return The direction that the ball was hit in
     */
    public @Nullable Double directionInDegreesRelativeToBatter() {
        return directionInDegreesRelativeToBatter;
    }

    /**
     * @return The angle the ball came off the bat. A negative number is hitting into the ground (with -90.0 being
     * directly downward); positive is in the air (with 90.0 being straight up).
     */
    public @Nullable Double launchAngle() {
        return launchAngle;
    }

    /**
     * @return The speed of the ball off the bat, or null if unknown
     */
    public @Nullable Integer speedInKms() {
        return speedInKms;
    }

    /**
     * @return The distance the ball traveled to until a fielder picked it up, or it crossed the boundary, or (for a six) when it landed. Null if unknown.
     */
    public @Nullable Double distanceInMeters() {
        return distanceInMeters;
    }

    @Override
    public boolean equals(@Nullable Object o) {
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

    public static @Nonnull Builder trajectory() {
        return new Builder();
    }

    public static final class Builder {
        private Integer speedInKms;
        private Double directionInDegreesRelativeToBatter;
        private Double launchAngle;
        private Double distanceInMeters;

        /**
         * @param speedInKms The speed of the ball off the bat, or null if unknown
         * @return This builder
         */
        public @Nonnull Builder withSpeedInKms(@Nullable Integer speedInKms) {
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
        public @Nonnull Builder withDirectionInDegreesRelativeToBatter(@Nullable Double directionInDegreesRelativeToBatter) {
            this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
            return this;
        }

        /**
         * @param launchAngle The angle the ball came off the bat. A negative number is hitting into the ground
         *                    (with -90.0 being directly downward); positive is in the air (with 90.0 being straight up).
         * @return This builder
         */
        public @Nonnull Builder withLaunchAngle(@Nullable Double launchAngle) {
            this.launchAngle = launchAngle;
            return this;
        }

        /**
         * @param distanceInMeters The distance the ball traveled to until a fielder picked it up, or it crossed the boundary, or (for a six) when it landed. null if unknown.
         * @return This builder
         */
        public @Nonnull Builder withDistanceInMeters(@Nullable Double distanceInMeters) {
            this.distanceInMeters = distanceInMeters;
            return this;
        }

        public @Nonnull Trajectory build() {
            return new Trajectory(speedInKms, directionInDegreesRelativeToBatter, launchAngle, distanceInMeters);
        }

    }
}
