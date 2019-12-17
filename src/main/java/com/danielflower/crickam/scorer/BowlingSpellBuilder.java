package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

public class BowlingSpellBuilder {
    private BowlerInnings bowlerInnings;
    private int spellNumber;
    private ImmutableList<Over> overs;
    private Balls balls;

    public BowlingSpellBuilder withBowlerInnings(BowlerInnings bowlerInnings) {
        this.bowlerInnings = bowlerInnings;
        return this;
    }

    public BowlingSpellBuilder withSpellNumber(int spellNumber) {
        this.spellNumber = spellNumber;
        return this;
    }

    public BowlingSpell build() {
        return new BowlingSpell(bowlerInnings, spellNumber, overs, balls);
    }
}