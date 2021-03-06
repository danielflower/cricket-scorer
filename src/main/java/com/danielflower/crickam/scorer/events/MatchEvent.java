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
     * When undoing an event with {@link MatchControl#undo()} the match will be rolled back to the previous undo point.
     * <p>Undo points are often user generated events, whereas events automatically generated may not be undo points,
     * such that when a user undoes an event, it undoes the last event they initiated.</p>
     * @return <code>true</code> is this is an undo point.
     */
    boolean undoPoint();
}
