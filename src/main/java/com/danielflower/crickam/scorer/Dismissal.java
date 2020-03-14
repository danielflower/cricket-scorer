package com.danielflower.crickam.scorer;

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

    private Dismissal(DismissalType type, Player batter, Player bowler, Player fielder) {
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
     * @param team If specified, the wicket keeper will be marked with a &quot;†&quot; symbol if this is caught
     * @return A string representation of this dismissal in the style commonly found on scorecards, for example <em>c Williamson b Boult</em>
     */
    public String toScorecardString(LineUp team) {
        String bowlerName = this.bowler == null ? null : this.bowler.familyName();
        String fielderName;
        if (this.fielder == null) {
            fielderName = null;
        } else {
            fielderName = this.fielder.familyName();
            if (team != null) {
                if (team.wicketKeeper().equals(this.fielder)) {
                    fielderName = "†" + fielderName;
                } else if (!team.battingOrder().contains(this.fielder)) {
                    fielderName = "sub (" + fielderName + ")";
                }
            }
        }

        switch (type) {
            case BOWLED:
                return "b " + bowlerName;
            case CAUGHT:
                boolean cab = this.fielder == this.bowler;
                return cab ? "c & b " + fielderName : "c " + fielderName + " b " + bowlerName;
            case HIT_WICKET:
                return "hw " + bowlerName;
            case LEG_BEFORE_WICKET:
                return "lbw b " + bowlerName;
            case RUN_OUT:
                return fielderName == null ? "run out" : "run out (" + fielderName + ")";
            case STUMPED:
                return "st " + fielderName + " b " + bowlerName;
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

    public static Builder dismissal() {
        return new Builder();
    }

    public static final class Builder {

        private DismissalType type;
        private Player batter;
        private Player bowler;
        private Player fielder;

        public Builder withType(DismissalType type) {
            this.type = type;
            return this;
        }

        public Builder withBatter(Player batter) {
            this.batter = batter;
            return this;
        }

        /**
         * Specifies the bowler credit for the dismissal. Leave unset for types which return code from {@link DismissalType#creditedToBowler()}
         *
         * @param bowler The bowler credited with the dismissal.
         * @return This builder
         */
        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param fielder The fielder who caught the ball, or ran out the batter, or stumped the batter. Leave unset if no fielder for this dismissal.
         * @return This builder
         */
        public Builder withFielder(Player fielder) {
            this.fielder = fielder;
            return this;
        }

        public Dismissal build() {
            return new Dismissal(type, batter, bowler, fielder);
        }
    }

}


