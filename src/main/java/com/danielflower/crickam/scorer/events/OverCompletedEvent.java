package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Objects;

public final class OverCompletedEvent implements MatchEvent {

    private final Instant time;

    private OverCompletedEvent(Instant time) {
        this.time = Objects.requireNonNull(time);
    }

    @Override
    public Instant time() {
        return time;
    }

    public static Builder overCompleted() {
        return new Builder();
    }

    public static final class Builder {

        private Instant time = Instant.now();

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }
        public OverCompletedEvent build() {
            return new OverCompletedEvent(time);
        }

    }
}
