package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.MatchControl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;

import static com.danielflower.crickam.scorer.ImmutableList.emptyList;

/**
 * A match event that the API user feeds into the {@link MatchControl#onEvent(MatchEventBuilder)} to build up a match.
 * <p>Match events are created by builders. Builders can be created by calling the static methods on the {@link MatchEvents} class.</p>
 */
@Immutable
public interface MatchEvent {

    /**
     * @return A unique ID for this event
     */
    @Nonnull String id();

    /**
     * @return The time that the event took place, or null if unknown
     */
    @Nullable Instant time();

    /**
     * @return The ID of the event that auto-generated this event, or null if the API user created the event
     */
    @Nullable String generatedBy();

    /**
     * @return A new builder based on the current values of the event
     */
    @Nonnull MatchEventBuilder<?,?> newBuilder();

    /**
     * Certain events generate more events, for example a {@link InningsStartingEvent} causes a {@link BatterInningsStartingEvent}
     * for each of the openers, and a dismissal from a {@link BallCompletedEvent} causes a {@link BatterInningsCompletedEvent}
     * to be raised.
     * <p>These events are applied automatically when using the {@link MatchControl} class</p>
     * @return The list of events generated as a result of applying the current event, or an empty list if none were generated
     */
    default ImmutableList<MatchEventBuilder<?,?>> generatedEvents() {
        return emptyList();
    }

    /**
     * @return data associated with the event by the API user
     */
    @Nullable Object customData();
}
