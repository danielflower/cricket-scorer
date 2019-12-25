package com.danielflower.crickam.scorer;


public enum ShotType {
    BLOCK(Category.VERTICAL_BAT, "block"),
    COVER_DRIVE(Category.VERTICAL_BAT, "cover drive"),
    STRAIGHT_DRIVE(Category.VERTICAL_BAT, "straight drive"),
    SQUARE_DRIVE(Category.HORIZONTAL_BAT, "square drive"),
    ON_DRIVE(Category.VERTICAL_BAT, "on drive"),
    OFF_DRIVE(Category.VERTICAL_BAT, "off drive"),
    LEG_GLANCE(Category.VERTICAL_BAT, "leg glance"),
    CUT(Category.HORIZONTAL_BAT, "cut shot"),
    LATE_CUT(Category.HORIZONTAL_BAT, "late cut"),
    PULL(Category.HORIZONTAL_BAT, "pull shot"),
    HOOK(Category.HORIZONTAL_BAT, "hook shot"),
    SWEEP(Category.HORIZONTAL_BAT, "sweep shot"),
    REVERSE_SWEEP(Category.UNORTHODOX, "reverse sweep"),
    PADDLE_SWEEP(Category.UNORTHODOX, "paddle sweep"),
    SLOG(Category.UNORTHODOX, "slog"),
    SLOG_SWEEP(Category.UNORTHODOX, "slog sweep"),
    UPPER_CUT(Category.UNORTHODOX, "upper cut"),
    SWITCH_HIT(Category.UNORTHODOX, "switch hit"),
    RAMP(Category.UNORTHODOX, "ramp shot"),
    LEAVE(Category.LEAVE, "leave");

    private final Category category;
    private final String name;

    ShotType(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    /**
     * @return The name of the shot in english, for example &quot;cover driver&quot;
     */
    public String displayName() {
        return name;
    }

    /**
     * @return The category of shot, for example if it was hit with a vertical bat, or is a leave, etc
     */
    public Category category() {
        return category;
    }

    public enum Category {
        LEAVE, VERTICAL_BAT, HORIZONTAL_BAT, UNORTHODOX
    }

    @Override
    public String toString() {
        return name;
    }


}
