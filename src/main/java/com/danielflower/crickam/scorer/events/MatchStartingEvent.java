package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TimeZone;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public final class MatchStartingEvent implements MatchEvent {

    private final String matchID;
    private final Series series;
    private final Instant time;
    private final Instant scheduledStartTime;
    private final ImmutableList<LineUp> lineUps;
    private final MatchType matchType;
    private final int inningsPerTeam;
    private final Integer oversPerInnings;
    private final Venue venue;
    private final int numberOfScheduledDays;
    private final Integer ballsPerInnings;
    private final TimeZone timeZone;

    private MatchStartingEvent(String matchID, Series series, Instant time, Instant scheduledStartTime, ImmutableList<LineUp> lineUps, MatchType matchType, int inningsPerTeam, Integer oversPerInnings, Venue venue, int numberOfScheduledDays, Integer ballsPerInnings, TimeZone timeZone) {
        this.matchID = requireNonNull(matchID, "matchID");
        this.series = series;
        this.time = time;
        this.scheduledStartTime = scheduledStartTime;
        this.lineUps = requireNonNull(lineUps, "lineUps");
        this.matchType = requireNonNull(matchType, "matchType");
        this.inningsPerTeam = inningsPerTeam;
        this.oversPerInnings = oversPerInnings;
        this.venue = venue;
        this.numberOfScheduledDays = numberOfScheduledDays;
        this.ballsPerInnings = ballsPerInnings;
        this.timeZone = timeZone;
    }

    /**
     * @return The time this event was announced. Note this is separate from {@link #scheduledStartTime}
     */
    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public String matchID() {
        return matchID;
    }

    public Optional<Series> series() {
        return Optional.ofNullable(series);
    }

    public Optional<Instant> scheduledStartTime() {
        return Optional.ofNullable(scheduledStartTime);
    }

    public ImmutableList<LineUp> teamLineUps() {
        return lineUps;
    }

    public MatchType matchType() {
        return matchType;
    }

    public int inningsPerTeam() {
        return inningsPerTeam;
    }

    public OptionalInt oversPerInnings() {
        return Crictils.toOptional(oversPerInnings);
    }

    public Optional<Venue> venue() {
        return Optional.ofNullable(venue);
    }

    public int numberOfScheduledDays() {
        return numberOfScheduledDays;
    }

    public OptionalInt ballsPerInnings() {
        return Crictils.toOptional(ballsPerInnings);
    }

    public Optional<TimeZone> timeZone() {
        return Optional.ofNullable(timeZone);
    }

    public static final class Builder implements MatchEventBuilder<MatchStartingEvent> {
        private String matchID;
        private Series series;
        private Instant time;
        private Instant startTime;
        private ImmutableList<LineUp> lineUps;
        private MatchType matchType;
        private int inningsPerTeam;
        private Integer oversPerInnings;
        private int numberOfScheduledDays;
        private Venue venue;
        private Integer ballsPerInnings;
        private TimeZone timeZone;

        public Builder withMatchID(String matchID) {
            this.matchID = matchID;
            return this;
        }

        public Builder withSeries(Series series) {
            this.series = series;
            return this;
        }

        /**
         * Sets the time of the event. Note this is separate from {@link #withScheduledStartTime(Instant)} which is
         * the expected time of the first ball.
         *
         * @param time The time match is starting.
         * @return This builder
         */
        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        /**
         * @param startTime The expected time of the first ball
         * @return This builder
         */
        public Builder withScheduledStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withTeamLineUps(ImmutableList<LineUp> lineUps) {
            this.lineUps = lineUps;
            return this;
        }

        public Builder withMatchType(MatchType matchType) {
            this.matchType = matchType;
            return this;
        }

        public Builder withInningsPerTeam(int inningsPerTeam) {
            this.inningsPerTeam = inningsPerTeam;
            return this;
        }

        public Builder withOversPerInnings(Integer oversPerInnings) {
            this.oversPerInnings = oversPerInnings;
            return this;
        }

        public Builder withBallsPerInnings(Integer ballsPerInnings) {
            this.ballsPerInnings = ballsPerInnings;
            return this;
        }

        public Builder withNumberOfScheduledDays(int numberOfScheduledDays) {
            this.numberOfScheduledDays = numberOfScheduledDays;
            return this;
        }

        public Builder withVenue(Venue venue) {
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
        public Builder withTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public MatchStartingEvent build(Match match /* null for only this event type */) {
            Integer bpi = this.ballsPerInnings;
            if (bpi == null && oversPerInnings != null) {
                bpi = 6 * oversPerInnings;
            }
            TimeZone timeZone = this.timeZone;
            if (timeZone == null && venue != null) {
                timeZone = venue.timeZone();
            }
            String matchID = requireNonNullElseGet(this.matchID, () -> UUID.randomUUID().toString());
            return new MatchStartingEvent(matchID, series, time, startTime, lineUps, matchType, inningsPerTeam, oversPerInnings, venue, numberOfScheduledDays, bpi, timeZone);
        }
    }
}
