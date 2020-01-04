package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.OptionalInt;
import java.util.TimeZone;
import java.util.function.Supplier;

public final class Crictils {

    public static OptionalInt toOptional(Integer value) {
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    public static Integer toInteger(OptionalInt value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return value.getAsInt();
        }
    }

    public static void stateGuard(boolean assertion, Supplier<String> message) {
        if (!assertion) {
            throw new IllegalStateException(message.get());
        }
    }

    /**
     * Gets the local time at a time zone as an {@code Instant}
     * @param timeZone The time zone
     * @param year The year
     * @param month The month
     * @param day The day
     * @param hour The hour
     * @param minute The minute
     * @return The instant of the given time at the given time zone
     */
    public static Instant localTime(TimeZone timeZone, int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute)
            .atZone(timeZone.toZoneId())
            .toInstant();
    }

    /**
     * For an input of &quot;1&quot; this returns the string &quot;1st&quot;
     * @param number The number
     * @return The given number with its suffix
     */
    public static String withOrdinal(int number) {
        int last = number % 10;
        String suffix = (last == 1) ? "st"
            : (last == 2) ? "nd"
            : (last == 3) ? "rd"
            : "th";
        return number + suffix;
    }

    public static String pluralize(int num, String noun) {
        return pluralize(num, noun, noun + "s");
    }
    public static String pluralize(int num, String singular, String plural) {
        return num + " " + (num == 1 ? singular : plural);
    }

    public static Integer requireInRange(String name, Integer value, int min, int max) {
        return value == null ? null : requireInRange(name, value.intValue(), min, max);
    }

    public static int requireInRange(String name, int value, int min) {
        return requireInRange(name, value, min, Integer.MAX_VALUE);
    }
    public static int requireInRange(String name, int value, int min, int max) {
        if (value < min) {
            throw new IllegalArgumentException("The min value allowed for " + name + " is " + min + " but it was " + value);
        } else if (value > max) {
            throw new IllegalArgumentException("The max value allowed for " + name + " is " + max + " but it was " + value);
        }
        return value;
    }

}
