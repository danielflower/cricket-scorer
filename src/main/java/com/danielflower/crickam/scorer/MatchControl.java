package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchEventBuilder;
import com.danielflower.crickam.scorer.events.MatchEvents;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.time.*;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

/**
 * This class is the main entry point of the API.
 * <p>To generate a cricket match model, create a new control with {@link #newMatch(MatchStartingEvent)},
 * then feed in events to {@link #onEvent(MatchEvent)}.</p>
 * <p>After events have been passed in, the current state can be found by calling {@link #match()} and historical
 * states can be found in {@link #history()}.</p>
 * <p>You can also look up specific events, and then get a match control object at that point in time using the
 * {@link #eventStream(Class)} and {@link #asAt(MatchEvent)} methods.</p>
 * <p>Because this object (and all objects it links to) are immutable, the {@code onEvent} methods return new
 * instances of this object rather than mutating the state. This means you are free to do things such as apply
 * events to existing objects and have the results be independent of other instances. For example, you can perform
 * &quot;what if &quot; scenarios (such as &quot;what if a dropped catch had been caught&quot;) by looking
 * up the state as at some specific event with {@link #eventStream(Class)}, get the match control at that
 * point of time using {@link #asAt(MatchEvent)}, and then add new events using {@link #onEvent(MatchEvent)}
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
     * @param event A new-match event
     * @return A match control object you can use to build up match state
     * @see MatchEvents#matchStarting(int, Integer)
     */
    public static @Nonnull MatchControl newMatch(MatchStartingEvent event) {
        requireNonNull(event, "event");
        Match match = Match.newMatch(event);
        return new MatchControl(ImmutableList.emptyList(), event, match);
    }

    /**
     * Calls {@link MatchEventBuilder#apply(Match)} on the builder, then builds and applies the event to the match,
     * returning a new instance of match control.
     * <p>The original MatchControl instance is not changed.</p>
     * @param builder A match event builder
     * @return A new Match Control object
     * @see MatchEvents MatchEvents class for a number of handy builder objects
     */
    public @Nonnull MatchControl onEvent(MatchEventBuilder<?,?> builder) {
        requireNonNull(builder, "builder");
        MatchEventBuilder<?, ?> applied = builder.apply(match());
        MatchEvent built = applied.build();
        return onEvent(built);
    }

    /**
     * Applies the given event to the match, and returns a new instance of match control.
     * <p>The original object is not changed.</p>
     * @param event A match event
     * @return A new Match Control object
     * @see MatchEvents MatchEvents class for a number of handy builder objects
     */
    public @Nonnull MatchControl onEvent(MatchEvent event) {
        requireNonNull(event, "event");
        Match newMatch = match().onEvent(event);
        ImmutableList<MatchControl> newHistory = this.ancestors.add(this);
        return new MatchControl(newHistory, event, newMatch);
    }

    /**
     * @return The match at the current point in time
     */
    public @Nonnull Match match() {
        return match;
    }

    /**
     * @return The last event that was applied
     */
    public @Nonnull MatchEvent event() {
        return event;
    }

    /**
     * @return true if {@link #parent()} has a parent match control; false if this is the control created by {@link #newMatch(MatchStartingEvent)}
     * @see #parent()
     */
    public boolean hasParent() {
        return !ancestors.isEmpty();
    }

    /**
     * Returns the match control that is the parent of this one.
     * <p>This can be used to effectively undo events.</p>
     * @return the parent of this control
     * @see #hasParent()
     * @throws IllegalStateException if this has no parent
     */
    public @Nonnull MatchControl parent() {
        MatchControl last = ancestors.last();
        if (last == null) throw new IllegalStateException("Cannot get the parent of the first event");
        return last;
    }

    /**
     * Gets the current innings, or throws an exception if none are in progress.
     * <p>To get an optional innings, use {@code match().currentInnings()} instead.</p>
     * @return The current innings that's in progress
     * @throws IllegalStateException There is not an innings in progress
     */
    public @Nonnull Innings currentInnings() {
        Innings innings = match().currentInnings();
        if (innings == null) throw new IllegalStateException("There is no innings in progress");
        return innings;
    }

    /**
     * @see #eventStream(Class)
     * @return All the events and corresponding states in the order they occurred
     */
    public @Nonnull ImmutableList<MatchControl> history() {
        return ancestors.add(this);
    }

    /**
     * @return The number of events that have taken place
     */
    public @Nonnegative int eventCount() {
        return ancestors.size() + 1;
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
        TimeZone tz = match().timeZone();
        if (tz == null) throw new UnsupportedOperationException("No time zone was set on the match (or on the venue) so local times cannot be calculated");
        ZoneId zoneId = tz.toZoneId();
        ImmutableList<MatchControl> history = history();
        for (int i = history.size() - 1; i >= 0; i--) {
            MatchControl er = history.get(i);
            Instant lastKnownTime = er.event().time();
            if (lastKnownTime != null) {
                LocalDate date = ofInstant(lastKnownTime, zoneId);
                LocalTime time = LocalTime.of(hour, minute, second);
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                return dateTime.atZone(zoneId).toInstant();
            }
        }
        throw new UnsupportedOperationException("No events have a time recorded against them, so this method cannot know which date to use");
    }

    private static LocalDate ofInstant(Instant instant, ZoneId zone) {
        ZoneOffset offset = zone.getRules().getOffset(instant);
        long localSecond = instant.getEpochSecond() + offset.getTotalSeconds();
        long localEpochDay = Math.floorDiv(localSecond, 86400);
        return LocalDate.ofEpochDay(localEpochDay);
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
    public @Nonnull MatchControl asAt(MatchEvent event) {
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
    public @Nonnull <T extends MatchEvent> Stream<T> eventStream(Class<T> eventClass) {
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
    public static @Nonnull Predicate<MatchControl> sameInnings(Innings innings) {
        return mc -> {
            Innings i = mc.match().currentInnings();
            return i != null && i.inningsNumber() == innings.inningsNumber();
        };
    }

    /**
     * Returns the match as at the last undo point (i.e. as at the previous time a match event
     * has {@link MatchEvent#undoPoint()} equal to <code>true</code>
     * @return The match with the last user generated event undone.
     */
    public @Nonnull MatchControl undo() {
        return currentUndoPoint().parent();
    }

    private MatchControl currentUndoPoint() {
        Iterator<MatchControl> all = history().reverseIterator();
        while (all.hasNext()) {
            MatchControl control = all.next();
            if (control.event().undoPoint()) {
                return control;
            }
        }
        throw new IllegalStateException("Cannot undo the first match event");
    }

}
