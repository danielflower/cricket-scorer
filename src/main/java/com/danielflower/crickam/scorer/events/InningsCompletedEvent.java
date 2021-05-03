package com.danielflower.crickam.scorer.events;

import com.danielflower.crickam.scorer.Innings;
import com.danielflower.crickam.scorer.Match;
import com.danielflower.crickam.scorer.Score;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Immutable
public final class InningsCompletedEvent extends BaseMatchEvent {

    private final boolean declared;
    private final int inningsNumber;
    private final Score score;

    private InningsCompletedEvent(UUID id, @Nullable Instant time, @Nullable Object customData, boolean undoPoint, boolean declared, @Nonnegative int inningsNumber, Score score) {
        super(id, time, customData, undoPoint);
        this.declared = declared;
        this.inningsNumber = inningsNumber;
        this.score = Objects.requireNonNull(score, "score");
    }


    public @Nonnegative int inningsNumber() {
        return inningsNumber;
    }

    /**
     * @return True if the batting team declared
     */
    public boolean declared() {
        return declared;
    }

    public Score score() {
        return score;
    }

    @Override
    public @Nonnull Builder newBuilder() {
        return baseBuilder(new Builder())
            .withDeclared(declared())
            .withInningsNumber(inningsNumber())
            .withScore(score())
            ;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InningsCompletedEvent that = (InningsCompletedEvent) o;
        return declared == that.declared && inningsNumber == that.inningsNumber && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declared, inningsNumber, score);
    }

    @Override
    public String toString() {
        return "InningsCompletedEvent{" +
            "declared=" + declared +
            ", inningsNumber=" + inningsNumber +
            ", score=" + score +
            '}';
    }

    public final static class Builder extends BaseMatchEventBuilder<Builder, InningsCompletedEvent> {
        private Boolean declared;
        private Integer inningsNumber;
        private Score score;

        public @Nullable Integer inningsNumber() { return inningsNumber; }
        public @Nullable Score score() { return score; }
        public @Nullable Boolean declared() {
            return declared;
        }

        public @Nonnull Builder withDeclared(@Nullable Boolean declared) {
            this.declared = declared;
            return this;
        }

        public @Nonnull Builder withInningsNumber(@Nullable Integer inningsNumber) {
            this.inningsNumber = inningsNumber;
            return this;
        }

        public @Nonnull Builder withScore(@Nullable Score score) {
            this.score = score;
            return this;
        }

        @Nonnull
        @Override
        public Builder apply(@Nonnull Match match) {
            Innings innings = match.currentInnings();
            if (innings == null) {
                throw new IllegalStateException("Can't end an innings as no in innings is in progress");
            }
            if (inningsNumber == null) {
                inningsNumber = match.inningsList().size();
            }
            if (declared == null) {
                declared = false;
            }
            if (score == null) {
                score = innings.score();
            }
            return this;
        }

        @Nonnull
        public InningsCompletedEvent build() {
            return new InningsCompletedEvent(id(), time(), customData(), undoPoint(), declared, inningsNumber, score);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Builder builder = (Builder) o;
            return declared == builder.declared;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), declared);
        }

        @Override
        public String toString() {
            return "Builder{" +
                "declared=" + declared +
                "} " + super.toString();
        }
    }

}
