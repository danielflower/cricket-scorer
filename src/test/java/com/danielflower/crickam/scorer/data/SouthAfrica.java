package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.Team;
import com.danielflower.crickam.scorer.TeamLevel;

import static com.danielflower.crickam.scorer.Handedness.LEFT_HANDED;
import static com.danielflower.crickam.scorer.PlayingRole.*;
import static com.danielflower.crickam.scorer.data.DataUtil.player;

public class SouthAfrica {

    public static final Player AMLA = player("Hashim Amla").withPlayingRole(BATTER).build();
    public static final Player BAVUMA = player("Temba Bavuma").withPlayingRole(BATTER).build();
    public static final Player DALA = player("Junior Dala").withPlayingRole(BOWLER).build();
    public static final Player DE_BRUYN = player("Theunis de Bruyn").withFamilyName("de Bruyn").withPlayingRole(BATTER).build();
    public static final Player DE_KOCK = player("Quinton de Kock").withFamilyName("de Kock").withPlayingRole(WICKET_KEEPER).build();
    public static final Player DUMINY = player("Jean-Paul Duminy").withPlayingRole(ALL_ROUNDER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player DU_PLESSIS = player("Faf du Plessis").withFamilyName("du Plessis").withPlayingRole(BATTER).build();
    public static final Player ELGAR = player("Dean Elgar").withPlayingRole(BATTER).withHandedness(LEFT_HANDED).build();
    public static final Player FORTUIN = player("Bjorn Fortuin").withPlayingRole(BOWLER).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player BE_HENDRICKS = player("Beuran Hendricks").withPlayingRole(BOWLER).withHandedness(LEFT_HANDED).build();
    public static final Player RR_HENDRICKS = player("Reeza Hendricks").withPlayingRole(BATTER).build();
    public static final Player TAHIR = player("Imran Tahir").withPlayingRole(BOWLER).build();
    public static final Player KLAASEN = player("Heinrich Klaasen").withPlayingRole(WICKET_KEEPER).build();
    public static final Player LINDE = player("George Linde").withPlayingRole(BOWLER).withHandedness(LEFT_HANDED).build();
    public static final Player MAHARAJ = player("Keshav Maharaj").withPlayingRole(BOWLER).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player MARKRAM = player("Aiden Markram").withPlayingRole(BATTER).build();
    public static final Player MILLER = player("David Miller").withPlayingRole(BATTER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player MORRIS = player("Chris Morris").withPlayingRole(ALL_ROUNDER).build();
    public static final Player MUTHUSAMY = player("Senuran Muthusamy").withPlayingRole(ALL_ROUNDER).withHandedness(LEFT_HANDED).build();
    public static final Player NGIDI = player("Lungi Ngidi").withPlayingRole(BOWLER).build();
    public static final Player NORTJE = player("Anrich Nortje").withPlayingRole(BOWLER).build();
    public static final Player PHEHLUKWAYO = player("Andile Phehlukwayo").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player PHILANDER = player("Vernon Philander").withPlayingRole(ALL_ROUNDER).build();
    public static final Player PIEDT = player("Dane Piedt").withPlayingRole(BOWLER).build();
    public static final Player PRETORIUS = player("Dwaine Pretorius").withPlayingRole(BOWLER).build();
    public static final Player RABADA = player("Kagiso Rabada").withPlayingRole(BOWLER).withBattingHandedness(LEFT_HANDED).build();
    public static final Player SHAMSI = player("Tabraiz Shamsi").withPlayingRole(BOWLER).withBowlingHandedness(LEFT_HANDED).build();
    public static final Player SIPAMLA = player("Lutho Sipamla").withPlayingRole(BOWLER).build();
    public static final Player VAN_DER_DUSSEN = player("Rassie van der Dussen").withFamilyName("van der Dussen").withPlayingRole(BATTER).build();
    public static final Player HAMZA = player("Zubayr Hamza").withPlayingRole(BATTER).build();

    public static Team team() {
        return Team.team()
            .withShortName("SA")
            .withName("South Africa")
            .withLevel(TeamLevel.INTERNATIONAL)
            .build();
    }

}
