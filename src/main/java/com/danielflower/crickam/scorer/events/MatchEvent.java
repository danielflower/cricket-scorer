package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchControl;

import java.time.Instant;
import java.util.Optional;

/**
 * A match event that the API user feeds into the {@link MatchControl#onEvent(MatchEventBuilder)} to build up a match.
 * <p>Match events are created by builders. Builders can be created by calling the static methods on the {@link MatchEvents} class.</p>
 */
public interface MatchEvent {

    /**
     * @return The time that the event took place, or empty if unknown
     */
    Optional<Instant> time();

}
