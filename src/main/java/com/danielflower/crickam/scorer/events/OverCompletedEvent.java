package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Match;

import java.time.Instant;

public final class OverCompletedEvent extends BaseMatchEvent {

    private OverCompletedEvent(String id, Instant time, MatchEvent generatedBy) {
        super(id, time, generatedBy);
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverCompletedEvent> {

        public OverCompletedEvent build(Match match) {
            return new OverCompletedEvent(id(), time(), generatedBy());
        }

    }
}
