package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Immutable
public abstract class BaseMatchEvent implements MatchEvent {
    private final String id;
    private final Instant time;
    private final String generatedBy;
    private final ImmutableList<MatchEventBuilder<?, ?>> generatedEvents;
    private final Object customData;

    protected BaseMatchEvent(String id, @Nullable Instant time, @Nullable String generatedBy, @Nullable Object customData) {
        this(id, time, generatedBy, customData, ImmutableList.emptyList());
    }
    protected BaseMatchEvent(String id, @Nullable Instant time, @Nullable String generatedBy, @Nullable Object customData, ImmutableList<MatchEventBuilder<?,?>> generatedEvents) {
        this.id = requireNonNull(id, "id");
        this.time = time;
        this.generatedBy = generatedBy;
        this.generatedEvents = requireNonNull(generatedEvents, "generatedEvents");
        this.customData = customData;
    }

    @Override
    public final @Nullable Instant time() {
        return time;
    }

    @Override
    public final @Nonnull String id() {
        return id;
    }

    @Override
    public final @Nonnull ImmutableList<MatchEventBuilder<?,?>> generatedEvents() {
        return this.generatedEvents;
    }

    @Override
    public final @Nullable String generatedBy() {
        return generatedBy;
    }

    @Override
    public @Nullable Object customData() {
        return customData;
    }
}
