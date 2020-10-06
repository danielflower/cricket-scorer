package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.*;

import static com.danielflower.crickam.scorer.Crictils.requireNonNullElseGet;
import static java.util.Objects.requireNonNull;

@Immutable
public final class MatchStartingEvent extends BaseMatchEvent {

    private final String matchID;
    private final Series series;
    private final Instant scheduledStartTime;
    private final ImmutableList<LineUp> lineUps;
    private final MatchType matchType;
    private final int inningsPerTeam;
    private final Integer oversPerInnings;
    private final Venue venue;
    private final int numberOfScheduledDays;
    private final Integer ballsPerInnings;
    private final TimeZone timeZone;
    private final transient ImmutableList<MatchEventListener> eventListeners;

    private MatchStartingEvent(String id, @Nullable String generatedBy, String matchID, @Nullable Series series, @Nullable Instant time, @Nullable Instant scheduledStartTime,
                               ImmutableList<LineUp> lineUps, MatchType matchType, @Nonnegative int inningsPerTeam, @Nullable Integer oversPerInnings, @Nullable Venue venue,
                               @Nonnegative int numberOfScheduledDays, @Nullable Integer ballsPerInnings, @Nullable TimeZone timeZone, ImmutableList<MatchEventListener> eventListeners) {
        super(id, time, generatedBy);
        this.matchID = requireNonNull(matchID, "matchID");
        this.series = series;
        this.scheduledStartTime = scheduledStartTime;
        this.lineUps = requireNonNull(lineUps, "lineUps");
        this.matchType = requireNonNull(matchType, "matchType");
        this.inningsPerTeam = inningsPerTeam;
        this.oversPerInnings = oversPerInnings;
        this.venue = venue;
        this.numberOfScheduledDays = numberOfScheduledDays;
        this.ballsPerInnings = ballsPerInnings;
        this.timeZone = timeZone;
        this.eventListeners = eventListeners;
    }

    public @Nonnull ImmutableList<MatchEventListener> eventListeners() {
        return eventListeners;
    }

    public @Nonnull String matchID() {
        return matchID;
    }

    public @Nullable Series series() {
        return series;
    }

    public @Nullable Instant scheduledStartTime() {
        return scheduledStartTime;
    }

    public @Nonnull ImmutableList<LineUp> teamLineUps() {
        return lineUps;
    }

    public @Nonnull MatchType matchType() {
        return matchType;
    }

    public @Nonnegative int inningsPerTeam() {
        return inningsPerTeam;
    }

    public @Nullable Integer oversPerInnings() {
        return oversPerInnings;
    }

    public @Nullable Venue venue() {
        return venue;
    }

    public @Nonnegative int numberOfScheduledDays() {
        return numberOfScheduledDays;
    }

    public @Nullable Integer ballsPerInnings() {
        return ballsPerInnings;
    }

