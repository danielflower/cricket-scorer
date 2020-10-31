package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Immutable
public abstract class BaseMatchEvent implements MatchEvent {
    private final UUID id;
    private final Instant time;
    private final UUID generatedBy;
    private final ImmutableList<MatchEventBuilder<?, ?>> generatedEvents;
    private final Object customData;

    protected BaseMatchEvent(UUID id, @Nullable Instant time, @Nullable UUID generatedBy, @Nullable Object customData) {
        this(id, time, generatedBy, customData, ImmutableList.emptyList());
    }
    protected BaseMatchEvent(UUID id, @Nullable Instant time, @Nullable UUID generatedBy, @Nullable Object customData, ImmutableList<MatchEventBuilder<?,?>> generatedEvents) {
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
    public final @Nonnull UUID id() {
        return id;
    }

    @Override
    public final @Nonnull ImmutableList<MatchEventBuilder<?,?>> generatedEvents() {
        return this.generatedEvents;
    }

    @Override
    public final @Nullable UUID generatedBy() {
        return generatedBy;
    }

    @Override
    public @Nullable Object customData() {
        return customData;
    }
}
