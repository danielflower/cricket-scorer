package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Some helper methods to create {@link MatchEventBuilder}s for {@link MatchEvent}s
 */
public final class MatchEvents {

    /**
     * Creates a builder for a new match event
     * @return A new builder
     * @see #matchStarting(int, Integer)
     */
    public static MatchStartingEvent.Builder matchStarting() {
        return new MatchStartingEvent.Builder();
    }
    /**
     * Creates a builder for a new match event
     * <p>Example parameters:</p>
     * <ul>
     *     <li><strong>5, null</strong> for a test match</li>
     *     <li><strong>4, null</strong> for a four day match</li>
     *     <li><strong>1, 50</strong> for an ODI</li>
     *     <li><strong>1, 20</strong> for a T20</li>
     * </ul>
     * @param scheduledDays The scheduled number of days for the match. Use <code>1</code> for limited overs cricket
     * @param oversPerInnings The maximum number of overs per innings. Use <code>null</code> for first class cricket
     * @return A new MatchStartingEvent builder
     */
    public static MatchStartingEvent.Builder matchStarting(int scheduledDays, @Nullable Integer oversPerInnings) {
        return oversPerInnings == null ? MatchStartingEvent.firstClass(scheduledDays) : MatchStartingEvent.limitedOvers(oversPerInnings);
    }

    public static InningsStartingEvent.Builder inningsStarting() {
        return new InningsStartingEvent.Builder();
    }

    public static BatterInningsStartingEvent.Builder batterInningsStarting() {
        return new BatterInningsStartingEvent.Builder();
    }

    public static OverStartingEvent.Builder overStarting() {
        return new OverStartingEvent.Builder();
    }

    /**
     * @return A builder with no values set.
     */
    public static BallCompletedEvent.Builder ballCompleted() {
        return new BallCompletedEvent.Builder();
    }

    /**
     * Creates a ball based on a text score such as &quot;1&quot;, &quot;W&quot; etc
     * <p>For the case where a wicket was taken, call {@link BallCompletedEvent.Builder#withDismissal(DismissalType)}
     * on the returned builder.</p>
     *
     * @param scoreText a string allowed by {@link Score#parse(String)}
     * @return A BallCompletedEvent builder
     * @throws IllegalArgumentException if {@code scoreText} was not a valid score
     */
    public static BallCompletedEvent.Builder ballCompleted(String scoreText) {
        Score score = Score.parse(scoreText);
        if (score == null) throw new IllegalArgumentException("Unknown score: " + scoreText);
        return ballCompleted().withRunsScored(score);
    }

    public static OverStartingEvent.Builder overStarting(Player bowler) {
        return overStarting().withBowler(bowler);
    }

    public static OverCompletedEvent.Builder overCompleted() {
        return new OverCompletedEvent.Builder();
    }

    public static BatterInningsCompletedEvent.Builder batterInningsCompleted() {
        return new BatterInningsCompletedEvent.Builder();
    }

    public static BatterInningsCompletedEvent.Builder batterInningsCompleted(BattingState reason) {
        return batterInningsCompleted().withReason(reason);
    }

    public static InningsCompletedEvent.Builder inningsCompleted() {
        return new InningsCompletedEvent.Builder();
    }

    public static MatchCompletedEvent.Builder matchCompleted() {
        return new MatchCompletedEvent.Builder();
    }


}
