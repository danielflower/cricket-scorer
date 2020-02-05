package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchEventBuilder;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import java.time.*;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

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

    private final ImmutableList<MatchControl> ancestors;
    private final MatchEvent event;
    private final Match match;

    private MatchControl(ImmutableList<MatchControl> ancestors, MatchEvent event, Match match) {
        this.ancestors = ancestors;
        this.event = event;
        this.match = match;
    }

    /**
     * Creates a new match control object
     * @param builder A builder for a new-match event
     * @return A match control object you can use to build up match state
     * @see MatchEvents#matchStarting(MatchType)
     */
    public static MatchControl newMatch(MatchStartingEvent.Builder builder) {
        requireNonNull(builder, "builder");
        MatchStartingEvent event = builder.build(null);
        Match match = Match.newMatch(event);
        return new MatchControl(ImmutableList.emptyList(), event, match);
    }

    /**
     * Applies the given event to the match, and returns a new instance of match control.
     * <p>The original object is not changed.</p>
     * @param builder A builder for a match event
     * @return A new Match Control object
     * @see MatchEvents MatchEvents class for a number of handy builder objects
     */
    public MatchControl onEvent(MatchEventBuilder<?,?> builder) {
        requireNonNull(builder, "builder");
        MatchEvent event = builder.build(match());
        Match newMatch = match().onEvent(event);
        ImmutableList<MatchControl> newHistory = this.ancestors.add(this);
        MatchControl newMatchControl = new MatchControl(newHistory, event, newMatch);

        for (MatchEventBuilder<?,?> childBuilder : event.generatedEvents()) {
            childBuilder.withGeneratedBy(event);
            newMatchControl = newMatchControl.onEvent(childBuilder);
        }

        return newMatchControl;
    }

    /**
     * @return The match at the current point in time
     */
    public Match match() {
        return match;
    }

    /**
     * @return The last event that was applied
     */
    public MatchEvent event() {
        return event;
    }

    /**
     * @return true if {@link #parent()} has a parent match control; false if this is the control created by {@link #newMatch(MatchStartingEvent.Builder)}
     * @see #parent()
     */
    public boolean hasParent() {
        return !ancestors.isEmpty();
    }

    /**
     * Returns the match control that is the parent of this one.
     * <p>Note that the parent state may not be the state of the match as at the time the API user last added an
     * event, as events may generate child events. To undo the last added user event, call {@link #atPreviousUserGeneratedEvent()}
     * instead.</p>
     * @return the parent of this control
     * @see #hasParent()
     * @see #atPreviousUserGeneratedEvent()
     * @see #atLastUserGeneratedEvent()
     * @throws IllegalStateException if this has no parent
     */
    public MatchControl parent() {
        return ancestors.last().orElseThrow(() -> new IllegalStateException("Cannot get the parent of the first event"));
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
    public ImmutableList<MatchControl> history() {
        return ancestors.add(this);
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
        ImmutableList<MatchControl> history = history();
        for (int i = history.size() - 1; i >= 0; i--) {
            MatchControl er = history.get(i);
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
     *         .filter(ice -&gt; ice.inningsNumber() == 2)
     *         .findFirst()
     *         .map(e -&gt; control.asAt(e))
     *         .orElseThrow();
     * </code></pre>
     * @param event The event to look up
     * @return The {@code MatchControl} as at the time that the event was added
     */
    public MatchControl asAt(MatchEvent event) {
        requireNonNull(event, "event");
        for (MatchControl matchControl : this.history()) {
            if (matchControl.event().equals(event)) {
                return matchControl;
            }
        }
        throw new IllegalArgumentException("No matching event found on this match");
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
        Class<? extends MatchEvent> clazz = requireNonNull(eventClass, "eventClass");
        return history().stream()
            .filter(c -> c.event().getClass().equals(clazz))
            .map(MatchControl::event)
            .map(eventClass::cast);
    }

    /**
     * A predicate that returns true if the innings number of the match after applying the event is the same as the
     * given innings number.
     * <p>This is designed to be used to filter the result of {@link MatchControl#history()}</p>
     * @param innings The innings (at any point of time in that innings) that you are searching for
     * @return A predicate that can be used in a stream filter
     */
    public static Predicate<MatchControl> sameInnings(Innings innings) {
        return mc -> mc.match().currentInnings().isPresent()
            && mc.match().currentInnings().get().inningsNumber() == innings.inningsNumber();
    }

    /**
     * Some events auto generate child events, in which case {@link #event()} would not link to the last event
     * added by the API user. This method finds the most recent match control state generated by a user event.
     * @return The match as at last event that was generated by the API user
     * @see #atPreviousUserGeneratedEvent()
     */
    public MatchControl atLastUserGeneratedEvent() {
        Iterator<MatchControl> all = history().reverseIterator();
        while (all.hasNext()) {
            MatchControl control = all.next();
            if (control.event().generatedBy().isEmpty()) {
                return control;
            }
        }
        // should not be possible to get here as the first event is always user generated
        throw new IllegalStateException("Cannot call this method on an empty match");
    }

    /**
     * Returns the match as at the state when the second to last event was added by the API user.
     * <p>This can be used to undo applied events by calling this method on a match control, and then calling
     * {@link #onEvent(MatchEventBuilder)} with a new event on the parent control.</p>
     * <p>This differs from {@link #atLastUserGeneratedEvent()} in that that method may return
     * the current state of the match.</p>
     * @return The match with the last user generated event undone.
     * @see #atLastUserGeneratedEvent()
     */
    public MatchControl atPreviousUserGeneratedEvent() {
        return atLastUserGeneratedEvent().parent().atLastUserGeneratedEvent();
    }
}
