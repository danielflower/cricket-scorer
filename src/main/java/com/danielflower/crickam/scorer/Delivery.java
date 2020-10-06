package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Describes the manner in which the bowler bowled the ball.
 * <p>An instance can be created by getting a builder with {@link #delivery()} and it is associated with a ball
 * via the {@link com.danielflower.crickam.scorer.events.BallCompletedEvent.Builder#withDelivery(Delivery)} method.</p>
 */
@Immutable
public final class Delivery {
    private final DeliveryType deliveryType;
    private final Integer speedInKilometers;
    private final Double positionOfBounce;
    private final Double horizontalPitchInMeters;
    private final Double changeInLineAfterBounceInDegrees;
    private final WicketSide bowledFrom;

    private Delivery(@Nullable DeliveryType deliveryType, @Nullable Integer speedInKilometers, @Nullable Double positionOfBounce, @Nullable Double horizontalPitchInMeters, @Nullable Double changeInLineAfterBounceInDegrees, @Nullable WicketSide bowledFrom) {
        if (horizontalPitchInMeters != null && (horizontalPitchInMeters < -1.5 || horizontalPitchInMeters > 1.5)) {
            throw new IllegalArgumentException("horizontalPitchInMeters must be between -1.5 and 1.5, or null, but was " + horizontalPitchInMeters);
        }
        if (positionOfBounce != null && (positionOfBounce < 0 || positionOfBounce > 40)) {
            throw new IllegalArgumentException("positionOfBounce must be between 0 and 40, or null, but was " + positionOfBounce);
        }
        if (speedInKilometers != null && (speedInKilometers < 1 || speedInKilometers > 200)) {
            throw new IllegalArgumentException("speedInKilometers must be between 1 and 200, or null, but was " + speedInKilometers);
        }
        if (changeInLineAfterBounceInDegrees != null && (changeInLineAfterBounceInDegrees < -90 || changeInLineAfterBounceInDegrees > 90)) {
            throw new IllegalArgumentException("changeInLineAfterBounceInDegrees must be between -90 and 90, or null, but was " + changeInLineAfterBounceInDegrees);
        }
        this.deliveryType = deliveryType;
        this.speedInKilometers = speedInKilometers;
        this.positionOfBounce = positionOfBounce;
        this.horizontalPitchInMeters = horizontalPitchInMeters;
        this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
        this.bowledFrom = bowledFrom;
    }

    public @Nullable DeliveryType deliveryType() {
        return deliveryType;
    }

    public @Nullable Integer speedInKilometers() {
        return speedInKilometers;
    }

    /**
     * @return The number of meters from the bowling crease where the ball bounced, or null if unknown
     */
    public @Nullable Double positionOfBounce() {
        return positionOfBounce;
    }

    /**
     * @return If {@link #positionOfBounce()} is known, then specifies a "length" such as {@link DeliveryLength#FULL}
     * that best describes the length of the ball.
     */
    public @Nullable DeliveryLength deliveryLength() {
        if (positionOfBounce == null) return null;
        double pos = positionOfBounce;
        if (pos > 20.5) return DeliveryLength.FULL_TOSS;
        if (pos > 19.5) return DeliveryLength.YORKER;
        if (pos > 17.5) return DeliveryLength.FULL;
        if (pos > 14.5) return DeliveryLength.GOOD;
        return DeliveryLength.SHORT;
    }

    /**
     * @return The distance in meters from the horizontal middle of the pitch where the ball pitched, where -1.5 is
     * to the far left (off side for a right hander; leg side for a lefty) and 1.5 is to the far-right. Returns null
     * if unknown.
     */
    public @Nullable Double horizontalPitchInMeters() {
        return horizontalPitchInMeters;
    }

    /**
     * @return 0 to indicate the ball when straight on; -90 if it made a total left turn; +90 for a total right turn. Null if unknown.
     */
    public @Nullable Double changeInLineAfterBounceInDegrees() {
        return changeInLineAfterBounceInDegrees;
    }

    /**
     * @return Whether it was bowled over or around the wicket, or null if unknown
     */
    public @Nullable WicketSide bowledFrom() {
        return bowledFrom;
    }

    @Override
    public String toString() {
        return "Delivery{" +
            "deliveryType=" + deliveryType +
            ", speedInKilometers=" + speedInKilometers +
            ", positionOfBounce=" + positionOfBounce +
            ", horizontalPitchInMeters=" + horizontalPitchInMeters +
            ", changeInLineAfterBounceInDegrees=" + changeInLineAfterBounceInDegrees +
            ", bowledFrom=" + bowledFrom +
            '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return deliveryType == delivery.deliveryType &&
            Objects.equals(speedInKilometers, delivery.speedInKilometers) &&
            Objects.equals(positionOfBounce, delivery.positionOfBounce) &&
            Objects.equals(horizontalPitchInMeters, delivery.horizontalPitchInMeters) &&
            Objects.equals(changeInLineAfterBounceInDegrees, delivery.changeInLineAfterBounceInDegrees) &&
            bowledFrom == delivery.bowledFrom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryType, speedInKilometers, positionOfBounce, horizontalPitchInMeters, changeInLineAfterBounceInDegrees, bowledFrom);
    }

    /**
     * @return A new builder
     */
    public static Builder delivery() {
        return new Builder();
    }

    public static final class Builder {
        private DeliveryType deliveryType;
        private Integer speedInKilometers;
        private Double positionOfBounce;
        private Double horizontalPitchInMeters;
        private Double changeInLineAfterBounceInDegrees;
        private WicketSide bowledFrom;

        public @Nonnull Builder withDeliveryType(@Nullable DeliveryType deliveryType) {
            this.deliveryType = deliveryType;
            return this;
        }

        /**
         * @param speedInKilometers The speed the ball was bowled at, or null if unknown
         * @return This builder
         */
        public @Nonnull Builder withSpeedInKilometers(@Nullable Integer speedInKilometers) {
            this.speedInKilometers = speedInKilometers;
            return this;
        }

        /**
         * @param positionOfBounce The number of meters from the bowling crease where the ball bounced, or null if unknown
         * @return This builder
         */
        public @Nonnull Builder withPositionOfBounce(@Nullable Double positionOfBounce) {
            this.positionOfBounce = positionOfBounce;
            return this;
        }

        /**
         * @param horizontalPitchInMeters The distance in meters from the horizontal middle of the pitch where the ball pitched, where -1.5 is
         *                                to the far left (off side for a right hander; leg side for a lefty) and 1.5 is to the far-right. Use null if unknown.
         * @return This builder
         */
        public @Nonnull Builder withHorizontalPitchInMeters(@Nullable Double horizontalPitchInMeters) {
            this.horizontalPitchInMeters = horizontalPitchInMeters;
            return this;
        }

        /**
         * @param changeInLineAfterBounceInDegrees 0 to indicate the ball when straight on; -90 if it made a total left turn; +90 for a total right turn.
         * @return This builder
         */
        public @Nonnull Builder withChangeInLineAfterBounceInDegrees(@Nullable Double changeInLineAfterBounceInDegrees) {
            this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
            return this;
        }

        public @Nonnull Builder withBowledFrom(@Nullable WicketSide bowledFrom) {
            this.bowledFrom = bowledFrom;
            return this;
        }

        public @Nonnull Delivery build() {
            return new Delivery(deliveryType, speedInKilometers, positionOfBounce, horizontalPitchInMeters, changeInLineAfterBounceInDegrees, bowledFrom);
        }

    }
}
