package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DeliveryTest {

    @Test
    public void canBuildThem() {
        Delivery delivery = aDelivery().withDeliveryType(DeliveryType.ARM_BALL).build();
        assertThat(delivery.deliveryType(), is(DeliveryType.ARM_BALL));
    }

    public static Delivery.Builder aDelivery() {
        return Delivery.delivery();
    }

}