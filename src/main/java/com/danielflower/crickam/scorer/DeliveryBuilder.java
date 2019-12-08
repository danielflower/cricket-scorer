package com.danielflower.crickam.scorer;

import java.util.Optional;

public class DeliveryBuilder {
    private Optional<DeliveryType> deliveryType = Optional.empty();
    private Integer speedInKilometers;
    private Double positionOfBounce;
    private Double horizontalPitchInMeters;
    private Double changeInLineAfterBounceInDegrees;
    private Optional<WicketSide> bowledFrom = Optional.empty();

    public DeliveryBuilder setDeliveryType(Optional<DeliveryType> deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public DeliveryBuilder setSpeedInKilometers(Integer speedInKilometers) {
        this.speedInKilometers = speedInKilometers;
        return this;
    }

    public DeliveryBuilder setPositionOfBounce(Double positionOfBounce) {
        this.positionOfBounce = positionOfBounce;
        return this;
    }

    public DeliveryBuilder setHorizontalPitchInMeters(Double horizontalPitchInMeters) {
        this.horizontalPitchInMeters = horizontalPitchInMeters;
        return this;
    }

    public DeliveryBuilder setChangeInLineAfterBounceInDegrees(Double changeInLineAfterBounceInDegrees) {
        this.changeInLineAfterBounceInDegrees = changeInLineAfterBounceInDegrees;
        return this;
    }

    public DeliveryBuilder setBowledFrom(Optional<WicketSide> bowledFrom) {
        this.bowledFrom = bowledFrom;
        return this;
    }

    public Delivery build() {
        return new Delivery(deliveryType, speedInKilometers, positionOfBounce, horizontalPitchInMeters, changeInLineAfterBounceInDegrees, bowledFrom);
    }

    public static DeliveryBuilder aDelivery() {
        return new DeliveryBuilder();
    }
}
