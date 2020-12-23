package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchControl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.UUID;

/**
 * A match event that the API user feeds into the {@link MatchControl#onEvent(MatchEventBuilder)} to build up a match.
 * <p>Match events are created by builders. Builders can be created by calling the static methods on the {@link MatchEvents} class.</p>
 */
@Immutable
public interface MatchEvent {

    /**
     * @return A unique ID for this event
     */
    @Nonnull UUID id();

    /**
     * @return The time that the event took place, or null if unknown
     */
    @Nullable Instant time();

    /**
     * @return A new builder based on the current values of the event
     */
    @Nonnull MatchEventBuilder<?,?> newBuilder();

    /**
     * @return data associated with the event by the API user
     */
    @Nullable Object customData();

    /**
     * @return An optional ID that can tie multiple events together. The main use is where you want to undo
     * events as a transaction.
     */
    @Nullable UUID transactionID();
}
