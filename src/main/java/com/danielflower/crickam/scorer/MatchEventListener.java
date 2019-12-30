package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;

/**
 * A class that listens to events (e.g. to build up state)
 * @param <T> The type of object that this listener produces
 */
public interface MatchEventListener<T> {

    /**
     * Applies an event to a listener, and returns the result of applying it
     * @param event The event
     * @return a transformed listener which will receive the next event (may be a new object, or the same as the current instance)
     *
     */
    T onEvent(MatchEvent event);

}
