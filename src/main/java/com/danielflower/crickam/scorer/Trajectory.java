package com.danielflower.crickam.scorer;

public final class Trajectory {
	private final Integer speedInKms;
	private final Double directionInDegreesRelativeToBatter;
	private final Double elevationInDegrees;
	private final Double distance;

	public Trajectory(Integer speedInKms, Double directionInDegreesRelativeToBatter, Double elevationInDegrees, Double distance) {
		this.speedInKms = speedInKms;
		this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
		this.elevationInDegrees = elevationInDegrees;
		this.distance = distance;
	}


	/**
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
	public Double getDirectionInDegreesRelativeToBatter() {
		return directionInDegreesRelativeToBatter;
	}

	public Double getElevationInDegrees() {
		return elevationInDegrees;
	}

	@Override
	public String toString() {
		return "Trajectory{" +
				"speedInKms=" + speedInKms +
				", directionInDegreesRelativeToBatter=" + directionInDegreesRelativeToBatter +
				", elevationInDegrees=" + elevationInDegrees +
				", distance=" + distance +
				'}';
	}
}
