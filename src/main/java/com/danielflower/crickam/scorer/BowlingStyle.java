package com.danielflower.crickam.scorer;

import com.danielflower.crickam.stats.Distribution;
import com.danielflower.crickam.stats.SimpleRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@ApiStatus.AvailableSince("1.0.0")
public final class BowlingStyle {

	private final Handedness bowlingHandedness;
	private final Distribution<DeliveryType> deliveryTypes;
	private final SimpleRange bowlingSpeed;

	BowlingStyle(@NotNull Handedness bowlingHandedness, @Nullable Distribution<DeliveryType> deliveryTypes,
                        @Nullable SimpleRange bowlingSpeed) {
        Objects.requireNonNull((Object) bowlingHandedness, "bowlingHandedness");
        this.bowlingHandedness = bowlingHandedness;
		this.deliveryTypes = deliveryTypes;
		this.bowlingSpeed = bowlingSpeed;
	}

    /**
     * @return The hand predominantly used to bowl with
     */
    public Handedness bowlingHandedness() {
        return bowlingHandedness;
    }

    /**
     * @return The types of balls this bowler bowls, weighted by the likelihood of bowling that ball.
     */
    public Distribution<DeliveryType> deliveryTypes() {
        return deliveryTypes;
    }

    /**
     * @return The range of speeds that this bowler can bowl at
     */
    public SimpleRange bowlingSpeed() {
        return bowlingSpeed;
    }
}
