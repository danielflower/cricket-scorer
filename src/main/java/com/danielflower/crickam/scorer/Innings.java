package com.danielflower.crickam.scorer;

import com.danielflower.crickam.utils.ImmutableList;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.danielflower.crickam.utils.ImmutableListCollector.toImmutableList;

public class Innings {
	public final Match match;
	private final LineUp battingTeam;
	private final LineUp bowlingTeam;
	public final int inningsNumber;
	private final ImmutableList<Partnership> partnerships;
	private final ImmutableList<BatsmanInnings> batters;
	private final ImmutableList<Player> yetToBat;
	private final ImmutableList<Over> overs;
	public final Instant startTime;
	public final Date endTime;
	private final Balls balls;
	private final BatsmanInnings currentStriker;
	private final BatsmanInnings currentNonStriker;
	private final BowlingSpell currentBowlingSpell;
	private final BowlingSpell currentBowlingSpellOfOtherBowler;
	private final ImmutableList<BowlerInnings> bowlerInningses;
	private final ImmutableList<BowlingSpell> spells;
	private final Score difference;
	private final int numberOfScheduledOvers;

	public Over getCurrentOver() {
		if (overs.size() == 0) {
			return null;
		}
		return overs.get(overs.size() - 1);
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

	public ImmutableList<Partnership> getPartnerships() {
		return partnerships;
	}

	public ImmutableList<BatsmanInnings> getBatters() {
		return batters;
	}

	public ImmutableList<Player> getYetToBat() {
		return yetToBat;
	}

	public BowlingSpell getCurrentBowlingSpell() {
		return currentBowlingSpell;
	}

	public BowlingSpell getCurrentBowlingSpellOfOtherBowler() {
		return currentBowlingSpellOfOtherBowler;
	}

	public Player nextBatsman(boolean remove) {
        return yetToBat.get(0);
	}

	public ImmutableList<Over> getOvers() {
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

	public Innings(Match match, LineUp battingTeam, LineUp bowlingTeam, ImmutableList<Partnership> partnerships, ImmutableList<Player> openers, int inningsNumber, ImmutableList<BatsmanInnings> batters, ImmutableList<Over> overs, Instant startTime, Date endTime, Balls balls, BatsmanInnings currentStriker, BatsmanInnings currentNonStriker, BowlingSpell currentBowlingSpell, BowlingSpell currentBowlingSpellOfOtherBowler, ImmutableList<BowlerInnings> bowlerInningses, ImmutableList<BowlingSpell> spells, Score difference, int numberOfScheduledOvers) {
		this.match = match;
		this.battingTeam = battingTeam;
		this.bowlingTeam = bowlingTeam;
        this.partnerships = partnerships;
        this.inningsNumber = inningsNumber;
        this.batters = batters;
        this.overs = overs;
        this.endTime = endTime;
        this.balls = balls;
        this.currentStriker = currentStriker;
        this.currentNonStriker = currentNonStriker;
        this.currentBowlingSpell = currentBowlingSpell;
        this.currentBowlingSpellOfOtherBowler = currentBowlingSpellOfOtherBowler;
        this.bowlerInningses = bowlerInningses;
        this.spells = spells;
        this.difference = difference;
        yetToBat = battingTeam.getPlayers().stream().filter(p -> !openers.contains(p)).collect(toImmutableList());
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
			}
		}
		if (batsmanInnings == null) {
			batsmanInnings = new BatsmanInnings(player, balls, balls, batters.size() + 1, time, null, Optional.empty());
			batters.add(batsmanInnings);
		}
//		yetToBat.remove(player);

		if (currentStriker == null) {
//			currentStriker = batsmanInnings;
		} else if (currentNonStriker == null) {
//			currentNonStriker = batsmanInnings;
		}
		if (batters.size() > 1) {
			Partnership partnership = new Partnership(balls.score().wickets + 1, other(batsmanInnings), batsmanInnings, balls, new Balls(), new Balls(), time, null);
			partnerships.add(partnership);
		}
		return batsmanInnings;
	}

	private BatsmanInnings other(BatsmanInnings batsmanInnings) {
		return getCurrentStriker() == batsmanInnings ?
				getCurrentNonStriker() : getCurrentStriker();
	}

	public void addBall(BallAtCompletion ball) {
//		balls = balls.add(ball);
		getCurrentPartnership().addBall(ball);
//		getCurrentStriker().addBall(ball);
//		currentBowlingSpell.addBall(ball);

		Over currentOver = getCurrentOver();
//		currentOver.addBall(ball);
		if (ball.getPlayersCrossed()) {
			switchPlayers();
		}

		if (ball.getDismissal().isPresent()) {
			Dismissal dismissal = ball.getDismissal().get();
			if (dismissal.batter == currentNonStriker) {
//				currentNonStriker.setDismissal(dismissal, ball.getDateCompleted());
//				currentNonStriker = null;
			} else {
//				currentStriker.setDismissal(dismissal, ball.getDateCompleted());
//				currentStriker = null;
			}
		}

//		difference = difference.subtract(ball.getScore());
	}

	public Over newOver(Player bowler) {
		BowlerInnings innings = getOrCreateBowlerInnings(bowler);
		BowlingSpell bowlingSpell;
		if (currentBowlingSpellOfOtherBowler != null && bowler.equals(currentBowlingSpellOfOtherBowler.getBowlerInnings().getPlayer())) {
			bowlingSpell = currentBowlingSpellOfOtherBowler;
		} else {
			bowlingSpell = new BowlingSpellBuilder().withBowlerInnings(innings).withSpellNumber(innings.getSpells().size() + 1).build();
			spells.add(bowlingSpell);
//			innings.addBowlingSpell(bowlingSpell);
		}

		Over over = new Over(overs.size() + 1, currentStriker, currentNonStriker, balls, bowlingSpell);
		overs.add(over);
//		bowlingSpell.addOver(over);

//		currentBowlingSpellOfOtherBowler = currentBowlingSpell;
//		currentBowlingSpell = bowlingSpell;
		return over;
	}

	private BowlerInnings getOrCreateBowlerInnings(Player bowler) {
		Optional<BowlerInnings> first = bowlerInningses.stream().filter(s -> s.getPlayer().equals(bowler)).findFirst();
		if (first.isPresent()) {
			return first.get();
		} else {
			BowlerInnings bowlerInnings = new BowlerInningsBuilder().withBowler(bowler).build();
			bowlerInningses.add(bowlerInnings);
			return bowlerInnings;
		}
	}

	public void switchPlayers() {
		BatsmanInnings previousStriker = currentStriker;
//		currentStriker = currentNonStriker;
//		currentNonStriker = previousStriker;
	}

	public ImmutableList<BowlerInnings> getBowlerInningses() {
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


