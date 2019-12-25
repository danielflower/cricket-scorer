package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEventBuilder;

import java.util.ArrayList;
import java.util.List;

public final class MatchControl {

    private final List<Match> matches = new ArrayList<>();

    public MatchControl(Match match) {
        matches.add(match);
    }

    public Match onEvent(MatchEventBuilder<?> eventBuilder) {
        return onEvent(eventBuilder.build());
    }

    public Match onEvent(MatchEvent event) {
        Match match = current().onEvent(event);
        matches.add(match);
        return match;
    }

    public Match current() {
        return matches.get(matches.size() - 1);
    }

}
