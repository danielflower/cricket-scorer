package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;

import java.util.Optional;

/**
 * Data passed to a {@link MatchEventListener}
 */
public interface MatchEventData {

    /**
     * @return The event that was raised
     */
    MatchEvent event();

    /**
     * @return The state of the match before the event was raised, or empty if this is the first event of the match
     */
    Optional<MatchControl> matchBeforeEvent();

    /**
     * @return The state of the match after the event was applied
     */
    MatchControl matchAfterEvent();

    /**
     * A shortcut for <code>matchAfterEvent().match()</code>
     * @return The match after the event was applied
     */
    Match match();

    /**
     * Returns the event cast to the given type, if the event is an instance of that type
     * @param eventClass The class to check for
     * @param <T> The type of the event
     * @return The event, or empty if the event is not of the desired type
     */
    <T extends MatchEvent> Optional<T> eventAs(Class<T> eventClass);
}

class MatchEventDataImpl implements MatchEventData {

    private final MatchEvent event;
    private final MatchControl control;

    MatchEventDataImpl(MatchEvent event, MatchControl control) {
        this.event = event;
        this.control = control;
    }

    @Override
    public MatchEvent event() {
        return event;
    }

    @Override
    public Optional<MatchControl> matchBeforeEvent() {
        return control.hasParent() ? Optional.of(control.parent()) : Optional.empty();
    }

    @Override
    public MatchControl matchAfterEvent() {
        return control;
    }

    @Override
    public Match match() {
        return matchAfterEvent().match();
    }

    @Override
    public <T extends MatchEvent> Optional<T> eventAs(Class<T> eventClass) {
        if (eventClass.isAssignableFrom(event.getClass())) {
            return Optional.of((T)event);
        }
        return Optional.empty();
    }
}
