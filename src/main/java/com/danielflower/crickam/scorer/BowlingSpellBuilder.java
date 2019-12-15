package com.danielflower.crickam.scorer;

public class BowlingSpellBuilder {
    private BowlerInnings bowlerInnings;
    private int spellNumber;

    public BowlingSpellBuilder withBowlerInnings(BowlerInnings bowlerInnings) {
        this.bowlerInnings = bowlerInnings;
        return this;
    }

    public BowlingSpellBuilder withSpellNumber(int spellNumber) {
        this.spellNumber = spellNumber;
        return this;
    }

    public BowlingSpell build() {
        return new BowlingSpell(bowlerInnings, spellNumber);
    }
}