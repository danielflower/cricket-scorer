package com.danielflower.crickam.scorer;

public final class RPO {

	public final int intValue;
	public final int nearestIntValue;
	public final int firstDecimalPlace;
	public final double doubleValue;

	private RPO(double doubleValue, int intValue, int nearestIntValue, int firstDecimalPlace) {
		this.intValue = intValue;
		this.nearestIntValue = nearestIntValue;
		this.firstDecimalPlace = firstDecimalPlace;
		this.doubleValue = doubleValue;
	}

	public static RPO fromDouble(double value) {
		int intVal = (int) value;
		int dp = (int) Math.round(10 * (value - intVal));
		int nearest = (dp >= 5) ? intVal + 1 : intVal;
		return new RPO(value, intVal, nearest, dp);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RPO rpo = (RPO) o;
		return Double.compare(rpo.doubleValue, doubleValue) == 0;

	}

	@Override
	public int hashCode() {
		return Double.hashCode(doubleValue);
	}

	@Override
	public String toString() {
		return intValue + "." + firstDecimalPlace;
	}
}
