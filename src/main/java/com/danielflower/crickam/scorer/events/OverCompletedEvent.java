package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;
import java.util.Optional;

public final class OverCompletedEvent implements MatchEvent {

    private final Instant time;

    private OverCompletedEvent(Instant time) {
        this.time = time;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public static final class Builder implements MatchEventBuilder<OverCompletedEvent> {

        private Instant time;

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }
        public OverCompletedEvent build(Match match) {
            return new OverCompletedEvent(time);
        }

    }
}
