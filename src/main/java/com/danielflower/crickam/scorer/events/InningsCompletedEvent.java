package com.danielflower.crickam.scorer.events;

import java.time.Instant;
import java.util.Optional;

public final class InningsCompletedEvent implements MatchEvent {

    private final Instant time;
    private final boolean declared;

    private InningsCompletedEvent(Instant time, boolean declared) {
        this.time = time;
        this.declared = declared;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    /**
     * @return True if the batting team declared
     */
    public boolean declared() {
        return declared;
    }

    public final static class Builder implements MatchEventBuilder<InningsCompletedEvent> {
        private Instant time;
        private boolean declared;

        public InningsCompletedEvent build() {
            return new InningsCompletedEvent(time, declared);
        }

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        public Builder withDeclared(boolean declared) {
            this.declared = declared;
            return this;
        }
    }

    public static Builder inningsCompleted() {
        return new Builder();
    }
}
