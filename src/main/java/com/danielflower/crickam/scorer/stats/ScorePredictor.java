package com.danielflower.crickam.scorer.stats;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class ScorePredictor {

	private final HashMap<String, Double> memory = new HashMap<>();

	public double predictRemainingRunsToBeScored(int ballsRemaining, int wicketsRemaining, List<Distribution<Boolean>> wicketChance, List<Distribution<Integer>> runChance) {

		// V(b,w) =r(b,w) +p(b,w) V(b+1,w+1) +(1-p(b,w)))V(b+1,w)

		if (runChance.size() < 2 || wicketChance.size() < 2) {
			return 0.0;
		}
		double rbw = Distribution.combine(runChance.get(0), runChance.get(1)).expectedValue();
		double pbw = Distribution.combine(wicketChance.get(0), wicketChance.get(1)).expectedValue();
		String key = (rbw + ":" + pbw + ":" + ballsRemaining + ":" + wicketsRemaining).intern();
		Double cached = memory.get(key);
		if (cached == null) {
			double amount = 0.0;
			for (int i = 1; i <= ballsRemaining; i++) {

				if (i == 0 || wicketsRemaining == 0) {
					amount = 0.0;
				} else {
					List<Distribution<Boolean>> ifWicketLostWicketsChance = wicketChance.stream().skip(1).collect(toList());
					List<Distribution<Integer>> ifWicketLostRunsChance = runChance.stream().skip(1).collect(toList());
					double estimateFromNextBallIfWicketTakenNow = predictRemainingRunsToBeScored(i - 1, wicketsRemaining - 1, ifWicketLostWicketsChance, ifWicketLostRunsChance);
					double estimateFromNextBallIfWicketNotTakenNow = amount;
					amount = rbw + (pbw * estimateFromNextBallIfWicketTakenNow) + ((1.0 - pbw) * estimateFromNextBallIfWicketNotTakenNow);
				}
			}
			memory.put(key, amount);
			return amount;
		} else {
			return cached;
		}
	}

}
