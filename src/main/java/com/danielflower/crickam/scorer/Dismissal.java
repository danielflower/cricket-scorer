package com.danielflower.crickam.scorer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A dismissal of a batter
 */
@Immutable
public final class Dismissal {
    private final DismissalType type;
    private final Player batter;
    private final Player bowler;
    private final Player fielder;

    private Dismissal(DismissalType type, Player batter, @Nullable Player bowler, @Nullable Player fielder) {
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
    public @Nonnull Player batter() {
        return batter;
    }

    /**
     * @return The mode of dismissal
     */
    public @Nonnull DismissalType type() {
        return type;
    }

    /**
     * @return The bowler credited with the dismissal, or null if {@link DismissalType#creditedToBowler()} is false
     */
    public @Nullable Player bowler() {
        return bowler;
    }

    /**
     * @return The player that caught the ball if the {@link #type()} is {@link DismissalType#CAUGHT}, or the fielder
     * the enacted the runout or stumping. It will be null for dismissal types such as {@link DismissalType#BOWLED} etc
     */
    public @Nullable Player fielder() {
        return fielder;
    }

    /**
     * @param team If specified, the wicket keeper will be marked with a &quot;†&quot; symbol if this is caught
     * @return A string representation of this dismissal in the style commonly found on scorecards, for example <em>c Williamson b Boult</em>
     */
    public @Nonnull String toScorecardString(@Nullable LineUp team) {
        String bowlerName = this.bowler == null ? null : this.bowler.scorecardName();
        String fielderName;
        if (this.fielder == null) {
            fielderName = null;
        } else {
            fielderName = this.fielder.scorecardName();
            if (team != null) {
                if (team.wicketKeeper().samePlayer(this.fielder)) {
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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dismissal dismissal = (Dismissal) o;
        return type == dismissal.type &&
            batter.samePlayer(dismissal.batter) &&
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

        public @Nonnull Builder withType(@Nullable DismissalType type) {
            this.type = type;
            return this;
        }

        public @Nonnull Builder withBatter(@Nullable Player batter) {
            this.batter = batter;
            return this;
        }

        /**
         * Specifies the bowler credit for the dismissal. Leave unset for types which return code from {@link DismissalType#creditedToBowler()}
         *
         * @param bowler The bowler credited with the dismissal.
         * @return This builder
         */
        public @Nonnull Builder withBowler(@Nullable Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param fielder The fielder who caught the ball, or ran out the batter, or stumped the batter. Leave unset if no fielder for this dismissal.
         * @return This builder
         */
        public @Nonnull Builder withFielder(@Nullable Player fielder) {
            this.fielder = fielder;
            return this;
        }

        public @Nonnull Dismissal build() {
            return new Dismissal(type, batter, bowler, fielder);
        }
    }

}


