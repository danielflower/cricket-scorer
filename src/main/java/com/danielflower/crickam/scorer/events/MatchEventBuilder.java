package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;

/**
 * A builder for a match event
 * @param <B> The type of the builder
 * @param <T> The type of event
 */
public interface MatchEventBuilder<B extends MatchEventBuilder<B,T>, T extends MatchEvent> {

    /**
     * Builds the event
     * @param match The state of the current match which may be used to fill in certain values
     * @return A newly constructed T
     * @throws NullPointerException thrown when a required field is not set
     * @throws IllegalArgumentException thrown when a specified value is invalid
     */
    T build(Match match);

    /**
     * @param id A unique ID for this event. If unset, a UUID will be assigned.
     * @return this builder
     */
    B withID(String id);

    /**
     * @param generatedById The ID of the event that auto-generated this event, or null if it was created by the API user.
     * @return this builder
     */
    B withGeneratedBy(String generatedById);

    /**
     * @param time The time the event occured
     * @return This builder
     */
    B withTime(Instant time);

    /**
     * @return The ID of the event, or null if unset
     */
    String id();

    /**
     * @return The ID of the event that generated this event, or null if unset or if it wasn't generated
     */
    String generatedBy();

    /**
     * @return The time of the event, or null if unset
     */
    Instant time();

}
