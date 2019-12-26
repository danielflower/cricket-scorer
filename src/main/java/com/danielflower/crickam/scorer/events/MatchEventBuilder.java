package com.danielflower.crickam.scorer.events;

/**
 * A builder for a match event
 * @param <T> The type of event
 */
public interface MatchEventBuilder<T extends MatchEvent> {

    /**
     * Builds the event
     * @return A newly constructed T
     * @throws NullPointerException thrown when a required field is not set
     * @throws IllegalArgumentException thrown when a specified value is invalid
     */
    T build();

}
