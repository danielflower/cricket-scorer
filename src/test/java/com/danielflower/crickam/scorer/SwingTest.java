package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SwingTest {

    @Test
    public void canCreate() {
        Swing swing = aSwing().setFootDirection(Optional.of(12.0)).setImpact(Optional.of(Impact.EDGED)).build();
        assertThat(swing.getFootDirection(), is(Optional.of(12.0)));
        assertThat(swing.getImpact(), is(Optional.of(Impact.EDGED)));
    }

    public static SwingBuilder aSwing() {
        return new SwingBuilder();
    }

}