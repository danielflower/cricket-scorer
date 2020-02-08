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

    private MatchCompletedEvent(String id, Instant time, MatchEvent generatedBy, MatchResult result) {
        super(id, time, generatedBy);
        this.result = Objects.requireNonNull(result, "result");
    }

    public MatchResult result() {
        return result;
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, MatchCompletedEvent> {

        private MatchResult result;

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

    }
}
