package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEventBuilder;

/**
 * A hook for listening (and creating) events that are added or generated to a match.
 * <p>Register listeners with {@link com.danielflower.crickam.scorer.events.MatchStartingEvent.Builder#withEventListeners(MatchEventListener...)}</p>
 */
public interface MatchEventListener {

    /**
     * Listener callback invoked when an event has been added.
     * <p>This also allows you to generate new events off the back of an existing event.</p>
     * <p>Note: this is invoked before any child events are applied.</p>
     *
     * @param data Information about the event
     * @return A list of events to apply to the match. This can be null
     * @throws Exception Any unhandled exceptions will bubble up to the <code>onEvent</code> method.
     */
    ImmutableList<MatchEventBuilder<?, ?>> onEvent(MatchEventData data) throws Exception;

}
