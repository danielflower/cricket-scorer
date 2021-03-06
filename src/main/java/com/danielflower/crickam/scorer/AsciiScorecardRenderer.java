package com.danielflower.crickam.scorer;

import com.danielflower.crickam.scorer.events.BatterInningsCompletedEvent;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.danielflower.crickam.scorer.Crictils.pluralize;

@Immutable
public final class AsciiScorecardRenderer {

    private static final char NEWLINE = '\n';

    public static String toString(MatchControl control) {
        StringBuilder writer = new StringBuilder();
        try {
            render(control, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return writer.toString();
    }

    public static void render(MatchControl control, Appendable writer) throws IOException {
        Match match = control.match();

        String matchType = match.oversPerInnings() == null
            ? match.numberOfScheduledDays() + " day match"
            : match.oversPerInnings() == 20 ? "T20" : "One day match";

        String header = match.teams().stream().map(l -> l.teamName().toUpperCase()).collect(Collectors.joining(" vs "));
        writer.append(header).append(NEWLINE).append(repeat('=', header.length()))
            .append(NEWLINE).append(NEWLINE)
            .append(matchType);

        if (match.scheduledStartTime() != null && match.timeZone() != null) {
            LocalDateTime startTime = LocalDateTime.ofInstant(match.scheduledStartTime(), match.timeZone().toZoneId());
            writer.append(", ");
            if (match.numberOfScheduledDays() == 1) {
                writer.append(startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
            } else {
                LocalDateTime endTime = startTime.plusDays(match.numberOfScheduledDays() - 1);
                String endFormat = startTime.getMonthValue() == endTime.getMonthValue() ? "d yyyy" : "MMM d yyyy";
                writer.append(startTime.format(DateTimeFormatter.ofPattern("MMM d"))).append(" to ")
                    .append(endTime.format(DateTimeFormatter.ofPattern(endFormat)));
            }
        }

        // Match summary
        writer.append(NEWLINE);
        if (match.result() != null) {
            writer.append(NEWLINE).append(match.result().toString()).append(NEWLINE);
        }

        // Batting scorecard
        for (Innings innings : match.inningsList()) {
            boolean multipleInningsPerTeam = match.numberOfInningsPerTeam() > 1;
            String inningsNumber = multipleInningsPerTeam ? " " + Crictils.withOrdinal(innings.inningsNumberForBattingTeam()) : "";
            String inningsHeader = innings.battingTeam().teamName() + inningsNumber + " Innings";
            if (innings.originalMaxOvers() != null) {
                inningsHeader += " (" + pluralize(innings.originalMaxOvers(), "over") + " maximum)";
            }
            if (multipleInningsPerTeam && innings.target() != null) {
                inningsHeader += " (target: " + pluralize(innings.target(), "run") + ")";
            }

            writer.append(NEWLINE).append(inningsHeader).append(NEWLINE).append(repeat('-', inningsHeader.length())).append(NEWLINE).append(NEWLINE);

            int[] batColWidths = {-21, -30, 4, 4, 4, 3, 3, 7};
            renderLine(writer, batColWidths, "BATTER", "", "R", "M", "B", "4s", "6s", "SR");
            for (BatterInnings bi : innings.batterInningsList()) {
                Score s = bi.score();
                String sr = s.battingStrikeRate() != null ? String.format("%.1f", s.battingStrikeRate()) : "-";
                String dismissal = bi.dismissal() != null ? bi.dismissal().toScorecardString(innings.bowlingTeam())
                    : bi.state() == BattingState.RETIRED || bi.state() == BattingState.RETIRED_OUT
                    ? "retired" : "not out";
                String batterName = bi.player().scorecardName();
                if (bi.player().samePlayer(innings.battingTeam().wicketKeeper())) {
                    batterName += " †";
                }
                if (bi.player().samePlayer(innings.battingTeam().captain())) {
                    batterName += " (c)";
                }
                renderLine(writer, batColWidths, batterName, dismissal, s.batterRuns(), "", s.facedByBatter(), s.fours(), s.sixes(), sr);
            }
            renderLine(writer, batColWidths, "Extras", getExtraDetails(innings.score()), innings.score().extras());
            renderLine(writer, batColWidths, "TOTAL", "(" + innings.score().wickets() + " wkts; " + innings.overDotBallString() + " overs)", innings.score().teamRuns());
            writer.append(NEWLINE);


            // Yet to bat
            if (!innings.yetToBat().isEmpty()) {
                writer.append(innings.state() == Innings.State.COMPLETED ? "Did not bat: " : "Yet to bat: ");
                writer.append(innings.yetToBat().stream().map(Player::scorecardName).collect(Collectors.joining(", ")))
                    .append(NEWLINE).append(NEWLINE);
            }

            // Fall of wickets
            writer.append("Fall of wickets: ");
            List<MatchControl> dismissals = control.history().stream()
                .filter(MatchControl.sameInnings(innings))
                .filter(me -> me.event() instanceof BatterInningsCompletedEvent)
                .filter(me -> ((BatterInningsCompletedEvent)me.event()).reason() != BattingState.INNINGS_ENDED)
                .collect(Collectors.toList());

            int num = 0;
            for (MatchControl state : dismissals) {
                if (num != 0) {
                    if (num % 3 == 0) {
                        writer.append(",").append(NEWLINE);
                    } else {
                        writer.append(", ");
                    }
                }
                Innings innings1 = Objects.requireNonNull(state.match().currentInnings());
                Score score = innings1.score();
                BatterInningsCompletedEvent event = (BatterInningsCompletedEvent) state.event();
                String scoreText = score.wickets() + "-" + score.teamRuns();
                String time;
                if (event.reason() == BattingState.RETIRED_OUT || event.reason() == BattingState.RETIRED) {
                    scoreText += "*";
                    time = "retired " + ((event.reason() == BattingState.RETIRED) ? "not out" : "out");
                } else {
                    time = innings1.overDotBallString() + " ov";
                }

                writer.append(scoreText).append(" (").append(event.batter().name())
                    .append(", ").append(time).append(")");
                num++;
            }
            writer.append(NEWLINE).append(NEWLINE);


            int[] bowlColWidths = {-20, 5, 4, 4, 4, 7, 3, 3, 3, 3, 3};
            boolean showDots = match.oversPerInnings() != null;
            renderLine(writer, bowlColWidths, "BOWLING", "O", "M", "R", "W", "Econ", showDots ? "0s" : "", "4s", "6s", "WD", "NB");
            for (BowlerInnings bi : innings.bowlerInningsList()) {
                Score s = bi.score();
                String dots = showDots ? String.valueOf(s.dots()) : "";
                renderLine(writer, bowlColWidths, bi.bowler().scorecardName(), bi.overDotBallString(), bi.maidens(),
                    s.bowlerRuns(), bi.wickets(), s.bowlerEconomyRate(), dots, s.fours(), s.sixes(), s.wideDeliveries(), s.noBalls());
            }

            writer.append(NEWLINE);
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
