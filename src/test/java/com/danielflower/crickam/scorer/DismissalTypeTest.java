package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DismissalTypeTest {

    @Test
    void fullNameIsLowercase() {
        assertThat(DismissalType.LEG_BEFORE_WICKET.fullName(), is("leg before wicket"));
    }

    @Test
    void abbreviationsAreLowercase() {
        assertThat(DismissalType.LEG_BEFORE_WICKET.abbreviation(), is("lbw"));
    }

}