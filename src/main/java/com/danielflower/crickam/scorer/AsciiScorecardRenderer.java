package com.danielflower.crickam.scorer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AsciiScorecardRenderer {

    public static final String NEWLINE = String.format("%n");

    public static String toString(Match match) {
        StringBuilder writer = new StringBuilder();
        try {
            render(match, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return writer.toString();
    }

    public static void render(Match match, Appendable writer) throws IOException {

        String header = match.teams().stream().map(l -> l.team().name().toUpperCase()).collect(Collectors.joining(" vs "));
        writer.append(header).append(NEWLINE).append(repeat('=', header.length()))
            .append(NEWLINE).append(NEWLINE)
            .append(match.matchType().name());

        if (match.venue().isPresent()) {
            Venue venue = match.venue().get();
            writer.append(" at ").append(venue.name()).append(", ").append(venue.city());
            if (match.scheduledStartTime().isPresent()) {
                LocalDateTime startTime = LocalDateTime.ofInstant(match.scheduledStartTime().get(), venue.timeZone().toZoneId());
                writer.append(", ").append(startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
            }
        }
        writer.append(NEWLINE);
        if (match.result().isPresent()) {
            writer.append(NEWLINE).append(match.result().get().toString()).append(NEWLINE);
        }

        for (Innings innings : match.inningsList()) {
            String inningsHeader = innings.battingTeam().team().name() + " Innings";
            if (innings.originalNumberOfScheduledOvers().isPresent()) {
                inningsHeader += " (" + innings.originalNumberOfScheduledOvers().getAsInt() + " overs maximum)";
            }

            writer.append(NEWLINE).append(inningsHeader).append(NEWLINE).append(repeat('-', inningsHeader.length())).append(NEWLINE).append(NEWLINE);

            int[] batColWidths = {-17, -26, 4, 4, 4, 3, 3, 7};
            renderLine(writer, batColWidths, "BATTER", "", "R", "M", "B", "4s", "6s", "SR");
            for (BatterInnings bi : innings.batterInningsList()) {
                Score s = bi.score();
                String sr = s.battingStrikeRate().isPresent() ? String.format("%.1f", s.battingStrikeRate().get()) : "-";
                String dismissal = bi.dismissal().isPresent() ? bi.dismissal().get().toScorecardString() : "not out";
                renderLine(writer, batColWidths, bi.player().firstInitialWithSurname(), dismissal, s.batterRuns(), "", s.facedByBatter(), s.fours(), s.sixes(), sr);
            }
            renderLine(writer, batColWidths, "Extras", getExtraDetails(innings.score()), innings.score().extras());
            renderLine(writer, batColWidths, "TOTAL", "(" + innings.score().wickets() + " wkts; " + innings.ballNumber() + " overs)", innings.score().teamRuns());
            writer.append(NEWLINE);


            if (!innings.yetToBat().isEmpty()) {
                writer.append(innings.state() == Innings.State.COMPLETED ? "Did not bat: " : "Yet to bat: ");
                writer.append(innings.yetToBat().stream().map(Player::firstInitialWithSurname).collect(Collectors.joining(", ")))
                    .append(NEWLINE);
            }
            writer.append(NEWLINE);


            writer.append("Fall of wickets: ");
            // TODO: handle case where innings started with penalties credited
            Score score = Score.EMPTY;
            for (Ball ball : innings.balls()) {
                score = score.add(ball.score());
                if (ball.dismissal().isPresent()) {
                    if (score.wickets() > 1) {
                        if (score.wickets() % 4 == 0) {
                            writer.append(",").append(NEWLINE);
                        } else {
                            writer.append(", ");
                        }
                    }
                    Player dismissed = ball.dismissal().get().batter();
                    String scoreText = score.wickets() + "-" + score.teamRuns();
                    writer.append(scoreText).append(" (").append(dismissed.firstInitialWithSurname()).append(")");
                }
            }
            writer.append(NEWLINE).append(NEWLINE);


            int[] bowlColWidths = {-20, 4, 4, 4, 4, 7, 3, 3, 3, 3, 3};
            renderLine(writer, bowlColWidths, "BOWLING", "O", "M", "R", "W", "Econ", "0s", "4s", "6s", "WD", "NB");
            for (BowlerInnings bi : innings.bowlerInningsList()) {
                Score s = bi.score();
                renderLine(writer, bowlColWidths, bi.bowler().firstInitialWithSurname(), bi.overs().size(), bi.maidens(),
                    s.bowlerRuns(), bi.wickets(), s.bowlerEconomyRate(), s.dots(), s.fours(), s.sixes(), s.wides(), s.noBalls());
            }

        }

    }

    private static String getExtraDetails(Score score) {
        if (score.extras() == 0) {
            return "";
        }
        String s = "(";
        String sep = ", ";
        if (score.byes() > 0) {
            s += "b " + score.byes() + sep;
        }
        if (score.legByes() > 0) {
            s += "lb " + score.legByes() + sep;
        }
        if (score.wides() > 0) {
            s += "w " + score.wides() + sep;
        }
        if (score.noBalls() > 0) {
            s += "nb " + score.noBalls() + sep;
        }
        if (score.penaltyRuns() > 0) {
            s += "p " + score.penaltyRuns() + sep;
        }
        return s.substring(0, s.length() - sep.length()) + ")";
    }

    private static void renderLine(Appendable writer, int[] colWidths, Object... values) throws IOException {
        for (int i = 0; i < colWidths.length; i++) {
            if (i < values.length) {
                int colsSize = colWidths[i];
                boolean leftAlign = colsSize < 0;
                colsSize = Math.abs(colsSize);
                String str = values[i].toString();
                if (str.length() > colsSize - 1) {
                    str = str.substring(0, colsSize - 1);
                }
                if (leftAlign) {
                    writer.append(String.format("%-" + colsSize + "s", str));
                } else {
                    writer.append(String.format("%" + colsSize + "s", str));
                }
            }
        }
        writer.append(NEWLINE);
    }

    private static String repeat(char value, int number) {
        return new String(new char[number]).replace('\0', value);
    }

}
