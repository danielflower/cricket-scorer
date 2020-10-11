package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @Nonnull T build(Match match);

    /**
     * @param id A unique ID for this event. If unset, a UUID will be assigned.
     * @return this builder
     */
    @Nonnull B withID(String id);

    /**
     * @param generatedById The ID of the event that auto-generated this event, or null if it was created by the API user.
     * @return this builder
     */
    @Nonnull B withGeneratedBy(@Nullable String generatedById);

    /**
     * @param time The time the event occurred
     * @return This builder
     */
    @Nonnull B withTime(@Nullable Instant time);

    /**
     * @return API-user defined custom data associated with the event
     */
    @Nullable Object customData();

    /**
     * This allows the API user to associate an arbitrary object with the match to make retrieval easier
     * (e.g. from event listeners).
     * <p>Note that matches are immutable objects unless mutable custom data is specified</p>
     * @param customData An arbitrary object
     * @return This builder
     */
    @Nonnull
    B withCustomData(@Nullable Object customData);

    /**
     * @return The ID of the event, or null if unset
     */
    @Nonnull String id();

    /**
     * @return The ID of the event that generated this event, or null if unset or if it wasn't generated
     */
    @Nullable String generatedBy();

    /**
     * @return The time of the event, or null if unset
     */
    @Nullable Instant time();

}
