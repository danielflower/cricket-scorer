package com.danielflower.crickam.scorer;

public enum DismissalType {
	Bowled(true),
	TimedOut(false),
	Caught(true),
	HandledTheBall(false),
	HitTheBallTwice(false),
	HitWicket(true),
	LegBeforeWicket(true),
	ObstructingTheField(false),
	RunOut(false),
	Stumped(true);

	private final boolean bowler;

    DismissalType(boolean bowler) {
        this.bowler = bowler;
    }

    public boolean creditedToBowler() {
        return bowler;
    }

    public boolean creditedToWicketKeeper() {
        return this == Stumped;
    }
}

