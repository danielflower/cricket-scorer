package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.UUID;

abstract class BaseMatchEventBuilder<B extends MatchEventBuilder<B,T>, T extends MatchEvent> implements MatchEventBuilder<B, T> {

    private String id = UUID.randomUUID().toString();
    private MatchEvent generatedBy;
    private Instant time;

    public MatchEvent generatedBy() {
        return generatedBy;
    }

    public Instant time() {
        return time;
    }

    public String id() {
        return id;
    }

    @Override
    public final B withID(String id) {
        this.id = id;
        return (B)this;
    }

    @Override
    public final B withGeneratedBy(MatchEvent generatedBy) {
        this.generatedBy = generatedBy;
        return (B) this;
    }

    @Override
    public final B withTime(Instant time) {
        this.time = time;
        return (B) this;
    }
}
