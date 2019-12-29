package com.danielflower.crickam.scorer;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A dismissal of a batter
 */
public final class Dismissal {
    private final DismissalType type;
    private final Player batter;
    private final Player bowler;
    private final Player fielder;

    public Dismissal(DismissalType type, Player batter, Player bowler, Player fielder) {
        this.type = requireNonNull(type);
        this.batter = requireNonNull(batter);
        if (type.creditedToBowler()) {
            this.bowler = requireNonNull(bowler, "For dismissals of type " + type + " the bowler must be specified");
        } else {
            this.bowler = null;
        }
        this.fielder = fielder;
    }

    /**
     * @return The batter who was dismissed
     */
    public Player batter() {
        return batter;
    }

    /**
     * @return The mode of dismissal
     */
    public DismissalType type() {
        return type;
    }

    /**
     * @return The bowler credited with the dismissal, or empty if {@link DismissalType#creditedToBowler()} is false
     */
    public Optional<Player> bowler() {
        return Optional.ofNullable(bowler);
    }

    /**
     * @return The player that caught the ball if the {@link #type()} is {@link DismissalType#CAUGHT}, or the fielder
     * the enacted the runout or stumping. It will be an empty value for dismissal types such as {@link DismissalType#BOWLED} etc
     */
    public Optional<Player> fielder() {
        return Optional.ofNullable(fielder);
    }

    /**
     * @param wicketKeeper If specified, the wicket keeper will be marked with a &quot;†&quot; symbol if this is caught
     * @return A string representation of this dismissal in the style commonly found on scorecards, for example <em>c Williamson b Boult</em>
     */
    public String toScorecardString(@Nullable Player wicketKeeper) {
        String bowler = this.bowler == null ? null : this.bowler.familyName();
        String fielder = this.fielder == null ? null : this.fielder.familyName();
        switch (type) {
            case BOWLED:
                return "b " + bowler;
            case CAUGHT:
                String catcher = (this.fielder == this.bowler) ? "&" : fielder;
                String prefix = this.fielder == wicketKeeper ? "†" : "";
                return "c " + prefix + catcher + " b " + bowler;
            case HIT_WICKET:
                return "hw " + bowler;
            case LEG_BEFORE_WICKET:
                return "lbw b " + bowler;
            case RUN_OUT:
                return fielder == null ? "run out" : "run out (" + fielder + ")";
            case STUMPED:
                return "st " + fielder + " b " + bowler;
        }
        return type.toString().toLowerCase().replace('_', ' ');
    }

    @Override
    public String toString() {
        return toScorecardString(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dismissal dismissal = (Dismissal) o;
        return type == dismissal.type &&
            batter.equals(dismissal.batter) &&
            Objects.equals(bowler, dismissal.bowler) &&
            Objects.equals(fielder, dismissal.fielder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, batter, bowler, fielder);
    }
}


