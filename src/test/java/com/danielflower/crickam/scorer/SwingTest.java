package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SwingTest {

    @Test
    public void canCreate() {
        Swing swing = aSwing().withFootDirection(12.0).withImpact(ImpactOnBat.EDGED).build();
        assertThat(swing.footDirection(), is(Optional.of(12.0)));
        assertThat(swing.impactOnBat(), is(Optional.of(ImpactOnBat.EDGED)));
    }

    public static Swing.Builder aSwing() {
        return new Swing.Builder();
    }

}