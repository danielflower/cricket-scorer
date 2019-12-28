package com.danielflower.crickam.scorer;

public enum DeliveryType {
    LEG_BREAK,
    GOOGLY,
    TOP_SPINNER,
    FLICKER_BALL,
    SLIDER,
    FLIPPER,

    OFF_BREAK,
    DOOSRA,
    TOPSPINNER,
    CARROM_BALL,
    ARM_BALL,
    TEESRA,

    BOUNCER,
    STRAIGHT,
    INSWINGER,
    REVERSE_SWING,
    LEG_CUTTER,
    OFF_CUTTER,
    OUTSWINGER,
    YORKER,
    BEAMER,
    KNUCKLEBALL,
    SLOWER_BALL;

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", " ");
    }
}
