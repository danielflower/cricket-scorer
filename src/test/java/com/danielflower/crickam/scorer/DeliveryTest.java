package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DeliveryTest {

    @Test
    public void canBuildThem() {
        Delivery delivery = aDelivery().setDeliveryType(Optional.of(DeliveryType.ARM_BALL)).build();
        assertThat(delivery.getDeliveryType(), is(Optional.of(DeliveryType.ARM_BALL)));
    }

    public static DeliveryBuilder aDelivery() {
        return new DeliveryBuilder()
            .setBowledFrom(Optional.empty())
            .setDeliveryType(Optional.empty());
    }

}