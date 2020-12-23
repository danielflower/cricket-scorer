package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

/**
 * A builder for a match event
 * @param <B> The type of the builder
 * @param <T> The type of event
 */
public interface MatchEventBuilder<B extends MatchEventBuilder<B,T>, T extends MatchEvent> {

    /**
     * Sets values in the builder based on the state of the match.
     * <p>If values are set before calling this, the builder should not override those.</p>
     * @param match The current match state which can be used to derive values for the builder
     * @return this builder
     */
    default @Nonnull B apply(@Nonnull Match match) { return (B)this; }

    /**
     * Builds the event
     * @return A newly constructed T
     * @throws NullPointerException thrown when a required field is not set
     * @throws IllegalArgumentException thrown when a specified value is invalid
     */
    @Nonnull T build();

    /**
     * @param id A unique ID for this event. If unset, a random UUID will be assigned.
     * @return this builder
     */
    @Nonnull B withID(UUID id);

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
     * A transaction ID, used when multiple events are to be added (and perhaps later undo) together as a transaction.
     * The main use-case is to be able to use a single undo operation to undo multiple events that were added together.
     * @param transactionID A transaction ID that links multiple events together
     * @return This builder
     */
    B withTransactionID(@Nullable UUID transactionID);

    /**
     * @return The ID of the event
     */
    @Nonnull UUID id();

    /**
     * @return The time of the event, or null if unset
     */
    @Nullable Instant time();

    /**
     * @return An optional transaction ID that can link this event to others
     */
    @Nullable UUID transactionID();

}
