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
    private final Player executor;

    Dismissal(DismissalType type, Player batter, Player bowler, Player executor) {
        this.type = requireNonNull(type);
        this.batter = requireNonNull(batter);
        this.bowler = requireNonNull(bowler);
        this.executor = executor;
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
     * @return The bowler
     */
    public Player bowler() {
        return bowler;
    }

    /**
     * @return The player that caught the ball if the {@link #type()} is {@link DismissalType#CAUGHT}, or the fielder
     * the enacted the runout or stumping. It will be an empty value for dismissal types such as {@link DismissalType#BOWLED} etc
     */
    public Optional<Player> executor() {
        return Optional.ofNullable(executor);
    }

    /**
     * @param wicketKeeper If specified, the wicket keeper will be marked with a &quot;†&quot; symbol if this is caught
     * @return A string representation of this dismissal in the style commonly found on scorecards, for example <em>c Williamson b Boult</em>
     */
    public String toScorecardString(@Nullable Player wicketKeeper) {
        String bowler = this.bowler == null ? null : this.bowler().familyName();
        String fielder = this.executor == null ? null : this.executor.familyName();
        switch (type) {
            case BOWLED:
                return "b " + bowler;
            case CAUGHT:
                String catcher = (this.executor == this.bowler()) ? "&" : fielder;
                String prefix = this.executor == wicketKeeper ? "†" : "";
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
            bowler.equals(dismissal.bowler) &&
            Objects.equals(executor, dismissal.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, batter, bowler, executor);
    }
}


