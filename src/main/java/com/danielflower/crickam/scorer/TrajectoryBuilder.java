package com.danielflower.crickam.scorer;

public class TrajectoryBuilder {
	private Integer speedInKms;
	private Double directionInDegreesRelativeToBatter;
	private Double elevationInDegrees;
	private Double distance;

	public TrajectoryBuilder setSpeedInKms(Integer speedInKms) {
		this.speedInKms = speedInKms;
		return this;
	}

	public TrajectoryBuilder setDirectionInDegreesRelativeToBatter(Double directionInDegreesRelativeToBatter) {
		this.directionInDegreesRelativeToBatter = directionInDegreesRelativeToBatter;
		return this;
	}

	public TrajectoryBuilder setElevationInDegrees(Double elevationInDegrees) {
		this.elevationInDegrees = elevationInDegrees;
		return this;
	}

	public TrajectoryBuilder setDistance(Double distance) {
		this.distance = distance;
		return this;
	}

	public Trajectory build() {
		return new Trajectory(speedInKms, directionInDegreesRelativeToBatter, elevationInDegrees, distance);
	}

	public static TrajectoryBuilder aTrajectory() {
		return new TrajectoryBuilder();
	}
}