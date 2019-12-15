package com.danielflower.crickam.scorer;

import java.util.ArrayList;
import java.util.List;

public class MatchControl {

    private final List<Match> matches = new ArrayList<>();

    public MatchControl(Match match) {
        matches.add(match);
    }

    public void onEvent(MatchEvent event) {
        matches.add(current().onEvent(event));
    }

    public Match current() {
        return matches.get(matches.size() - 1);
    }

}
