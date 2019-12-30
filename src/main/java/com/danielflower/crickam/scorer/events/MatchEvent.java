package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;
import com.danielflower.crickam.scorer.MatchControl;

import java.time.Instant;
import java.util.Optional;

import static com.danielflower.crickam.scorer.ImmutableList.emptyList;

/**
 * A match event that the API user feeds into the {@link MatchControl#onEvent(MatchEventBuilder)} to build up a match.
 * <p>Match events are created by builders. Builders can be created by calling the static methods on the {@link MatchEvents} class.</p>
 */
public interface MatchEvent {

    /**
     * @return The time that the event took place, or empty if unknown
     */
    Optional<Instant> time();

    /**
     * Certain events generate more events, for example a {@link InningsStartingEvent} causes a {@link BatterInningsStartingEvent}
     * for each of the openers, and a dismissal from a {@link BallCompletedEvent} causes a {@link BatterInningsEndedEvent}
     * to be raised.
     * <p>These events are applied automatically when using the {@link MatchControl} class</p>
     * @return The list of events generated as a result of applying the current event, or an empty list if none were generated
     */
    default ImmutableList<MatchEventBuilder<?>> generatedEvents() {
        return emptyList();
    }

}
