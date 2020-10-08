package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.England;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class LineUpTest {

    LineUp england = LineUp.lineUp()
        .withTeam(England.team().build())
        .withCaptain(England.STOKES)
        .withWicketKeeper(England.STOKES)
        .withBattingOrder(ImmutableList.of(
            England.SAM_CURRAN, England.TOM_CURRAN, England.MAHMOOD, England.STOKES
        ))
        .build();

    @Test
    void canLookupBasedOnSurname() {
        assertThat(england.findPlayer("Mahmood"), is(England.MAHMOOD));
        assertThat(england.findPlayer("MAHMOOD"), is(England.MAHMOOD));
        assertThat(england.findPlayer("mahmood"), is(England.MAHMOOD));
        assertThat(england.findPlayer("Flower"), is(nullValue()));
    }

}