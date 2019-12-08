package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.time.Instant;

class BatsmanInningsTest {

    @Test
    public void canCreate() {
//        BatsmanInnings bi =
    }


    public static BatsmanInningsBuilder aBatsmanInnings() {
        return new BatsmanInningsBuilder()
            .setPlayer(PlayerTest.aPlayer())
            .setBallsSoFarInInnings(new Balls())
            .setInningsStartTime(Instant.now())
            .setNumberCameIn(1)
            ;
    }

}