package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.BatterInnings;
import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.danielflower.crickam.scorer.Crictils.requireInRange;
import static java.util.Objects.requireNonNull;

public final class OverStartingEvent implements MatchEvent {

    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;
    private final Instant time;
    private final int ballsInOver;
    private final int overNumber;
    private final int inningsNumber;

    private OverStartingEvent(Player bowler, Player striker, Player nonStriker, Instant time, int ballsInOver, int overNumber, int inningsNumber) {
        this.bowler = requireNonNull(bowler, "bowler");
        this.striker = requireNonNull(striker, "striker");
        this.nonStriker = requireNonNull(nonStriker, "nonStriker");
        this.time = time;
        this.ballsInOver = requireInRange("ballsInOver", ballsInOver, 1);
        this.overNumber = overNumber;
        this.inningsNumber = inningsNumber;
    }

    public Player bowler() {
        return bowler;
    }

    public Player striker() {
        return striker;
    }

    public Player nonStriker() {
        return nonStriker;
    }

    public int ballsInOver() {
        return ballsInOver;
    }

    @Override
    public Optional<Instant> time() {
        return Optional.ofNullable(time);
    }

    /**
     * @return The zero-based index of this over in the current innings (i.e. the first over in an innings is 0)
     */
    public int overNumber() {
        return overNumber;
    }

    public int inningsNumber() {
        return inningsNumber;
    }

    public static final class Builder implements MatchEventBuilder<OverStartingEvent> {

        private Player bowler;
        private Player striker;
        private Player nonStriker;
        private Instant time;
        private int ballsInOver = 6;

        /**
         * @param bowler Specifies the bowler of this over. This must be set.
         * @return This builder
         */
        public Builder withBowler(Player bowler) {
            this.bowler = bowler;
            return this;
        }

        /**
         * @param ballsInOver The default number of balls per over. Defaults to 6 if unset.
         * @return This builder
         */
        public Builder withBallsInOver(int ballsInOver) {
            this.ballsInOver = ballsInOver;
            return this;
        }

        /**
         * @param striker The player who is on strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public Builder withStriker(Player striker) {
            this.striker = striker;
            return this;
        }

        /**
         * @param nonStriker The player who is off strike for this over, or null to guess based on the current state of the game.
         * @return This builder
         */
        public Builder withNonStriker(Player nonStriker) {
            this.nonStriker = nonStriker;
            return this;
        }

        /**
         * @param time The time that the over is starting (this may be when the bowler gets the ball, before the first ball is started).
         * @return This builder
         */
        public Builder withTime(Instant time) {
            this.time = time;
            return this;
        }

        public OverStartingEvent build(Match match) {
            Innings innings = match.currentInnings().orElseThrow(() -> new IllegalStateException("An over cannot start when there is no current innings"));
            boolean isFirst = innings.overs().last().isEmpty();
            Player strikerPlayer = Objects.requireNonNullElse(striker, playerOrNull(isFirst ? innings.currentStriker() : innings.currentNonStriker()));
            Player nonStrikerPlayer = Objects.requireNonNullElse(nonStriker, playerOrNull(isFirst ? innings.currentNonStriker() : innings.currentStriker()));
            int numberInInnings = innings.overs().size();
            return new OverStartingEvent(bowler, strikerPlayer, nonStrikerPlayer, time, ballsInOver, numberInInnings, innings.inningsNumber());
        }

        @Nullable
        private static Player playerOrNull(Optional<BatterInnings> batterInnings) {
            return batterInnings.isPresent() ? batterInnings.get().player() : null;
        }
    }
}
