package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Over;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Immutable
public final class OverCompletedEvent extends BaseMatchEvent {

    private final Over over;

    private OverCompletedEvent(UUID id, @Nullable Instant time, @Nullable UUID generatedBy, Over over, @Nullable Object customData) {
        super(id, time, generatedBy, customData);
        this.over = requireNonNull(over, "over");
    }

    /**
     * @return The over that this event completes
     */
    public Over over() {
        return over;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return new Builder()
            .withID(id())
            .withTime(time())
            .withGeneratedBy(generatedBy())
            ;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverCompletedEvent> {

        @Nonnull
        public OverCompletedEvent build(Match match) {
            if (match.currentInnings() == null) throw new IllegalStateException("Cannot complete an over when there is no innings in progress");
            Over over = match.currentInnings().currentOver();
            if (over == null) throw new IllegalStateException("Cannot complete an over when there is no over in progress");
            return new OverCompletedEvent(id(), time(), generatedBy(), over, customData());
        }

    }
}
