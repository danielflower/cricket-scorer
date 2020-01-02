package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchEventBuilder;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import java.time.*;
import java.util.Objects;
import java.util.stream.Stream;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;

/**
 * This class is the main entry point of the API.
 * <p>To generate a cricket match model, create a new control with {@link #newMatch(MatchStartingEvent.Builder)},
 * then feed in events to {@link #onEvent(MatchEventBuilder)}.</p>
 * <p>After events have been passed in, the current state can be found by calling {@link #match()} and historical
 * states can be found in {@link #history()}.</p>
 * <p>You can also look up specific events, and then get a match control object at that point in time using the
 * {@link #eventStream(Class)} and {@link #asAt(MatchEvent)} methods.</p>
 * <p>Because this object (and all objects it links to) are immutable, the {@code onEvent} methods return new
 * instances of this object rather than mutating the state. This means you are free to do things such as apply
 * events to existing objects and have the results be independent of other instances. For example, you can perform
 * &quot;what if &quot; scenarios (such as &quot;what if a dropped catch had been caught&quot;) by looking
 * up the state as at some specific event with {@link #eventStream(Class)}, get the match control at that
 * point of time using {@link #asAt(MatchEvent)}, and then add new events using {@link #onEvent(MatchEventBuilder)}
 * to add the dismissal, and then access the new state at {@link #match()}. This would all happen on a copy of the
 * match control, so the original match data would not be changed.</p>
 */
public final class MatchControl {

    private final ImmutableList<EventOutput> events;

    private MatchControl(ImmutableList<EventOutput> events) {
        this.events = events;
    }

    /**
     * Creates a new match control object
     * @param builder A builder for a new-match event
     * @return A match control object you can use to build up match state
     * @see MatchEvents#matchStarting(MatchType)
     */
    public static MatchControl newMatch(MatchStartingEvent.Builder builder) {
        MatchStartingEvent event = builder.build();
        return new MatchControl(ImmutableList.of(new EventOutput(event, Match.newMatch(event))));
    }

    /**
     * Applies the given event to the match, and returns a new instance of match control.
     * <p>The original object is not changed.</p>
     * @param builder A builder for a match event
     * @return A new Match Control object
     * @see MatchEvents MatchEvents class for a number of handy builder objects
     */
    public MatchControl onEvent(MatchEventBuilder<?> builder) {
        return onEvent(builder.build(match()));
    }

    MatchControl onEvent(MatchEvent event) {
        Match match = match().onEvent(event);
        ImmutableList<EventOutput> newEvents = events.add(new EventOutput(event, match));

        for (MatchEventBuilder<?> builder : event.generatedEvents()) {
            MatchEvent generatedEvent = builder.build(match);
            match = match.onEvent(generatedEvent);
            newEvents = newEvents.add(new EventOutput(generatedEvent, match));
        }

        return new MatchControl(newEvents);
    }

    /**
     * @return The match at the current point in time
     */
    public Match match() {
        return events.get(events.size() - 1).match();
    }

    /**
     * Gets the current innings, or throws an exception if none are in progress.
     * <p>To get an optional innings, use {@code match().currentInnings()} instead.</p>
     * @return The current innings that's in progress
     * @throws IllegalStateException There is not an innings in progress
     */
    public Innings currentInnings() {
        return match().currentInnings().orElseThrow(() -> new IllegalStateException("There is no innings in progress"));
    }

    /**
     * @see #eventStream(Class)
     * @return All the events and corresponding states in the order they occurred
     */
    public ImmutableList<EventOutput> history() {
        return events;
    }

    /**
     * A convenient way to convert a local time (such as &quot;17:30&quot;) into a {@code Instant} object. This
     * method uses the match's {@link Match#timeZone()} as the time zone and the last {@link MatchEvent#time()} to find
     * the date
     * @param hour The hour in the local time zone (between 0 and 23)
     * @param minute The minute in the local time zone (between 0 and 59)
     * @param second The second in the local time zone (between 0 and 59)
     * @return An instant based on the current match date and local time zone
     */
    public Instant localTime(int hour, int minute, int second) {
        requireInRange("hour", hour, 0, 23);
        requireInRange("minute", minute, 0, 59);
        requireInRange("second", second, 0, 59);
        ZoneId zoneId = match().timeZone()
            .orElseThrow(() -> new UnsupportedOperationException("No time zone was set on the match (or on the venue) so local times cannot be calculated"))
            .toZoneId();
        for (int i = events.size() - 1; i >= 0; i--) {
            EventOutput er = events.get(i);
            if (er.event().time().isPresent()) {
                Instant lastKnownTime = er.event().time().get();
                LocalDate date = LocalDate.ofInstant(lastKnownTime, zoneId);
                LocalTime time = LocalTime.of(hour, minute, second);
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                return dateTime.atZone(zoneId).toInstant();
            }
        }
        throw new UnsupportedOperationException("No events have a time recorded against them, so this method cannot know which date to use");
    }

    /**
     * Gets the match control state as at the time of another event.
     * <p>One way to find an event is to {@link #eventStream(Class)}. In the following example, the state of
     * the match is found as at the end of the second innings:</p>
     * <pre><code>
     * MatchControl controlAtEndOfInnings2 = control.eventStream(InningsCompletedEvent.class)
     *         .filter(ice -> ice.inningsNumber() == 2)
     *         .findFirst()
     *         .map(e -> control.asAt(e))
     *         .orElseThrow();
     * </code></pre>
     * @param event The event to look up
     * @return The {@code MatchControl} as at the time that the event was added
     */
    public MatchControl asAt(MatchEvent event) {
        int index = 0;
        for (EventOutput eo : events) {
            if (eo.event() == event) {
                return new MatchControl(events.subList(0, index));
            }
            index++;
        }
        throw new IllegalArgumentException("No event found on this match");
    }

    /**
     * Returns a stream of the events that have happened on this match.
     * <p>Note that given a single event, the entire state of that match at that point can be found by
     * using the {@link #asAt(MatchEvent)} method.</p>
     * @param eventClass The type of event to filter by
     * @param <T> The type of stream that will be returned
     * @return A stream
     */
    public <T extends MatchEvent> Stream<T> eventStream(Class<T> eventClass) {
        Class<? extends MatchEvent> clazz = Objects.requireNonNullElse(eventClass, MatchEvent.class);
        return events.stream()
            .map(EventOutput::event)
            .filter(event -> event.getClass().equals(clazz))
            .map(eventClass::cast);
    }
}
