package com.danielflower.crickam.scorer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Runs per over
 * <p>These can be created by calling {@link #fromDouble(double)} or from {@link Score#runsPerOver()} or {@link Score#bowlerEconomyRate()}.</p>
 * <p>RPO is just a double value (accessed via {@link #value()} however this class also provides accessors to values
 * such as the first decimal place (as an integer, via {@link #firstDecimal()} and a toString() method that rounds to 1dp.</p>
 */
@Immutable
public final class RPO {

	private final int intValue;
	private final int nearestIntValue;
	private final int firstDecimal;
	private final double doubleValue;

	private RPO(@Nonnegative double doubleValue, @Nonnegative int intValue, @Nonnegative int nearestIntValue, @Nonnegative int firstDecimal) {
		this.intValue = intValue;
		this.nearestIntValue = nearestIntValue;
		this.firstDecimal = firstDecimal;
		this.doubleValue = doubleValue;
	}

    /**
     * Creates a new RPO object
     * @param value Runs per over as a double
     * @return A new RPO
     */
	public static @Nonnull RPO fromDouble(@Nonnegative double value) {
		int intVal = (int) value;
		int dp = (int) Math.floor(10 * (value - intVal));
		int nearest = (dp >= 5) ? intVal + 1 : intVal;
		return new RPO(value, intVal, nearest, dp);
	}

    /**
     * @return The number of runs per over, rounded down to the nearest integer. e.g. a RPO of 4.8 would return 4 from this method
     */
    public @Nonnegative int intValue() {
        return intValue;
    }

    /**
     * @return The number of runs per over, rounded to the nearest integer. e.g. a RPO of 4.8 would return 5 from this method
     */
    public @Nonnegative int nearestIntValue() {
        return nearestIntValue;
    }

    /**
     * @return The first value after the decimal place, e.g. a RPO of 4.8 would return 8 from this method
     */
    public @Nonnegative int firstDecimal() {
        return firstDecimal;
    }

    /**
     * @return The runs per over
     */
    public @Nonnegative double value() {
        return doubleValue;
    }

	@Override
	public boolean equals(@Nullable Object o) {
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
		return intValue + "." + firstDecimal;
	}
}
