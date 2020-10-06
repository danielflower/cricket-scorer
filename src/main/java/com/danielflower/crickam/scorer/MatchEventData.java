package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Data passed to a {@link MatchEventListener}
 */
@Immutable
public interface MatchEventData {

    /**
     * @return The event that was raised
     */
    @Nonnull MatchEvent event();

    /**
     * @return The state of the match before the event was raised, or null if this is the first event of the match
     */
    @Nullable MatchControl matchBeforeEvent();

    /**
     * @return The state of the match after the event was applied
     */
    @Nonnull MatchControl matchAfterEvent();

    /**
     * A shortcut for <code>matchAfterEvent().match()</code>
     * @return The match after the event was applied
     */
    @Nonnull Match match();

    /**
     * Returns the event cast to the given type, if the event is an instance of that type
     * @param eventClass The class to check for
     * @param <T> The type of the event
     * @return The event, or null if the event is not of the desired type
     */
    @Nullable <T extends MatchEvent> T eventAs(Class<T> eventClass);

    /**
     * @return True is this is the first event in the match (and so {@link #event()} will be a {@link com.danielflower.crickam.scorer.events.MatchStartingEvent}
     */
    boolean firstEventInMatch();
}

@Immutable
class MatchEventDataImpl implements MatchEventData {

    private final MatchEvent event;
    private final MatchControl control;

    MatchEventDataImpl(MatchEvent event, MatchControl control) {
        this.event = event;
        this.control = control;
    }

    @Override
    public @Nonnull MatchEvent event() {
        return event;
    }


    @Override
    public @Nullable MatchControl matchBeforeEvent() {
        return control.hasParent() ? control.parent() : null;
    }

    @Override
    public @Nonnull MatchControl matchAfterEvent() {
        return control;
    }

    @Override
    public @Nonnull Match match() {
        return matchAfterEvent().match();
    }

    @Override
    public @Nullable <T extends MatchEvent> T eventAs(Class<T> eventClass) {
        if (eventClass.isAssignableFrom(event.getClass())) {
            return (T)event;
        }
        return null;
    }

    @Override
    public boolean firstEventInMatch() {
        return !control.hasParent();
    }
}
