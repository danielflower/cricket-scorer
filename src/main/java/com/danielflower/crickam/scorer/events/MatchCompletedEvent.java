package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.MatchResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * An event that signifies a match is completed. No more events should occur after this.
 */
@Immutable
public final class MatchCompletedEvent extends BaseMatchEvent {

    private final MatchResult result;

    private MatchCompletedEvent(UUID id, @Nullable Instant time, @Nullable UUID generatedBy, MatchResult result, @Nullable Object customData) {
        super(id, time, generatedBy, customData);
        this.result = Objects.requireNonNull(result, "result");
    }

    public @Nonnull MatchResult result() {
        return result;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return new Builder()
            .withResult(result)
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, MatchCompletedEvent> {

        private MatchResult result;

        public @Nullable MatchResult result() {
            return result;
        }

        /**
         * @param result The result of the match, or leave unset to have the library infer the winner
         * @return This builder
         */
        public @Nonnull Builder withResult(MatchResult result) {
            this.result = result;
            return this;
        }

        @Nonnull
        public MatchCompletedEvent build(Match match) {
            MatchResult toUse = this.result;
            if (toUse == null) {
                toUse = match.calculateResult();
            }
            return new MatchCompletedEvent(id(), time(), generatedBy(), toUse, customData());
        }

        @Override
        public boolean equals(@Nullable Object o) {
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
