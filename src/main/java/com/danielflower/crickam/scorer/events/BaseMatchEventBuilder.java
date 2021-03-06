package com.danielflower.crickam.scorer.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class BaseMatchEventBuilder<B extends MatchEventBuilder<B,T>, T extends MatchEvent> implements MatchEventBuilder<B, T> {

    private UUID id = UUID.randomUUID();
    private Instant time;
    private Object customData;
    private boolean undoPoint = true;

    @Override
    public @Nullable Instant time() {
        return time;
    }

    @Override
    public @Nonnull UUID id() {
        return id;
    }

    @Override
    public @Nullable Object customData() { return customData; }

    @Override
    public boolean undoPoint() {
        return undoPoint;
    }

    @Nonnull
    @Override
    public final B withID(UUID id) {
        this.id = id;
        return (B)this;
    }

    @Nonnull
    @Override
    public final B withTime(@Nullable Instant time) {
        this.time = time;
        return (B) this;
    }

    @Override
    public @Nonnull B withCustomData(@Nullable Object customData) {
        this.customData = customData;
        return (B) this;
    }

    @Override
    public B withUndoPoint(boolean undoPoint) {
        this.undoPoint = undoPoint;
        return (B)this;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMatchEventBuilder<?, ?> that = (BaseMatchEventBuilder<?, ?>) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(customData, that.customData) &&
            Objects.equals(time, that.time) &&
            Objects.equals(undoPoint, that.undoPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customData, time, undoPoint);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + id + '\'' +
            ", customData=" + customData +
            ", time=" + time +
            ", undoPoint=" + undoPoint +
            '}';
    }
}
