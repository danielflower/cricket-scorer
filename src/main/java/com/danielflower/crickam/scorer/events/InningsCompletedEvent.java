package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Optional;

public final class InningsCompletedEvent implements MatchEvent {

    private final Instant time;

    private InningsCompletedEvent(Instant time) {
        this.time = time;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public final static class Builder implements MatchEventBuilder<InningsCompletedEvent> {
        private Instant time;

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