    public @Nullable TimeZone timeZone() {
        return timeZone;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return new Builder()
            .withMatchID(matchID)
            .withSeries(series)
            .withScheduledStartTime(scheduledStartTime)
            .withTeamLineUps(lineUps)
            .withMatchType(matchType)
            .withInningsPerTeam(inningsPerTeam)
            .withOversPerInnings(oversPerInnings)
            .withNumberOfScheduledDays(numberOfScheduledDays)
            .withVenue(venue)
            .withBallsPerInnings(ballsPerInnings)
            .withTimeZone(timeZone)
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, MatchStartingEvent> {
        private String matchID;
        private Series series;
        private Instant startTime;
        private ImmutableList<LineUp> lineUps;
        private MatchType matchType;
        private int inningsPerTeam;
        private Integer oversPerInnings;
        private int numberOfScheduledDays;
        private Venue venue;
        private Integer ballsPerInnings;
        private TimeZone timeZone;
        private ImmutableList<MatchEventListener> eventListeners = ImmutableList.emptyList();

        public @Nullable String matchID() {
            return matchID;
        }

        public @Nullable Series series() {
            return series;
        }

        public @Nullable Instant scheduledStartTime() {
            return startTime;
        }

        public @Nullable ImmutableList<LineUp> teamLineUps() {
            return lineUps;
        }

        public @Nullable MatchType matchType() {
            return matchType;
        }

        public @Nonnegative int inningsPerTeam() {
            return inningsPerTeam;
        }

        public @Nullable Integer oversPerInnings() {
            return oversPerInnings;
        }

        public @Nonnegative int numberOfScheduledDays() {
            return numberOfScheduledDays;
        }

        public @Nullable Venue venue() {
            return venue;
        }

        public @Nullable Integer ballsPerInnings() {
            return ballsPerInnings;
        }

        public @Nullable TimeZone timeZone() {
            return timeZone;
        }

        public @Nonnull Builder withMatchID(String matchID) {
            this.matchID = matchID;
            return this;
        }

        public @Nonnull Builder withSeries(@Nullable Series series) {
            this.series = series;
            return this;
        }

        public ImmutableList<MatchEventListener> eventListeners() {
            return eventListeners;
        }

        /**
         * @param startTime The expected time of the first ball
         * @return This builder
         */
        public @Nonnull Builder withScheduledStartTime(@Nullable Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public @Nonnull Builder withTeamLineUps(ImmutableList<LineUp> lineUps) {
            this.lineUps = lineUps;
            return this;
        }

        public @Nonnull Builder withMatchType(MatchType matchType) {
            this.matchType = matchType;
            return this;
        }

        public @Nonnull Builder withInningsPerTeam(int inningsPerTeam) {
            this.inningsPerTeam = inningsPerTeam;
            return this;
        }

        public @Nonnull Builder withOversPerInnings(@Nullable Integer oversPerInnings) {
            this.oversPerInnings = oversPerInnings;
            return this;
        }

        public @Nonnull Builder withBallsPerInnings(@Nullable Integer ballsPerInnings) {
            this.ballsPerInnings = ballsPerInnings;
            return this;
        }

        public @Nonnull Builder withNumberOfScheduledDays(int numberOfScheduledDays) {
            this.numberOfScheduledDays = numberOfScheduledDays;
            return this;
        }

        public @Nonnull Builder withVenue(@Nullable Venue venue) {
            this.venue = venue;
            return this;
        }

        /**
         * The time zone that this match is played in. This is optional, and if left unset then then timezone
         * of the venue will be used if that has been set with {@link #withVenue(Venue)}
         *
         * @param timeZone The time zone that this match was played in
         * @return This builder
         */
        public @Nonnull Builder withTimeZone(@Nullable TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        /**
         * Event listeners allow you to listen to all events (including generated events) and add your own
         * events to the match.
         *
         * @param eventListeners The list of listeners
         * @return This builder
         */
        public @Nonnull Builder withEventListeners(ImmutableList<MatchEventListener> eventListeners) {
            this.eventListeners = eventListeners;
            return this;
        }

        /**
         * Event listeners allow you to listen to all events (including generated events) and add your own
         * events to the match.
         *
         * @param eventListeners The list of listeners
         * @return This builder
         */
        public @Nonnull Builder withEventListeners(MatchEventListener... eventListeners) {
            return withEventListeners(ImmutableList.of(eventListeners));
        }

        @Nonnull
        public MatchStartingEvent build(@Nullable Match match /* null for only this event type */) {
            Integer bpi = this.ballsPerInnings;
            if (bpi == null && oversPerInnings != null) {
                bpi = 6 * oversPerInnings;
            }
            TimeZone timeZone = this.timeZone;
            if (timeZone == null && venue != null) {
                timeZone = venue.timeZone();
            }
            String matchID = requireNonNullElseGet(this.matchID, () -> UUID.randomUUID().toString());
            return new MatchStartingEvent(id(), generatedBy(), matchID, series, time(), startTime, lineUps, matchType,
                inningsPerTeam, oversPerInnings, venue, numberOfScheduledDays, bpi, timeZone, eventListeners);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return inningsPerTeam == builder.inningsPerTeam &&
                numberOfScheduledDays == builder.numberOfScheduledDays &&
                Objects.equals(matchID, builder.matchID) &&
                Objects.equals(series, builder.series) &&
                Objects.equals(startTime, builder.startTime) &&
                Objects.equals(lineUps, builder.lineUps) &&
                matchType == builder.matchType &&
                Objects.equals(oversPerInnings, builder.oversPerInnings) &&
                Objects.equals(venue, builder.venue) &&
                Objects.equals(ballsPerInnings, builder.ballsPerInnings) &&
                Objects.equals(timeZone, builder.timeZone) &&
                Objects.equals(eventListeners, builder.eventListeners);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), matchID, series, startTime, lineUps, matchType, inningsPerTeam, oversPerInnings, numberOfScheduledDays, venue, ballsPerInnings, timeZone, eventListeners);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "matchID='" + matchID + '\'' +
                ", series=" + series +
                ", startTime=" + startTime +
                ", lineUps=" + lineUps +
                ", matchType=" + matchType +
                ", inningsPerTeam=" + inningsPerTeam +
                ", oversPerInnings=" + oversPerInnings +
                ", numberOfScheduledDays=" + numberOfScheduledDays +
                ", venue=" + venue +
                ", ballsPerInnings=" + ballsPerInnings +
                ", timeZone=" + timeZone +
                ", eventListeners=" + eventListeners +
                "} " + super.toString();
        }
    }
}
