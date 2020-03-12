package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public abstract class BaseMatchEvent implements MatchEvent {
    private final String id;
    private final Instant time;
    private final String generatedBy;
    private final ImmutableList<MatchEventBuilder<?, ?>> generatedEvents;

    protected BaseMatchEvent(String id, Instant time, String generatedBy) {
        this(id, time, generatedBy, ImmutableList.emptyList());
    }
    protected BaseMatchEvent(String id, Instant time, String generatedBy, ImmutableList<MatchEventBuilder<?,?>> generatedEvents) {
        this.id = requireNonNull(id, "id");
        this.time = time;
        this.generatedBy = generatedBy;
        this.generatedEvents = requireNonNull(generatedEvents, "generatedEvents");
    }

    @Override
    public final Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final ImmutableList<MatchEventBuilder<?,?>> generatedEvents() {
        return this.generatedEvents;
    }

    @Override
    public final Optional<String> generatedBy() {
        return Optional.ofNullable(generatedBy);
    }
}
