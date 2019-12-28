package com.danielflower.crickam.scorer;

public enum DismissalType {
	BOWLED(true),
	TIMED_OUT(false),
	CAUGHT(true),
	HANDLED_THE_BALL(false),
	HIT_THE_BALL_TWICE(false),
	HIT_WICKET(true),
	LEG_BEFORE_WICKET(true),
	OBSTRUCTING_THE_FIELD(false),
	RUN_OUT(false),
	STUMPED(true);

	private final boolean bowler;

    DismissalType(boolean bowler) {
        this.bowler = bowler;
    }

    public boolean creditedToBowler() {
        return bowler;
    }

    public boolean creditedToWicketKeeper() {
        return this == STUMPED;
    }
}

