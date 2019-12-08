package com.danielflower.crickam.scorer;

import java.util.Date;
import java.util.Optional;

public interface BallAtCompletion extends BallAtEngagement {
	Score getScore();

	boolean getPlayersCrossed();

	Optional<Dismissal> getDismissal();

	boolean isLegal();

	Optional<Player> getFielder();

	Date getDateCompleted();
}


