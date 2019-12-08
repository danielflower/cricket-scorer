package com.danielflower.crickam.scorer;


import com.danielflower.crickam.stats.Distribution;
import com.danielflower.crickam.stats.Range;
import com.danielflower.crickam.stats.SimpleRange;

import static java.util.Arrays.asList;

public class BowlingStyleBuilder {
	private Handedness bowlingHandedness;
	private Distribution<DeliveryType> deliveryTypes;
	private SimpleRange bowlingSpeed;

	public BowlingStyleBuilder setHandedness(Handedness bowlingHandedness) {
		this.bowlingHandedness = bowlingHandedness;
		return this;
	}

	public BowlingStyleBuilder setDeliveryTypes(Distribution<DeliveryType> deliveryTypes) {
		this.deliveryTypes = deliveryTypes;
		return this;
	}

	public BowlingStyleBuilder setBowlingSpeed(SimpleRange bowlingSpeed) {
		this.bowlingSpeed = bowlingSpeed;
		return this;
	}

	public BowlingStyle build() {
		return new BowlingStyle(bowlingHandedness, deliveryTypes, bowlingSpeed);
	}

	public static BowlingStyleBuilder aBowlingStyle() {
		return new BowlingStyleBuilder();
	}

	public static BowlingStyleBuilder medium() {
		return aBowlingStyle().setDeliveryTypes(
				new Distribution<>(asList(DeliveryType.STRAIGHT))
		).setBowlingSpeed(Range.between(96, 119));
	}
	public static BowlingStyleBuilder slow() {
		return aBowlingStyle().setDeliveryTypes(
				new Distribution<>(asList(DeliveryType.STRAIGHT))
		).setBowlingSpeed(Range.between(90, 110));
	}

	public static BowlingStyleBuilder mediumFast() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
						DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
				))
		).setBowlingSpeed(Range.between(120, 129));
	}

	public static BowlingStyleBuilder fastMedium() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
						DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
				))
		).setBowlingSpeed(Range.between(130, 141));
	}

	public static BowlingStyleBuilder fast() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
						DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
				))
		).setBowlingSpeed(Range.between(142, 152));
	}

	public static BowlingStyleBuilder legSpinner() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
				DeliveryType.ARM_BALL, DeliveryType.FLIPPER, DeliveryType.GOOGLY, DeliveryType.LEG_BREAK
		))
		).setBowlingSpeed(Range.between(142, 152))
				.setHandedness(Handedness.RightHanded);
	}

	public static BowlingStyleBuilder offSpinner() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
				DeliveryType.ARM_BALL, DeliveryType.OFF_BREAK, DeliveryType.DOOSRA
		))
		).setBowlingSpeed(Range.between(142, 152))
				.setHandedness(Handedness.RightHanded);
	}

	public static BowlingStyleBuilder leftArmOrthodox() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
				DeliveryType.ARM_BALL, DeliveryType.OFF_BREAK, DeliveryType.TOPSPINNER
		))
		).setBowlingSpeed(Range.between(142, 152))
				.setHandedness(Handedness.LeftHanded);
	}

	public static BowlingStyleBuilder chinaman() {
		return aBowlingStyle().setDeliveryTypes(new Distribution<>(asList(
				DeliveryType.ARM_BALL, DeliveryType.FLIPPER, DeliveryType.GOOGLY, DeliveryType.LEG_BREAK
		))
		).setBowlingSpeed(Range.between(142, 152))
				.setHandedness(Handedness.LeftHanded);
	}
}