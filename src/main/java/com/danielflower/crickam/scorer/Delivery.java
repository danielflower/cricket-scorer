package com.danielflower.crickam.scorer;

import java.util.Optional;

public class Delivery {
    private final Optional<DeliveryType> deliveryType;
    private final Integer speedInKilometers;
    private final Double positionOfBounce;
    private final Double horizontalPitchInMeters;
    private final Double changeInLineAfterBounceInDegrees;
    private final Optional<WicketSide> bowledFrom;

    public Delivery(Optional<DeliveryType> deliveryType, Integer speedInKilometers, Double positionOfBounce, Double horizontalPitchInMeters, Double changeInLineAfterBounceInDegrees, Optional<WicketSide> bowledFrom) {
        this.deliveryType = deliveryType;
        this.speedInKilometers = speedInKilometers;
        this.positionOfBounce = positionOfBounce;
        this.horizontalPitchInMeters = horizontalPitchInMeters;
        this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
        this.bowledFrom = bowledFrom;
    }

    public Optional<DeliveryType> getDeliveryType() {
        return deliveryType;
    }

    public Integer getSpeedInKilometers() {
        return speedInKilometers;
    }

    /**
     * The number of meters from the bowling crease where the ball bounced
     */
    public Double getPositionOfBounce() {
        return positionOfBounce;
    }

    public Optional<DeliveryLength> length() {
        if (positionOfBounce == null) return Optional.empty();
        double pos = positionOfBounce;
        if (pos > 20.5) return Optional.of(DeliveryLength.FULL_TOSS);
        if (pos > 19.5) return Optional.of(DeliveryLength.YORKER);
        if (pos > 17.5) return Optional.of(DeliveryLength.FULL);
        if (pos > 14.5) return Optional.of(DeliveryLength.GOOD);
        return Optional.of(DeliveryLength.SHORT);
    }

    /**
     * The distance in meters from the horizontal middle of the pitch where the ball pitched, where -1.5 is
     * to the far left (off side for a right hander; leg side for a lefty) and 1.5 is to the far-right.
     */
    public Double getHorizontalPitchInMeters() {
        return horizontalPitchInMeters;
    }

    public Double getChangeInLineAfterBounceInDegrees() {
        return changeInLineAfterBounceInDegrees;
    }

    public Optional<WicketSide> getBowledFrom() {
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
}
