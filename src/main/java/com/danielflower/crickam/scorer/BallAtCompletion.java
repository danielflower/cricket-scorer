package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.Optional;

public interface BallAtCompletion extends BallAtEngagement, MatchEvent {

    Score score();

	boolean getPlayersCrossed();

	Optional<Dismissal> getDismissal();

	boolean isLegal();

	Optional<Player> getFielder();

	Instant getDateCompleted();
}


