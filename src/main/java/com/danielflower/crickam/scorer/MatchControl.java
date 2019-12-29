package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchEventBuilder;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import java.time.*;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;

public final class MatchControl {

    public static class EventResult {
        private final MatchEvent event;
        private final Match match;

        private EventResult(MatchEvent event, Match match) {
            this.event = event;
            this.match = match;
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

    private final ImmutableList<EventResult> events;

    private MatchControl(ImmutableList<EventResult> events) {
        this.events = events;
    }

    public static MatchControl newMatch(MatchStartingEvent.Builder builder) {
        return newMatch(builder.build());
    }


    public static MatchControl newMatch(MatchStartingEvent event) {
        return new MatchControl(ImmutableList.of(new EventResult(event, Match.newMatch(event))));
    }

    public MatchControl onEvent(MatchEventBuilder<?> builder) {
        return onEvent(builder.build(match()));
    }

    MatchControl onEvent(MatchEvent event) {
        Match match = match().onEvent(event);
        return new MatchControl(events.add(new EventResult(event, match)));
    }

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

    public ImmutableList<EventResult> events() {
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
            EventResult er = events.get(i);
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

}
