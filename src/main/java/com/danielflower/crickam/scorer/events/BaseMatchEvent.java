package com.danielflower.crickam.scorer.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Immutable
public abstract class BaseMatchEvent implements MatchEvent {
    private final UUID id;
    private final Instant time;
    private final Object customData;
    private final boolean undoPoint;

    protected BaseMatchEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint) {
        this.id = requireNonNull(id, "id");
        this.time = time;
        this.customData = customData;
        this.undoPoint = undoPoint;
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
    public @Nullable Object customData() {
        return customData;
    }

    @Override
    public boolean undoPoint() {
        return undoPoint;
    }

    protected <T extends MatchEventBuilder<?,?>> T baseBuilder(T builder) {
        return (T)builder.withID(id())
            .withTime(time())
            .withCustomData(customData())
            .withUndoPoint(undoPoint());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMatchEvent that = (BaseMatchEvent) o;
        return Objects.equals(id, that.id) && Objects.equals(time, that.time)
            && Objects.equals(customData, that.customData) && Objects.equals(undoPoint, that.undoPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, customData, undoPoint);
    }

}
