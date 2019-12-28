package com.danielflower.crickam.scorer;

public enum DismissalType {
	BOWLED(true, "b"),
	TIMED_OUT(false, "to"),
	CAUGHT(true, "c"),
	HANDLED_THE_BALL(false, "hb"),
	HIT_THE_BALL_TWICE(false, "ht"),
	HIT_WICKET(true, "hw"),
	LEG_BEFORE_WICKET(true, "lbw"),
	OBSTRUCTING_THE_FIELD(false, "of"),
	RUN_OUT(false, "ro"),
	STUMPED(true, "st");

	private final boolean bowler;
    private final String abbreviation;

    DismissalType(boolean bowler, String abbreviation) {
        this.bowler = bowler;
        this.abbreviation = abbreviation;
    }

    /**
     * @return The abbreviation commonly used on scorecards, for example &quot;b&quot; for bowled.
     */
    public String abbreviation() {
        return abbreviation;
    }

    /**
     * Looks up a dismissal based on its abbreviation
     * @param abbreviation An abbreviation such as &quot;lbw&quot;
     * @return The matching dismissal type
     * @throws IllegalArgumentException No type with that abbreviation exists
     */
    public static DismissalType fromAbbreviation(String abbreviation) {
        for (DismissalType di : DismissalType.values()) {
            if (di.abbreviation().equalsIgnoreCase(abbreviation)) {
                return di;
            }
        }
        throw new IllegalArgumentException("No dismissal abbreviation " + abbreviation);
    }

    public boolean creditedToBowler() {
        return bowler;
    }

    public boolean creditedToWicketKeeper() {
        return this == STUMPED;
    }
}

