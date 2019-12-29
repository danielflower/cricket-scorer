package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.DismissalType;
import com.danielflower.crickam.scorer.MatchType;
import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.Score;

/**
 * Some helper methods to create {@link MatchEventBuilder}s for {@link MatchEvent}s
 */
public final class MatchEvents {

    /**
     * @return A builder with no values set.
     */
    public static BallCompletedEvent.Builder ballCompleted() {
        return new BallCompletedEvent.Builder();
    }

    /**
     * Creates a ball based on a text score such as &quot;1&quot;, &quot;W&quot etc
     * <p>For the case where a wicket was taken, call {@link BallCompletedEvent.Builder#withDismissal(DismissalType)}
     * on the returned builder.</p>
     * @param scoreText a string allowed by {@link Score#parse(String)}
     * @return A BallCompletedEvent builder
     * @throws IllegalArgumentException if {@code scoreText} was not a valid score
     */
    public static BallCompletedEvent.Builder ballCompleted(String scoreText) {
        Score score = Score.parse(scoreText).orElseThrow(() -> new IllegalArgumentException("Unknown score: " + scoreText));
        return ballCompleted()
            .withRunsScored(score);
    }

    public static BatterInningsStartingEvent.Builder batterInningsStarting() {
        return new BatterInningsStartingEvent.Builder();
    }

    public static InningsCompletedEvent.Builder inningsCompleted() {
        return new InningsCompletedEvent.Builder();
    }

    public static InningsStartingEvent.Builder inningsStarting() {
        return new InningsStartingEvent.Builder();
    }

    public static MatchCompletedEvent.Builder matchCompleted() {
        return new MatchCompletedEvent.Builder();
    }

    public static MatchStartingEvent.Builder matchStarting() {
        return new MatchStartingEvent.Builder();
    }

    public static MatchStartingEvent.Builder matchStarting(MatchType matchType) {
        MatchStartingEvent.Builder builder = matchStarting().withMatchType(matchType);
        switch (matchType) {
            case TEST:
            case FIRST_CLASS:
                return builder.withNumberOfInningsPerTeam(2).withNumberOfScheduledDays(5);
            case ODI:
            case ONE_DAY:
                return builder.withNumberOfInningsPerTeam(1).withNumberOfScheduledDays(1).withOversPerInnings(50);
            case T20I:
            case T20:
                return builder.withNumberOfInningsPerTeam(1).withNumberOfScheduledDays(1).withOversPerInnings(20);
        }
        return builder;
    }

    public static OverCompletedEvent.Builder overCompleted() {
        return new OverCompletedEvent.Builder();
    }

    public static OverStartingEvent.Builder overStarting() {
        return new OverStartingEvent.Builder();
    }

    public static OverStartingEvent.Builder overStarting(Player bowler) {
        return overStarting().withBowler(bowler);
    }
}