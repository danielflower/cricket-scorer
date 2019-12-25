package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.stats.Distribution;
import com.danielflower.crickam.scorer.stats.Range;
import com.danielflower.crickam.scorer.stats.SimpleRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * Describes the manner in which a player generally bowls balls.
 * <p>Create a builder for a style with the {@link #bowlingStyle()} method or one of the predefined builders
 * such as {@link Builder#fast()} etc.</p>
 */
@ApiStatus.AvailableSince("1.0.0")
public final class BowlingStyle {

	private final Handedness bowlingHandedness;
	private final Distribution<DeliveryType> deliveryTypes;
	private final SimpleRange bowlingSpeed;

	BowlingStyle(@NotNull Handedness bowlingHandedness, @Nullable Distribution<DeliveryType> deliveryTypes,
                        @Nullable SimpleRange bowlingSpeed) {
        this.bowlingHandedness = requireNonNull(bowlingHandedness, "bowlingHandedness");
		this.deliveryTypes = deliveryTypes;
		this.bowlingSpeed = bowlingSpeed;
	}

    /**
     * @return The hand predominantly used to bowl with
     */
    public Handedness bowlingHandedness() {
        return bowlingHandedness;
    }

    /**
     * @return The types of balls this bowler bowls, weighted by the likelihood of bowling that ball.
     */
    public Optional<Distribution<DeliveryType>> deliveryTypes() {
        return Optional.ofNullable(deliveryTypes);
    }

    /**
     * @return The range of speeds that this bowler can bowl at
     */
    public Optional<SimpleRange> bowlingSpeed() {
        return Optional.ofNullable(bowlingSpeed);
    }

    /**
     * @return A new builder
     */
    public static Builder bowlingStyle() {
        return new Builder();
    }

    public static final class Builder {
        private Handedness bowlingHandedness = Handedness.RightHanded;
        private Distribution<DeliveryType> deliveryTypes;
        private SimpleRange bowlingSpeed;

        public Builder withHandedness(Handedness bowlingHandedness) {
            this.bowlingHandedness = bowlingHandedness;
            return this;
        }

        public Builder withDeliveryTypes(Distribution<DeliveryType> deliveryTypes) {
            this.deliveryTypes = deliveryTypes;
            return this;
        }

        public Builder withBowlingSpeed(SimpleRange bowlingSpeed) {
            this.bowlingSpeed = bowlingSpeed;
            return this;
        }

        public BowlingStyle build() {
            return new BowlingStyle(bowlingHandedness, deliveryTypes, bowlingSpeed);
        }

        public static Builder medium() {
            return bowlingStyle().withDeliveryTypes(
                new Distribution<>(asList(DeliveryType.STRAIGHT))
            ).withBowlingSpeed(Range.between(96, 119));
        }

        public static Builder slow() {
            return bowlingStyle().withDeliveryTypes(
                new Distribution<>(asList(DeliveryType.STRAIGHT))
            ).withBowlingSpeed(Range.between(90, 110));
        }

        public static Builder mediumFast() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
                ))
            ).withBowlingSpeed(Range.between(120, 129));
        }

        public static Builder fastMedium() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
                ))
            ).withBowlingSpeed(Range.between(130, 141));
        }

        public static Builder fast() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.STRAIGHT, DeliveryType.INSWINGER, DeliveryType.OUTSWINGER
                ))
            ).withBowlingSpeed(Range.between(142, 152));
        }

        public static Builder legSpinner() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.ARM_BALL, DeliveryType.FLIPPER, DeliveryType.GOOGLY, DeliveryType.LEG_BREAK
                ))
            ).withBowlingSpeed(Range.between(80, 100))
                .withHandedness(Handedness.RightHanded);
        }

        public static Builder offSpinner() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.ARM_BALL, DeliveryType.OFF_BREAK, DeliveryType.DOOSRA
                ))
            ).withBowlingSpeed(Range.between(80, 100))
                .withHandedness(Handedness.RightHanded);
        }

        public static Builder leftArmOrthodox() {
            return bowlingStyle()
                .withDeliveryTypes(new Distribution<>(asList(
                    DeliveryType.ARM_BALL, DeliveryType.OFF_BREAK, DeliveryType.TOPSPINNER
                    ))
                )
                .withBowlingSpeed(Range.between(80, 100))
                .withHandedness(Handedness.LeftHanded);
        }

        public static Builder chinaman() {
            return bowlingStyle().withDeliveryTypes(new Distribution<>(asList(
                DeliveryType.ARM_BALL, DeliveryType.FLIPPER, DeliveryType.GOOGLY, DeliveryType.LEG_BREAK
                ))
            ).withBowlingSpeed(Range.between(80, 100))
                .withHandedness(Handedness.LeftHanded);
        }
    }
}
