package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;
import java.util.Optional;

public final class InningsCompletedEvent implements MatchEvent {

    private final Instant time;
    private final boolean declared;
    private final int inningsNumber;

    private InningsCompletedEvent(Instant time, boolean declared, int inningsNumber) {
        this.time = time;
        this.declared = declared;
        this.inningsNumber = inningsNumber;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public int inningsNumber() {
        return inningsNumber;
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

        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        public Builder withDeclared(boolean declared) {
            this.declared = declared;
            return this;
        }

        public InningsCompletedEvent build(Match match) {
            int inningsNumber = match.inningsList().size();
            return new InningsCompletedEvent(time, declared, inningsNumber);
        }
    }

}
