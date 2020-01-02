package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Provides the match data after the included event was applied
 */
public class EventOutput {
    private final MatchEvent event;
    private final Match match;

    EventOutput(MatchEvent event, Match match) {
        this.event = event;
        this.match = match;
    }

    /**
     * A predicate that returns true if the innings number of the match after applying the event is the same as the
     * given innings number.
     * <p>This is designed to be used to filter the result of {@link MatchControl#history()}</p>
     * @param innings The innings (at any point of time in that innings) that you are searching for
     * @return A predicate that can be used in a stream filter
     */
    @NotNull
    public static Predicate<? super EventOutput> sameInnings(Innings innings) {
        return me -> me.match().currentInnings().isPresent() && me.match().currentInnings().get().inningsNumber() == innings.inningsNumber();
    }

    /**
     * @return The event that occurred
     */
    public MatchEvent event() {
        return event;
    }

    /**
     * @return The match that was created as a result of the event
     */
    public Match match() {
        return match;
    }
}
