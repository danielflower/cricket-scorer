package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Optional;

public final class MatchCompletedEvent implements MatchEvent {

    private final Instant time;

    private MatchCompletedEvent(Instant time) {
        this.time = time;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public static Builder matchCompleted() {
        return new Builder();
    }

    public final static class Builder implements MatchEventBuilder<MatchCompletedEvent> {

        private Instant time;

        public MatchCompletedEvent build() {
            return new MatchCompletedEvent(time);
        }

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

    }
}
