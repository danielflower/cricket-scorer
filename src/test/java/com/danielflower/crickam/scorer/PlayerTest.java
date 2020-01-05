package com.danielflower.crickam.scorer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    @Test
    public void kaneWilliamson() {
        Player kane = Player.player("Kane Stuart Williamson").build();
        assertThat(kane.familyName(), is("Williamson"));
        assertThat(kane.givenName(), is("Kane"));
        assertThat(kane.fullName(), is("Kane Williamson"));
        assertThat(kane.formalName(), is("Kane Stuart Williamson"));
        assertThat(kane.initialsWithSurname(), is("KS Williamson"));
        assertThat(kane.initials(), is("KS"));
    }

    @Test
    public void rossTaylor() {
        Player ross = Player.player("Luteru Ross Poutoa Lote Taylor").withGivenName("Ross").build();
        assertThat(ross.familyName(), is("Taylor"));
        assertThat(ross.givenName(), is("Ross"));
        assertThat(ross.formalGivenNames(), contains("Luteru", "Ross", "Poutoa", "Lote"));
        assertThat(ross.formalName(), is("Luteru Ross Poutoa Lote Taylor"));
        assertThat(ross.initialsWithSurname(), is("LRPL Taylor"));
        assertThat(ross.initials(), is("LRPL"));
        assertThat(ross.fullName(), is("Ross Taylor"));
    }

    @Test
    public void bjWatling() {
        Player ross = Player.player("Bradley-John Watling").withGivenName("BJ").withInitials("BJ").build();
        assertThat(ross.familyName(), is("Watling"));
        assertThat(ross.givenName(), is("BJ"));
        assertThat(ross.formalGivenNames(), contains("Bradley-John"));
        assertThat(ross.formalName(), is("Bradley-John Watling"));
        assertThat(ross.initialsWithSurname(), is("BJ Watling"));
        assertThat(ross.initials(), is("BJ"));
        assertThat(ross.fullName(), is("BJ Watling"));
    }

    @Test
    public void kusalPerera() {
        Player kusal = Player.player("Mathurage Don Kusal Janith Perera").withGivenName("Kusal").build();
        assertThat(kusal.familyName(), is("Perera"));
        assertThat(kusal.givenName(), is("Kusal"));
        assertThat(kusal.formalGivenNames(), contains("Mathurage", "Don", "Kusal", "Janith"));
        assertThat(kusal.formalName(), is("Mathurage Don Kusal Janith Perera"));
        assertThat(kusal.initialsWithSurname(), is("MDKJ Perera"));
        assertThat(kusal.initials(), is("MDKJ"));
        assertThat(kusal.fullName(), is("Kusal Perera"));
    }

    @Test
    public void rassieVanDerDussen() {
        Player rassie = Player.player("Hendrik Erasmus van der Dussen").withGivenName("Rassie").build();
        assertThat(rassie.familyName(), is("van der Dussen"));
        assertThat(rassie.givenName(), is("Rassie"));
        assertThat(rassie.formalGivenNames(), contains("Hendrik", "Erasmus"));
        assertThat(rassie.formalName(), is("Hendrik Erasmus van der Dussen"));
        assertThat(rassie.initialsWithSurname(), is("HE van der Dussen"));
        assertThat(rassie.initials(), is("HE"));
        assertThat(rassie.fullName(), is("Rassie van der Dussen"));
    }

    @Test
    public void fafDuPlessis() {
        Player faf = Player.player("Francois du Plessis").withGivenName("Faf").build();
        assertThat(faf.familyName(), is("du Plessis"));
        assertThat(faf.givenName(), is("Faf"));
        assertThat(faf.formalGivenNames(), contains("Francois"));
        assertThat(faf.formalName(), is("Francois du Plessis"));
        assertThat(faf.initialsWithSurname(), is("F du Plessis"));
        assertThat(faf.initials(), is("F"));
        assertThat(faf.fullName(), is("Faf du Plessis"));
    }

    @Test
    public void quintonDeKock() {
        Player quinton = Player.player("Quinton de Kock").build();
        assertThat(quinton.familyName(), is("de Kock"));
        assertThat(quinton.givenName(), is("Quinton"));
        assertThat(quinton.formalGivenNames(), contains("Quinton"));
        assertThat(quinton.formalName(), is("Quinton de Kock"));
        assertThat(quinton.initialsWithSurname(), is("Q de Kock"));
        assertThat(quinton.initials(), is("Q"));
        assertThat(quinton.fullName(), is("Quinton de Kock"));
    }

    @Test
    public void klRahul() {
        Player kl = Player.player("Kannaur Lokesh Rahul").withGivenName("KL").build();
        assertThat(kl.familyName(), is("Rahul"));
        assertThat(kl.givenName(), is("KL"));
        assertThat(kl.formalGivenNames(), contains("Kannaur", "Lokesh"));
        assertThat(kl.formalName(), is("Kannaur Lokesh Rahul"));
        assertThat(kl.initialsWithSurname(), is("KL Rahul"));
        assertThat(kl.initials(), is("KL"));
        assertThat(kl.fullName(), is("KL Rahul"));
    }

    @Test
    public void givenNameOrGivenNamesMustBeSet() {
        Player.Builder builder = Player.player().withFamilyName("Flower").withFullName("Han Flower");
        assertThrows(NullPointerException.class, builder::build);
    }

}