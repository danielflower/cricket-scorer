package com.danielflower.crickam.scorer;

public interface BallAtStart {
	BatsmanInnings getStriker();

	BatsmanInnings getNonStriker();

	BowlingSpell getBowlingSpell();

	int getNumberInOver();

	int id();

	BallAtCompletion getPreviousBall();

    BallAtEngagement engage(Delivery delivery, Swing swing, Trajectory trajectory);
}


