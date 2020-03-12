package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class BaseMatchEventBuilder<B extends MatchEventBuilder<B,T>, T extends MatchEvent> implements MatchEventBuilder<B, T> {

    private String id = UUID.randomUUID().toString();
    private String generatedBy;
    private Instant time;

    @Override
    public String generatedBy() {
        return generatedBy;
    }

    @Override
    public Instant time() {
        return time;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public final B withID(String id) {
        this.id = id;
        return (B)this;
    }

    @Override
    public final B withGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
        return (B) this;
    }

    @Override
    public final B withTime(Instant time) {
        this.time = time;
        return (B) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMatchEventBuilder<?, ?> that = (BaseMatchEventBuilder<?, ?>) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(generatedBy, that.generatedBy) &&
            Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, generatedBy, time);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + id + '\'' +
            ", generatedBy=" + generatedBy +
            ", time=" + time +
            '}';
    }
}
