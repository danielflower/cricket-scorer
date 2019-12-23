package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.danielflower.crickam.scorer.data.NewZealand.JAMES_NEESHAM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BatsmanInningsTest {

    @Test
    public void canCreate() {
        BatsmanInnings bi = aBatsmanInnings().setPlayer(JAMES_NEESHAM).build();
        assertThat(bi.getPlayer(), is(JAMES_NEESHAM));
    }


    public static BatsmanInningsBuilder aBatsmanInnings() {
        return new BatsmanInningsBuilder()
            .setPlayer(PlayerTest.aPlayer())
            .setInningsStartTime(Instant.now())
            .setNumberCameIn(1)
            ;
    }

}