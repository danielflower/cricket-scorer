package com.danielflower.crickam.scorer;


import com.danielflower.crickam.stats.Distribution;
import com.danielflower.crickam.stats.SimpleRange;

public class BowlingStyle {
	public final Handedness bowlingHandedness;
	public final Distribution<DeliveryType> deliveryTypes;
	public final SimpleRange bowlingSpeed;

	public BowlingStyle(Handedness bowlingHandedness, Distribution<DeliveryType> deliveryTypes, SimpleRange bowlingSpeed) {
		this.bowlingHandedness = bowlingHandedness;
		this.deliveryTypes = deliveryTypes;
		this.bowlingSpeed = bowlingSpeed;
	}

}
