package com.danielflower.crickam.scorer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Innings {
	public final Match match;
	private final LineUp battingTeam;
	private final LineUp bowlingTeam;
	public final int inningsNumber;
	private final List<Partnership> partnerships = new ArrayList<>();
	private final List<BatsmanInnings> batters = new ArrayList<>();
	private final List<Player> yetToBat;
	private final List<Over> overs = new ArrayList<>();
	public final Date startTime;
	public Date endTime;
	private Balls balls = new Balls();
	private BatsmanInnings currentStriker;
	private BatsmanInnings currentNonStriker;
	private BowlingSpell currentBowlingSpell;
	private BowlingSpell currentBowlingSpellOfOtherBowler;
	private List<BowlerInnings> bowlerInningses = new ArrayList<>();
	private final List<BowlingSpell> spells = new ArrayList<>();
	private Score difference;
	private final int numberOfScheduledOvers;

	public Over getCurrentOver() {
		if (overs.size() == 0) {
			return null;
		}
		return overs.get(overs.size() - 1);
	}

	public boolean hasStarted() {
		Over currentOver = getCurrentOver();
		return currentOver != null && currentOver.getCurrentBall() != null;
	}

	public BatsmanInnings getCurrentStriker() {
		return currentStriker;
	}

	public BatsmanInnings getCurrentNonStriker() {
		return currentNonStriker;
	}

	public Partnership getCurrentPartnership() {
		return partnerships.get(partnerships.size() - 1);
	}

	public LineUp getBattingTeam() {
		return battingTeam;
	}

	public LineUp getBowlingTeam() {
		return bowlingTeam;
	}

	public List<Partnership> getPartnerships() {
		return partnerships;
	}

	public List<BatsmanInnings> getBatters() {
		return batters;
	}

	public List<Player> getYetToBat() {
		return yetToBat;
	}

	public BowlingSpell getCurrentBowlingSpell() {
		return currentBowlingSpell;
	}

	public BowlingSpell getCurrentBowlingSpellOfOtherBowler() {
		return currentBowlingSpellOfOtherBowler;
	}

	public Player nextBatsman(boolean remove) {
		Player player = yetToBat.get(0);
		if (!yetToBat.remove(player)) {
			throw new IllegalStateException("This shouldn't happen");
		}
		return player;
	}

	public List<Over> getOvers() {
		return overs;
	}

	public Balls getBalls() {
		return balls;
	}

	public boolean getAllOut() {
		return yetToBat.size() == 0 && (getCurrentStriker() == null || getCurrentNonStriker() == null);
	}

	public int wicketsRemaining() {
		return yetToBat.size() + 1;
	}

	public Innings(Match match, LineUp battingTeam, LineUp bowlingTeam, List<Player> battingOrder, int inningsNumber, Date startTime, int numberOfScheduledOvers) {
		this.match = match;
		this.battingTeam = battingTeam;
		this.bowlingTeam = bowlingTeam;
		this.inningsNumber = inningsNumber;
		yetToBat = new ArrayList<>(battingOrder);
		this.startTime = startTime;
		this.numberOfScheduledOvers = numberOfScheduledOvers;
	}

	public BatsmanInnings bringInNextBatsman(Instant time) {
		if (yetToBat.size() == 0) {
			throw new IllegalStateException("Cannot bring in a new batsman when the team is all out");
		}
		Player player = nextBatsman(true);
		return bringInNextBatsman(player, time);
	}

	public BatsmanInnings bringInNextBatsman(Player player, Instant time) {

		BatsmanInnings batsmanInnings = null;
		for (BatsmanInnings batter : batters) {
			if (batter.getPlayer().equals(player)) {
				// retired player coming back
				batsmanInnings = batter;
				batsmanInnings.undoDismissal();
			}
		}
		if (batsmanInnings == null) {
			batsmanInnings = new BatsmanInnings(player, balls, batters.size() + 1, time);
			batters.add(batsmanInnings);
		}
		yetToBat.remove(player);

		if (currentStriker == null) {
			currentStriker = batsmanInnings;
		} else if (currentNonStriker == null) {
			currentNonStriker = batsmanInnings;
		}
		if (batters.size() > 1) {
			Partnership partnership = new Partnership(balls.score().wickets + 1, other(batsmanInnings), batsmanInnings, time);
			partnerships.add(partnership);
		}
		return batsmanInnings;
	}

	private BatsmanInnings other(BatsmanInnings batsmanInnings) {
		return getCurrentStriker() == batsmanInnings ?
				getCurrentNonStriker() : getCurrentStriker();
	}

	public void addBall(BallAtCompletion ball) {
		balls = balls.add(ball);
		getCurrentPartnership().addBall(ball);
		getCurrentStriker().addBall(ball);
		currentBowlingSpell.addBall(ball);

		Over currentOver = getCurrentOver();
		currentOver.addBall(ball);
		if (ball.getPlayersCrossed()) {
			switchPlayers();
		}

		if (ball.getDismissal().isPresent()) {
			Dismissal dismissal = ball.getDismissal().get();
			if (dismissal.batter == currentNonStriker) {
				currentNonStriker.setDismissal(dismissal, ball.getDateCompleted());
				currentNonStriker = null;
			} else {
				currentStriker.setDismissal(dismissal, ball.getDateCompleted());
				currentStriker = null;
			}
		}

		difference = difference.subtract(ball.getScore());
	}

	public Over newOver(Player bowler) {
		BowlerInnings innings = getOrCreateBowlerInnings(bowler);
		BowlingSpell bowlingSpell;
		if (currentBowlingSpellOfOtherBowler != null && bowler.equals(currentBowlingSpellOfOtherBowler.getBowlerInnings().getPlayer())) {
			bowlingSpell = currentBowlingSpellOfOtherBowler;
		} else {
			bowlingSpell = new BowlingSpell(innings, innings.getSpells().size() + 1);
			spells.add(bowlingSpell);
			innings.addBowlingSpell(bowlingSpell);
		}

		Over over = new Over(overs.size() + 1, currentStriker, currentNonStriker, bowlingSpell);
		overs.add(over);
		bowlingSpell.addOver(over);

		currentBowlingSpellOfOtherBowler = currentBowlingSpell;
		currentBowlingSpell = bowlingSpell;
		return over;
	}

	private BowlerInnings getOrCreateBowlerInnings(Player bowler) {
		Optional<BowlerInnings> first = bowlerInningses.stream().filter(s -> s.getPlayer().equals(bowler)).findFirst();
		if (first.isPresent()) {
			return first.get();
		} else {
			BowlerInnings bowlerInnings = new BowlerInnings(bowler);
			bowlerInningses.add(bowlerInnings);
			return bowlerInnings;
		}
	}

	public void switchPlayers() {
		BatsmanInnings previousStriker = currentStriker;
		currentStriker = currentNonStriker;
		currentNonStriker = previousStriker;
	}

	public List<BowlerInnings> getBowlerInningses() {
		return bowlerInningses;
	}


	public int getNumberOfBallsRemaining() {
		int totalBalls = numberOfScheduledOvers * 6;
		return totalBalls - getBalls().score().balls;
	}

	public boolean isFinished() {
		return getAllOut() || getNumberOfBallsRemaining() == 0;
	}

	public int getNumberOfScheduledBalls() {
		return numberOfScheduledOvers * 6;
	}



	public BatsmanInnings getBatsmanInnings(Player target) {
		Guard.notNull("target", target);
		if (target.equals(currentStriker.getPlayer())) {
			return currentStriker;
		} else if (target.equals(currentNonStriker.getPlayer())) {
			return currentNonStriker;
		}
		for (BatsmanInnings batter : batters) {
			if (target.equals(batter.getPlayer())) {
				return batter;
			}
		}
		throw new RuntimeException("Could not find innings for " + target);
	}

}


