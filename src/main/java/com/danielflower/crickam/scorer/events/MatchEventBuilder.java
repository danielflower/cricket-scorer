package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

/**
 * A builder for a match event
 * @param <T> The type of event
 */
public interface MatchEventBuilder<T extends MatchEvent> {

    /**
     * Builds the event
     * @param match The state of the current match which may be used to fill in certain values
     * @return A newly constructed T
     * @throws NullPointerException thrown when a required field is not set
     * @throws IllegalArgumentException thrown when a specified value is invalid
     */
    T build(Match match);

}
