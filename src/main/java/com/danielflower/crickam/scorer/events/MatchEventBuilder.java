package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.MatchControl;

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
     * When undoing an event with {@link MatchControl#undo()} the match will be rolled back to the previous undo point.
     * <p>Undo points are often user generated events, whereas events automatically generated may not be undo points,
     * such that when a user undoes an event, it undoes the last event they initiated.</p>
     * @param undoPoint <code>true</code> if calling {@link MatchControl#undo()} should roll back to this point
     * @return This builder
     */
    B withUndoPoint(boolean undoPoint);

    /**
     * @return The ID of the event
     */
    @Nonnull UUID id();

    /**
     * @return The time of the event, or null if unset
     */
    @Nullable Instant time();

    /**
     * @return <code>true</code> if {@link MatchControl#undo()} stops at this event
     */
    boolean undoPoint();

}
