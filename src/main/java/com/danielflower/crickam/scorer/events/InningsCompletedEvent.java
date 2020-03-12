package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;
import java.util.Objects;

public final class InningsCompletedEvent extends BaseMatchEvent {

    private final boolean declared;
    private final int inningsNumber;

    private InningsCompletedEvent(Instant time, String id, String generatedBy, boolean declared, int inningsNumber) {
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

    @Override
    public Builder newBuilder() {
        return new Builder()
            .withDeclared(declared)
            .withID(id())
            .withTime(time().orElse(null))
            .withGeneratedBy(generatedBy().orElse(null))
            ;
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, InningsCompletedEvent> {
        private boolean declared;

        public boolean declared() {
            return declared;
        }

        public Builder withDeclared(boolean declared) {
            this.declared = declared;
            return this;
        }

        public InningsCompletedEvent build(Match match) {
            int inningsNumber = match.inningsList().size();
            return new InningsCompletedEvent(time(), id(), generatedBy(), declared, inningsNumber);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return declared == builder.declared;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), declared);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "declared=" + declared +
                "} " + super.toString();
        }
    }

}
