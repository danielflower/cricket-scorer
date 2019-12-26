package com.danielflower.crickam.scorer;

import java.util.Objects;
import java.util.Optional;

/**
 * Describes the manner in which the bowler bowled the ball.
 * <p>An instance can be created by getting a builder with {@link #delivery()} and it is associated with a ball
 * via the {@link com.danielflower.crickam.scorer.events.BallCompletedEvent.Builder#withDelivery(Delivery)} method.</p>
 */
public final class Delivery {
    private final DeliveryType deliveryType;
    private final Integer speedInKilometers;
    private final Double positionOfBounce;
    private final Double horizontalPitchInMeters;
    private final Double changeInLineAfterBounceInDegrees;
    private final WicketSide bowledFrom;

    private Delivery(DeliveryType deliveryType, Integer speedInKilometers, Double positionOfBounce, Double horizontalPitchInMeters, Double changeInLineAfterBounceInDegrees, WicketSide bowledFrom) {
        this.deliveryType = deliveryType;
        this.speedInKilometers = speedInKilometers;
        this.positionOfBounce = positionOfBounce;
        this.horizontalPitchInMeters = horizontalPitchInMeters;
        this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
        this.bowledFrom = bowledFrom;
    }

    public Optional<DeliveryType> deliveryType() {
        return Optional.ofNullable(deliveryType);
    }

    public Integer speedInKilometers() {
        return speedInKilometers;
    }

    /**
     * @return The number of meters from the bowling crease where the ball bounced, or null if unknown
     */
    public Double positionOfBounce() {
        return positionOfBounce;
    }

    /**
     * @return If {@link #positionOfBounce()} is known, then specifies a "length" such as {@link DeliveryLength#FULL}
     * that best describes the length of the ball.
     */
    public Optional<DeliveryLength> deliveryLength() {
        if (positionOfBounce == null) return Optional.empty();
        double pos = positionOfBounce;
        if (pos > 20.5) return Optional.of(DeliveryLength.FULL_TOSS);
        if (pos > 19.5) return Optional.of(DeliveryLength.YORKER);
        if (pos > 17.5) return Optional.of(DeliveryLength.FULL);
        if (pos > 14.5) return Optional.of(DeliveryLength.GOOD);
        return Optional.of(DeliveryLength.SHORT);
    }

    /**
     * @return The distance in meters from the horizontal middle of the pitch where the ball pitched, where -1.5 is
     * to the far left (off side for a right hander; leg side for a lefty) and 1.5 is to the far-right. Returns null
     * if unknown.
     */
    public Double horizontalPitchInMeters() {
        return horizontalPitchInMeters;
    }

    public Double changeInLineAfterBounceInDegrees() {
        return changeInLineAfterBounceInDegrees;
    }

    /**
     * @return Whether it was bowled over or around the wicket, or null if unknown
     */
    public Optional<WicketSide> bowledFrom() {
        return Optional.ofNullable(bowledFrom);
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
    public boolean equals(Object o) {
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

        public Builder withDeliveryType(DeliveryType deliveryType) {
            this.deliveryType = deliveryType;
            return this;
        }

        public Builder withSpeedInKilometers(Integer speedInKilometers) {
            this.speedInKilometers = speedInKilometers;
            return this;
        }

        public Builder withPositionOfBounce(Double positionOfBounce) {
            this.positionOfBounce = positionOfBounce;
            return this;
        }

        public Builder withHorizontalPitchInMeters(Double horizontalPitchInMeters) {
            this.horizontalPitchInMeters = horizontalPitchInMeters;
            return this;
        }

        public Builder withChangeInLineAfterBounceInDegrees(Double changeInLineAfterBounceInDegrees) {
            this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
            return this;
        }

        public Builder withBowledFrom(WicketSide bowledFrom) {
            this.bowledFrom = bowledFrom;
            return this;
        }

        public Delivery build() {
            return new Delivery(deliveryType, speedInKilometers, positionOfBounce, horizontalPitchInMeters, changeInLineAfterBounceInDegrees, bowledFrom);
        }

    }
}
