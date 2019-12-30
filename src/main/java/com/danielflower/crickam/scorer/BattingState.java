package com.danielflower.crickam.scorer;

public enum BattingState {
    /**
     * The innings is in progress
     */
    IN_PROGRESS,

    /**
     * The batter retired due to illness, injury, or some other unavoidable reason, and may bat again.
     */
    RETIRED,

    /**
     * The batter chose to retire, so cannot resume their innings (unless the opposition caption agrees)
     */
    RETIRED_OUT,

    /**
     * The batter was dismissed
     */
    DISMISSED,

    /**
     * The batter was not out when the team's innings ended
     */
    INNINGS_ENDED
}
