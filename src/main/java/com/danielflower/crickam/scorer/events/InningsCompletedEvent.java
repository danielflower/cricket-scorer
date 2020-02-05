package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;

public final class InningsCompletedEvent extends BaseMatchEvent {

    private final boolean declared;
    private final int inningsNumber;

    private InningsCompletedEvent(Instant time, String id, MatchEvent generatedBy, boolean declared, int inningsNumber) {
        super(id, time, generatedBy);
        this.declared = declared;
        this.inningsNumber = inningsNumber;
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

    public final static class Builder extends BaseMatchEventBuilder<Builder, InningsCompletedEvent> {
        private boolean declared;

        public Builder withDeclared(boolean declared) {
            this.declared = declared;
            return this;
        }

        public InningsCompletedEvent build(Match match) {
            int inningsNumber = match.inningsList().size();
            return new InningsCompletedEvent(time(), id(), generatedBy(), declared, inningsNumber);
        }
    }

}
