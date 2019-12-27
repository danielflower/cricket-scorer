package com.danielflower.crickam.scorer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AsciiScorecardRenderer {

    private static final String NEWLINE = String.format("%n");

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

    public static void render(Match match, Appendable writer) throws IOException {

        writer.append(match.teams().get(0).team().name()).append(" vs ").append(match.teams().get(1).team().name());

        if (match.venue().isPresent()) {
            Venue venue = match.venue().get();
            writer.append(" at ").append(venue.name()).append(", ").append(venue.city());
            if (match.scheduledStartTime().isPresent()) {
                LocalDateTime startTime = LocalDateTime.ofInstant(match.scheduledStartTime().get(), venue.timeZone().toZoneId());
                writer.append(", ").append(startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
            }
        }
        writer.append(NEWLINE);

        for (Innings innings : match.inningsList()) {
            writer.append(NEWLINE).append(innings.battingTeam().team().name()).append(" Innings");
            if (innings.originalNumberOfScheduledOvers().isPresent()) {
                writer.append(" (").append(innings.originalNumberOfScheduledOvers().get().toString()).append(" overs maximum)").append(NEWLINE);
            }
            int[] batColWidths = {-17, -26, 4, 4, 4, 3, 3, 7};
            renderLine(writer, batColWidths, "BATTER", "", "R", "M", "B", "4s", "6s", "SR");
            for (BatterInnings bi : innings.batterInningsList()) {
                Score s = bi.score();
                String sr = s.battingStrikeRate().isPresent() ? String.format("%.1f", s.battingStrikeRate().get()) : "-";
                String dismissal = bi.dismissal().isPresent() ? bi.dismissal().get().toScorecardString() : "not out";
                renderLine(writer, batColWidths, bi.player().firstInitialWithSurname(), dismissal, s.batterRuns(), "", s.validDeliveries(), s.fours(), s.sixes(), sr);
            }
            renderLine(writer, batColWidths, "Extras", getExtraDetails(innings.score()), innings.score().extras());
            renderLine(writer, batColWidths, "TOTAL", "(" + innings.score().wickets() + " wkts; " + innings.ballNumber() + " overs)", innings.score().teamRuns());
            writer.append(NEWLINE);

            if (!innings.yetToBat().isEmpty()) {
                writer.append(innings.state() == Innings.State.COMPLETED ? "Did not bat: " : "Yet to bat: ");
                writer.append(innings.yetToBat().stream().map(Player::firstInitialWithSurname).collect(Collectors.joining(", ")));
            }

            writer.append(NEWLINE).append(NEWLINE);

            int[] bowlColWidths = {-20, 4, 4, 4, 4, 7, 3, 3, 3, 3, 3};
            renderLine(writer, bowlColWidths, "BOWLING", "O", "M", "R", "W", "Econ", "0s", "4s", "6s", "WD", "NB");
            for (BowlerInnings bi : innings.bowlerInningsList()) {
                Score s = bi.score();
                renderLine(writer, bowlColWidths, bi.bowler().firstInitialWithSurname(), bi.overs().size(), bi.maidens(),
                    s.bowlerRuns(), s.wickets(), s.bowlerEconomyRate(), s.dots(), s.fours(), s.sixes(), s.wides(), s.noBalls());
            }

        }

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

//    public static String asciiScorecard(Innings innings) {
//        int[] cols = new int[]{15, 24, 4, 4, 4, 3, 3, 6};
//        List<Object[]> rows = new ArrayList<>();
//        rows.add(new Object[]{innings.getBattingTeam().team.getName().toUpperCase(), "", "R", "M", "B", "4s", "6s", "SR"});
//        for (BatsmanInnings batter : innings.getBatters()) {
//
//            Score s = batter.getBalls().score();
//            String mins = "";
//
//            String dismissal = batter.getDismissal().isPresent() ? batter.getDismissal().get().toScorecardString() : "not out";
//            rows.add(new Object[]{batter.getPlayer().familyName, dismissal, s.scored, mins, s.balls, s.fours, s.sixes, s.strikeRate()});
//        }
//
//        Score score = innings.getBalls().score();
//        String extraDetails = getExtraDetails(score);
//        rows.add(new Object[]{"Extras", extraDetails, score.extras(), "", "", "", "", ""});
//        List<Over>  overs = innings.getOvers();
//        int ballsModulus = overs.size() > 0 ? overs.get(overs.size() -1).getLegalBalls() : 0;
//        rows.add(new Object[]{"TOTAL", "(" + score.wickets + " wkts; " + (overs.size() - 1) + "." + ballsModulus + " overs)", score.totalRuns(), "", "", "", "", ""});
//
//        StringBuilder sb = new StringBuilder();
//
//        for (Object[] row : rows) {
//            for (int i = 0; i < cols.length; i++) {
//                int colsSize = cols[i];
//                String str = row[i].toString();
//                if (str.length() > colsSize - 1) {
//                    str = str.substring(0, colsSize - 1);
//                }
//                if (i < 2) {
//                    sb.append(StringUtils.rightPad(str, colsSize));
//                } else {
//                    sb.append(StringUtils.leftPad(str, colsSize));
//                }
//            }
//            sb.append(NEWLINE);
//        }
//
//        return sb.toString();
//    }


}
