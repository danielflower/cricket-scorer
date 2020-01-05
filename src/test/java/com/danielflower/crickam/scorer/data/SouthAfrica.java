package com.danielflower.crickam.scorer.data;

import com.danielflower.crickam.scorer.Player;
import com.danielflower.crickam.scorer.Team;

public class SouthAfrica {

    public static final Player AMLA = Player.player("Hashim Amla").build();
    public static final Player BAVUMA = Player.player("Temba Bavuma").build();
    public static final Player DALA = Player.player("Junior Dala").build();
    public static final Player DE_BRUYN = Player.player("Theunis de Bruyn").build();
    public static final Player DE_KOCK = Player.player("Quinton de Kock").build();
    public static final Player DUMINY = Player.player("Jean-Paul Duminy").build();
    public static final Player DU_PLESSIS = Player.player("Faf du Plessis").build();
    public static final Player ELGAR = Player.player("Dean Elgar").build();
    public static final Player FORTUIN = Player.player("Bjorn Fortuin").build();
    public static final Player BE_HENDRICKS = Player.player("Beuran Hendricks").build();
    public static final Player RR_HENDRICKS = Player.player("Reeza Hendricks").build();
    public static final Player TAHIR = Player.player("Imran Tahir").build();
    public static final Player KLAASEN = Player.player("Heinrich Klaasen").build();
    public static final Player LINDE = Player.player("George Linde").build();
    public static final Player MAHARAJ = Player.player("Keshav Maharaj").build();
    public static final Player MARKRAM = Player.player("Aiden Markram").build();
    public static final Player MILLER = Player.player("David Miller").build();
    public static final Player MORRIS = Player.player("Chris Morris").build();
    public static final Player MUTHUSAMY = Player.player("Senuran Muthusamy").build();
    public static final Player NGIDI = Player.player("Lungi Ngidi").build();
    public static final Player NORTJE = Player.player("Anrich Nortje").build();
    public static final Player PHEHLUKWAYO = Player.player("Andile Phehlukwayo").build();
    public static final Player PHILANDER = Player.player("Vernon Philander").build();
    public static final Player PIEDT = Player.player("Dane Piedt").build();
    public static final Player PRETORIUS = Player.player("Dwaine Pretorius").build();
    public static final Player RABADA = Player.player("Kagiso Rabada").build();
    public static final Player SHAMSI = Player.player("Tabraiz Shamsi").build();
    public static final Player SIPAMLA = Player.player("Lutho Sipamla").build();
    public static final Player VAN_DER_DUSSEN = Player.player("Rassie van der Dussen").build();
    public static final Player HAMZA = Player.player("Zubayr Hamza").build();

    public static Team team() {
        return Team.team()
            .withShortName("SA")
            .withName("South Africa")
            .build();
    }

}
