package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static com.danielflower.crickam.scorer.data.NewZealand.MITCH_SANTNER;
import static com.danielflower.crickam.scorer.data.NewZealand.TRENT_BOULT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BowlerInningsTest {

    @Test
    public void canCreate() {
        assertThat(aBowlerInnings().withBowler(MITCH_SANTNER).build().bowler(), is(MITCH_SANTNER));
    }

    public static BowlerInningsBuilder aBowlerInnings() {
        return new BowlerInningsBuilder()
            .withBowler(TRENT_BOULT);
    }

}