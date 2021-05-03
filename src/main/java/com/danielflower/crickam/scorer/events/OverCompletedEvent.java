package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Immutable
public final class OverCompletedEvent extends BaseMatchEvent {

    private final Player bowler;
    private final int inningsNumber;
    private final int overNumber;
    private final int ballsInOver;
    private final Score score;
    private final boolean isMaiden;

    private OverCompletedEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint, Player bowler, int inningsNumber, int overNumber, int ballsInOver, Score score, boolean isMaiden) {
        super(id, time, customData, undoPoint);
        this.bowler = bowler;
        this.inningsNumber = inningsNumber;
        this.overNumber = overNumber;
        this.ballsInOver = ballsInOver;
        this.score = requireNonNull(score, "score");
        this.isMaiden = isMaiden;
    }

    public Player bowler() { return bowler; }

    public boolean isMaiden() { return isMaiden; }

    public int inningsNumber() {
        return inningsNumber;
    }

    public int overNumber() {
        return overNumber;
    }

    public int ballsInOver() {
        return ballsInOver;
    }

    public Score score() {
        return score;
    }


    @Override
    public @Nonnull Builder newBuilder() {
        return baseBuilder(new Builder())
            .withBowler(bowler())
            .withMaiden(isMaiden())
            .withBallsInOver(ballsInOver())
            .withInningsNumber(inningsNumber())
            .withOverNumber(overNumber())
            .withScore(score())
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OverCompletedEvent that = (OverCompletedEvent) o;
        return inningsNumber == that.inningsNumber && overNumber == that.overNumber && ballsInOver == that.ballsInOver && isMaiden == that.isMaiden && Objects.equals(bowler, that.bowler) && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bowler, inningsNumber, overNumber, ballsInOver, score, isMaiden);
    }

    @Override
    public String toString() {
        return "OverCompletedEvent{" +
            "bowler=" + bowler +
            ", inningsNumber=" + inningsNumber +
            ", overNumber=" + overNumber +
            ", ballsInOver=" + ballsInOver +
            ", score=" + score +
            ", isMaiden=" + isMaiden +
            '}';
    }

    public static final class Builder extends BaseMatchEventBuilder<Builder, OverCompletedEvent> {

        private Player bowler;
        private Integer inningsNumber;
        private Integer overNumber;
        private Integer ballsInOver;
        private Score score;
        private Boolean isMaiden;

        public Integer inningsNumber() {
            return inningsNumber;
        }

        public Integer overNumber() {
            return overNumber;
        }

        public Integer ballsInOver() {
            return ballsInOver;
        }

        public Builder withBowler(@Nullable Player bowler) {
            this.bowler = bowler;
            return this;
        }

        public Builder withMaiden(@Nullable Boolean maiden) {
            isMaiden = maiden;
            return this;
        }

        public Builder withInningsNumber(@Nullable Integer inningsNumber) {
            this.inningsNumber = inningsNumber;
            return this;
        }

        public Builder withOverNumber(@Nullable Integer overNumber) {
            this.overNumber = overNumber;
            return this;
        }

        public Builder withBallsInOver(@Nullable Integer ballsInOver) {
            this.ballsInOver = ballsInOver;
            return this;
        }

        public Builder withScore(@Nullable Score score) {
            this.score = score;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null) throw new IllegalStateException("Cannot complete an over when there is no innings in progress");
            Over over = innings.currentOver();
            if (over == null) throw new IllegalStateException("Cannot complete an over when there is no over in progress");

            if (bowler == null) bowler = over.bowler();
            if (inningsNumber == null) inningsNumber = innings.inningsNumber();
            if (overNumber == null) overNumber = over.overNumber();
            if (ballsInOver == null) ballsInOver = over.ballsInOver();
            if (score == null) score = over.score();
            if (isMaiden == null) isMaiden = over.isMaiden();
            return this;
        }

        @Nonnull
        public OverCompletedEvent build() {
            return new OverCompletedEvent(id(), time(), customData(), undoPoint(), bowler, inningsNumber, overNumber, ballsInOver, score, isMaiden);
        }

    }
}
