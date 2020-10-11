package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;

@Immutable
public final class InningsCompletedEvent extends BaseMatchEvent {

    private final boolean declared;
    private final int inningsNumber;

    private InningsCompletedEvent(@Nullable Instant time, String id, @Nullable String generatedBy, boolean declared, @Nonnegative int inningsNumber, @Nullable Object customData) {
        super(id, time, generatedBy, customData);
        this.declared = declared;
        this.inningsNumber = inningsNumber;
    }


    public @Nonnegative int inningsNumber() {
        return inningsNumber;
    }

    /**
     * @return True if the batting team declared
     */
    public boolean declared() {
        return declared;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return new Builder()
            .withDeclared(declared)
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, InningsCompletedEvent> {
        private boolean declared;

        public boolean declared() {
            return declared;
        }

        public @Nonnull Builder withDeclared(boolean declared) {
            this.declared = declared;
            return this;
        }

        @Nonnull
        public InningsCompletedEvent build(Match match) {
            int inningsNumber = match.inningsList().size();
            return new InningsCompletedEvent(time(), id(), generatedBy(), declared, inningsNumber, customData());
        }

        @Override
        public boolean equals(@Nullable Object o) {
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
