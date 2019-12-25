package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RPOTest {

    @Test
    void canConvertToVariousFormats() {
        RPO pi = RPO.fromDouble(3.141592653589793238462643383279502884197169399375105820);
        assertThat(pi.toString(), is("3.1"));
        assertThat(pi.firstDecimal(), is(1));
        assertThat(pi.intValue(), is(3));
        assertThat(pi.nearestIntValue(), is(3));
        assertThat(pi.value(), is(3.141592653589793238462643383279502884197169399375105820));
    }

    @Test
    void canRoundUp() {
        RPO nopi = RPO.fromDouble(3.99141592653589793238462643383279502884197169399375105820);
        assertThat(nopi.toString(), is("3.9"));
        assertThat(nopi.firstDecimal(), is(9));
        assertThat(nopi.intValue(), is(3));
        assertThat(nopi.nearestIntValue(), is(4));
        assertThat(nopi.value(), is(3.99141592653589793238462643383279502884197169399375105820));
    }

}