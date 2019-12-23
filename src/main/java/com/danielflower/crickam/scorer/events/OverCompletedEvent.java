package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;

public class OverCompletedEvent implements MatchEvent {

    private OverCompletedEvent() {
    }

    public static class Builder {
        public OverCompletedEvent build() {
            return new OverCompletedEvent();
        }
    }

    public static Builder overCompleted() {
        return new Builder();
    }
}
