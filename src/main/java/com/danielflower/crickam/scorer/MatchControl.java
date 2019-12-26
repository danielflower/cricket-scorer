package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.MatchEvent;
import com.danielflower.crickam.scorer.events.MatchEventBuilder;
import com.danielflower.crickam.scorer.events.MatchStartingEvent;

import java.util.ArrayList;
import java.util.List;

public final class MatchControl {

    private final List<Match> matches = new ArrayList<>();

    private MatchControl(Match match) {
        matches.add(match);
    }

    public static MatchControl newMatch(MatchStartingEvent match) {
        return new MatchControl(new Match(match, new ImmutableList<>()));
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
