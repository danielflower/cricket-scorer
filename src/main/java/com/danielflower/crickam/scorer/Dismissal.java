package com.danielflower.crickam.scorer;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Dismissal {
    private final DismissalType type;
    public final BatsmanInnings batter;
    private final Player bowler;
    private final Player executor;

    Dismissal(DismissalType type, BatsmanInnings batter, Player bowler, Player executor) {
        this.type = requireNonNull(type);
        this.batter = requireNonNull(batter);
        this.bowler = requireNonNull(bowler);
        this.executor = executor;
    }

    public DismissalType type() {
        return type;
    }

    public Player bowler() {
        return bowler;
    }

    /**
     * @return The player that caught the ball if the {@link #type()} is {@link DismissalType#Caught}, or the fielder
     * the enacted the runout or stumping. It will be an empty value for dismissal types such as {@link DismissalType#Bowled} etc
     */
    public Optional<Player> executor() {
        return Optional.ofNullable(executor);
    }

    public String toScorecardString() {
        String bowler = this.bowler == null ? null : this.bowler().familyName();
        String fielder = this.executor == null ? null : this.executor.familyName();
        switch (type) {
            case Bowled:
                return "b " + bowler;
            case Caught:
                String catcher = (this.executor == this.bowler()) ? "&" : fielder;
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


