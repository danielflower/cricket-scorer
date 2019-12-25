package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;

/**
 * A builder for a match event
 * @param <T> The type of event
 */
public interface MatchEventBuilder<T extends MatchEvent> {

    /**
     * Creates a new event
     * @return A newly constructed T
     */
    T build();

}
