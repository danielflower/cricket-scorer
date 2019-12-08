package com.danielflower.crickam.scorer;

public enum DeliveryType {
    LEG_BREAK,
    GOOGLY,
    FLIPPER,
    SLIDER,
    FLICKER_BALL,
    OFF_BREAK,
    DOOSRA,
    ARM_BALL,
    TOPSPINNER,
    CARROM_BALL,
    TEESRA,
    STRAIGHT,
    INSWINGER,
    REVERSE_SWING,
    LEG_CUTTER,
    OFF_CUTTER,
    OUTSWINGER;

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", " ");
    }
}
