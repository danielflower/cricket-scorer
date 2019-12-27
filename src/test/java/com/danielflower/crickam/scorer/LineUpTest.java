package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.data.England;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        assertThat(england.findPlayer("Mahmood"), is(Optional.of(England.MAHMOOD)));
        assertThat(england.findPlayer("MAHMOOD"), is(Optional.of(England.MAHMOOD)));
        assertThat(england.findPlayer("mahmood"), is(Optional.of(England.MAHMOOD)));
        assertThat(england.findPlayer("Curran"), is(Optional.empty()));
        assertThat(england.findPlayer("Flower"), is(Optional.empty()));
    }

    @Test
    void canLookupBasedOnInitials() {
        assertThat(england.findPlayer("TK Curran"), is(Optional.of(England.TOM_CURRAN)));
        assertThat(england.findPlayer("Tom Curran"), is(Optional.of(England.TOM_CURRAN)));
        assertThat(england.findPlayer("Sam Curran"), is(Optional.of(England.SAM_CURRAN)));
        assertThat(england.findPlayer("SK Curran"), is(Optional.of(England.SAM_CURRAN)));
        assertThat(england.findPlayer("S MAHMOOD"), is(Optional.of(England.MAHMOOD)));
        assertThat(england.findPlayer("Saqib mahmood"), is(Optional.of(England.MAHMOOD)));
        assertThat(england.findPlayer("D Flower"), is(Optional.empty()));
    }

}