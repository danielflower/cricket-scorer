package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;

import java.time.Instant;

public class InningsCompletedEvent implements MatchEvent {

    private final Instant time;

    private InningsCompletedEvent(Instant time) {
        this.time = time;
    }

    public Instant time() {
        return time;
    }

    public static class Builder {
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
