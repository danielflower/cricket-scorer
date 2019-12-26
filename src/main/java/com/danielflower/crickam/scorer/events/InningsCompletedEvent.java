package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Objects;

public final class InningsCompletedEvent implements MatchEvent {

    private final Instant time;

    private InningsCompletedEvent(Instant time) {
        this.time = Objects.requireNonNull(time);
    }

    @Override
    public Instant time() {
        return time;
    }

    public final static class Builder {
        private Instant time = Instant.now();

        public InningsCompletedEvent build() {
            return new InningsCompletedEvent(time);
        }

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }
    }

    public static Builder inningsCompleted() {
        return new Builder();
    }
}
