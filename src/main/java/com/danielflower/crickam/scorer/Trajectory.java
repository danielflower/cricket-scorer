package com.danielflower.crickam.scorer;

import java.util.Optional;

/**
 * The trajectory of the ball coming off the bat
 */
public final class Trajectory {
	private final Integer speedInKms;
	private final Double directionInDegreesRelativeToBatter;
	private final Double elevationInDegrees;
	private final Double distanceInMeters;

	private Trajectory(Integer speedInKms, Double directionInDegreesRelativeToBatter, Double elevationInDegrees, Double distanceInMeters) {
		this.speedInKms = speedInKms;
		this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
		this.elevationInDegrees = elevationInDegrees;
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
	 */
	public Optional<Double> directionInDegreesRelativeToBatter() {
		return Optional.ofNullable(directionInDegreesRelativeToBatter);
	}

	public Optional<Double> elevationInDegrees() {
		return Optional.ofNullable(elevationInDegrees);
	}

    public Optional<Integer> speedInKms() {
        return Optional.ofNullable(speedInKms);
    }

    public Optional<Double> distanceInMeters() {
        return Optional.ofNullable(distanceInMeters);
    }

    @Override
	public String toString() {
		return "Trajectory{" +
				"speedInKms=" + speedInKms +
				", directionInDegreesRelativeToBatter=" + directionInDegreesRelativeToBatter +
				", elevationInDegrees=" + elevationInDegrees +
				", distance=" + distanceInMeters +
				'}';
	}

	public static Builder trajectory() {
	    return new Builder();
    }

    public static final class Builder {
        private Integer speedInKms;
        private Double directionInDegreesRelativeToBatter;
        private Double elevationInDegrees;
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
         */
        public Builder withDirectionInDegreesRelativeToBatter(Double directionInDegreesRelativeToBatter) {
            this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
            return this;
        }

        public Builder withElevationInDegrees(Double elevationInDegrees) {
            this.elevationInDegrees = elevationInDegrees;
            return this;
        }

        public Builder withDistanceInMeters(Double distanceInMeters) {
            this.distanceInMeters = distanceInMeters;
            return this;
        }

        public Trajectory build() {
            return new Trajectory(speedInKms, directionInDegreesRelativeToBatter, elevationInDegrees, distanceInMeters);
        }

    }
}
