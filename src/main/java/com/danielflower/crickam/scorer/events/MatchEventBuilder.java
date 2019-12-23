package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.MatchEvent;

public interface MatchEventBuilder<T extends MatchEvent> {

    T build();

}
