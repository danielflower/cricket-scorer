package com.danielflower.crickam.scorer;

public interface BallAtStart {
	BatsmanInnings getStriker();

	BatsmanInnings getNonStriker();

	Player bowler();

	int getNumberInOver();

	int id();

    BallAtEngagement engage(Delivery delivery, Swing swing, Trajectory trajectory);
}


