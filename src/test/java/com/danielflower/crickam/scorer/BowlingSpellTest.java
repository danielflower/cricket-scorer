package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BowlingSpellTest {

    @Test
    public void canCreate() {
        assertThat(aBowlingSpell().withSpellNumber(2).build().getSpellNumber(), is(2));
    }

    public static BowlingSpellBuilder aBowlingSpell() {
        return new BowlingSpellBuilder()
            .withSpellNumber(1)
            .withBowlerInnings(BowlerInningsTest.aBowlerInnings().build());
    }

}