package com.danielflower.crickam.stats;

public final class Chance {

	public static boolean oneIn(int n) {
		return Math.random() < (1.0 / (double)n);
	}

	public static boolean inverseOneIn(int n) {
		return !oneIn(n);
	}
}
