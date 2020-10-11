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
    private final Instant scheduledStartTime;
    private final ImmutableList<LineUp<?>> lineUps;
    private final int inningsPerTeam;
    private final Integer oversPerInnings;
    private final int numberOfScheduledDays;
    private final Integer ballsPerInnings;
    private final TimeZone timeZone;
    private final transient ImmutableList<MatchEventListener> eventListeners;
    private final Object customData;

    private MatchStartingEvent(String id, @Nullable String generatedBy, String matchID, @Nullable Instant time, @Nullable Instant scheduledStartTime,
                               ImmutableList<LineUp<?>> lineUps, @Nonnegative int inningsPerTeam, @Nullable Integer oversPerInnings,
                               @Nonnegative int numberOfScheduledDays, @Nullable Integer ballsPerInnings, @Nullable TimeZone timeZone,
                               ImmutableList<MatchEventListener> eventListeners, @Nullable Object customData) {
        super(id, time, generatedBy, customData);
        this.matchID = requireNonNull(matchID, "matchID");
        this.scheduledStartTime = scheduledStartTime;
        this.lineUps = requireNonNull(lineUps, "lineUps");
        this.inningsPerTeam = inningsPerTeam;
        this.oversPerInnings = oversPerInnings;
        this.numberOfScheduledDays = numberOfScheduledDays;
        this.ballsPerInnings = ballsPerInnings;
        this.timeZone = timeZone;
        this.eventListeners = eventListeners;
        this.customData = customData;
    }

    public @Nonnull ImmutableList<MatchEventListener> eventListeners() {
        return eventListeners;
    }

    public @Nonnull String matchID() {
        return matchID;
    }

    public @Nullable Instant scheduledStartTime() {
        return scheduledStartTime;
    }

    public @Nonnull ImmutableList<LineUp<?>> teamLineUps() {
        return lineUps;
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
            .withScheduledStartTime(scheduledStartTime)
            .withTeamLineUps(lineUps)
            .withInningsPerTeam(inningsPerTeam)
            .withOversPerInnings(oversPerInnings)
            .withNumberOfScheduledDays(numberOfScheduledDays)
            .withBallsPerInnings(ballsPerInnings)
            .withTimeZone(timeZone)
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public Object customData() {
        return customData;
    }

    /**
     * Creates a builder for a 2-innings-per-side, multi-day cricket match, such as an international level test
     * match or domestic first class game.
     * @param days The number of scheduled days
     * @return A new builder
     */
    public static Builder firstClass(int days) {
        return new Builder().withInningsPerTeam(2).withNumberOfScheduledDays(days);
    }

    /**
     * Creates a builder for a limited-overs match, e.g. a T20, ODI, List A etc
     * @param overs The number of overs per innings
     * @return A new builder
     */
    public static Builder limitedOvers(int overs) {
        return new Builder().withInningsPerTeam(1).withNumberOfScheduledDays(1).withOversPerInnings(overs);
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, MatchStartingEvent> {
        private String matchID;
        private Instant startTime;
        private ImmutableList<LineUp<?>> lineUps;
        private int inningsPerTeam;
        private Integer oversPerInnings;
        private int numberOfScheduledDays;
        private Integer ballsPerInnings;
        private TimeZone timeZone;
        private ImmutableList<MatchEventListener> eventListeners = ImmutableList.emptyList();

        public @Nullable String matchID() {
            return matchID;
        }

        public @Nullable Instant scheduledStartTime() {
            return startTime;
        }

        public @Nullable ImmutableList<LineUp<?>> teamLineUps() {
            return lineUps;
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

        public @Nonnull Builder withTeamLineUps(ImmutableList<LineUp<?>> lineUps) {
            this.lineUps = lineUps;
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

        /**
         * The time zone that this match is played in
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
            String matchID = requireNonNullElseGet(this.matchID, () -> UUID.randomUUID().toString());
            return new MatchStartingEvent(id(), generatedBy(), matchID, time(), startTime, lineUps,
                inningsPerTeam, oversPerInnings, numberOfScheduledDays, bpi, this.timeZone, eventListeners, customData());
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
                Objects.equals(startTime, builder.startTime) &&
                Objects.equals(lineUps, builder.lineUps) &&
                Objects.equals(oversPerInnings, builder.oversPerInnings) &&
                Objects.equals(ballsPerInnings, builder.ballsPerInnings) &&
                Objects.equals(timeZone, builder.timeZone) &&
                Objects.equals(eventListeners, builder.eventListeners);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), matchID, startTime, lineUps, inningsPerTeam, oversPerInnings, numberOfScheduledDays, ballsPerInnings, timeZone, eventListeners);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "matchID='" + matchID + '\'' +
                ", startTime=" + startTime +
                ", lineUps=" + lineUps +
                ", inningsPerTeam=" + inningsPerTeam +
                ", oversPerInnings=" + oversPerInnings +
                ", numberOfScheduledDays=" + numberOfScheduledDays +
                ", ballsPerInnings=" + ballsPerInnings +
                ", timeZone=" + timeZone +
                ", eventListeners=" + eventListeners +
                "} " + super.toString();
        }
    }
}
