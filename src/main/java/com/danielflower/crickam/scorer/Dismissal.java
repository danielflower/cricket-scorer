package com.danielflower.crickam.scorer;

public class Dismissal
{
    private final DismissalType type;
    public final BatsmanInnings batter;
    private final BowlingSpell bowler;
    private final Player executor;
    public Dismissal(DismissalType type, BatsmanInnings batter, BowlingSpell bowler, Player executor) {
        this.type = type;
        this.batter = batter;
        this.bowler = bowler;
        this.executor = executor;
    }

    public DismissalType getType() {
        return type;
    }

    public BowlingSpell bowlingSpell() {
        return bowler;
    }

    public Player getBowler() {
        return bowler.bowlerInnings().bowler();
    }

    public Player getExecutor() {
        return executor;
    }

    public String toScorecardString() {
        String bowler = this.bowler == null ? null : this.getBowler().familyName();
        String fielder = this.executor == null ? null : this.executor.familyName();
        switch (type) {
            case Bowled:
                return "b " + bowler;
            case Caught:
                String catcher = (this.executor == this.getBowler()) ? "&" : fielder;
                return "c " + catcher + " b " + bowler;
            case HitWicket:
                return "hw " + bowler;
            case LegBeforeWicket:
                return "lbw b " + bowler;
            case Retired:
                return "retired hurt";
            case RunOut:
                return "run out (" + fielder + ")";
            case Stumped:
                return "st " + fielder + " b " + bowler;
        }
        return type.toString();
    }

    @Override
    public String toString() {
        return toScorecardString();
    }
}


