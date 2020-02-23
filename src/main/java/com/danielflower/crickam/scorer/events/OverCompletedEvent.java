package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Over;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public final class OverCompletedEvent extends BaseMatchEvent {

    private final Over over;

    private OverCompletedEvent(String id, Instant time, MatchEvent generatedBy, Over over) {
        super(id, time, generatedBy);
        this.over = requireNonNull(over, "over");
    }

    /**
     * @return The over that this event completes
     */
    public Over over() {
        return over;
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverCompletedEvent> {

        public OverCompletedEvent build(Match match) {
            Over over = match.currentInnings()
                .orElseThrow(() -> new IllegalStateException("Cannot complete an over when there is no innings in progress"))
                .currentOver()
                .orElseThrow(() -> new IllegalStateException("Cannot complete an over when there is no over in progress"));
            return new OverCompletedEvent(id(), time(), generatedBy(), over);
        }

    }
}
