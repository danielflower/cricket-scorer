package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SwingTest {

    @Test
    public void canCreate() {
        Swing swing = aSwing().withFootDirection(0.5).withImpact(ImpactOnBat.EDGED).build();
        assertThat(swing.footDirection(), is(Optional.of(0.5)));
        assertThat(swing.impactOnBat(), is(Optional.of(ImpactOnBat.EDGED)));
    }

    public static Swing.Builder aSwing() {
        return Swing.swing();
    }

}