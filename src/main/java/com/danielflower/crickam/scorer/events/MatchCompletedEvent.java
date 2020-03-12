package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.MatchResult;

import java.time.Instant;
import java.util.Objects;

/**
 * An event that signifies a match is completed. No more events should occur after this.
 */
public final class MatchCompletedEvent extends BaseMatchEvent {

    private final MatchResult result;

    private MatchCompletedEvent(String id, Instant time, String generatedBy, MatchResult result) {
        super(id, time, generatedBy);
        this.result = Objects.requireNonNull(result, "result");
    }

    public MatchResult result() {
        return result;
    }

    @Override
    public Builder newBuilder() {
        return new Builder()
            .withResult(result)
            .withID(id())
            .withTime(time().orElse(null))
            .withGeneratedBy(generatedBy().orElse(null))
            ;
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, MatchCompletedEvent> {

        private MatchResult result;

        public MatchResult result() {
            return result;
        }

        /**
         * @param result The result of the match, or leave unset to have the library infer the winner
         * @return This builder
         */
        public Builder withResult(MatchResult result) {
            this.result = result;
            return this;
        }

        public MatchCompletedEvent build(Match match) {
            MatchResult toUse = this.result;
            if (toUse == null) {
                toUse = match.calculateResult();
            }
            return new MatchCompletedEvent(id(), time(), generatedBy(), toUse);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return Objects.equals(result, builder.result);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), result);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "result=" + result +
                "} " + super.toString();
        }
    }
}
