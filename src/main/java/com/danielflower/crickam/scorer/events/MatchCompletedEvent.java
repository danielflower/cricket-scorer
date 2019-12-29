package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.MatchResult;

import java.time.Instant;
import java.util.Optional;

public final class MatchCompletedEvent implements MatchEvent {

    private final Instant time;
    private final MatchResult result;

    private MatchCompletedEvent(Instant time, MatchResult result) {
        this.time = time;
        this.result = result;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    public Optional<MatchResult> result() {
        return Optional.ofNullable(result);
    }

    public final static class Builder implements MatchEventBuilder<MatchCompletedEvent> {

        private Instant time;
        private MatchResult result;

        /**
         * @param time The time the match was declared completed
         * @return This builder
         */
        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        /**
         * @param result The result of the match, or null to have the library infer the winner
         * @return This builder
         */
        public Builder withResult(MatchResult result) {
            this.result = result;
            return this;
        }

        public MatchCompletedEvent build(Match match) {
            return new MatchCompletedEvent(time, result);
        }

    }
}
