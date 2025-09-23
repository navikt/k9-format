package no.nav.k9.søknad.ytelse.olp.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kurs;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kursholder;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Reise;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class OpplæringspengerYtelseValidatorTest {
    private final Periode SØKNADSPERIODE = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(1));
    private final Periode GYLDIG_ENDRINGS_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
    private final Reise REISEDAG_I_DAG = new Reise(true, List.of(LocalDate.now()), "Langt å kjøre");
    private final Reise INGEN_REISEDAG = new Reise(false, List.of(), null);

    private final OpplæringspengerYtelseValidator ytelseValidator = new OpplæringspengerYtelseValidator();

    @Test
    void skalValidereOk() {
        Kurs kurs = new Kurs(new Kursholder(UUID.randomUUID()), List.of(SØKNADSPERIODE), REISEDAG_I_DAG);
        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(SØKNADSPERIODE)).medUttak(YtelseEksempel.lagUttak(SØKNADSPERIODE)).medKurs(kurs);

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil).isEmpty();
    }

    @Test
    void KursperioderIkkeLikSøknadsperiodeSkalKasteFeil() {
        Periode kursPeriodeSomIkkeErLikSøknadsperiode = new Periode(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Kurs kurs = new Kurs(new Kursholder(UUID.randomUUID()), List.of(kursPeriodeSomIkkeErLikSøknadsperiode), REISEDAG_I_DAG);

        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(SØKNADSPERIODE)).medUttak(YtelseEksempel.lagUttak(SØKNADSPERIODE)).medKurs(kurs);

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil.size()).isEqualTo(1);
        assertThat(feil.get(0).getFeilmelding()).contains("Periodene er ikke komplett, periode som mangler er:");
    }

    @Test
    void TrekkAvKravPerioderSkalVæreOk() {
        Periode trekkKravPeriode = new Periode(GYLDIG_ENDRINGS_PERIODE.getFraOgMed(), GYLDIG_ENDRINGS_PERIODE.getFraOgMed());
        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of()).addTrekkKravPeriode(trekkKravPeriode);

        List<Feil> feil = ytelseValidator.valider(olpYtelse, List.of(GYLDIG_ENDRINGS_PERIODE));
        assertThat(feil).isEmpty();
    }

    @Test
    void TrekkAvPeriodeOgLeggTilNyttKursIAnnenPeriodeSkalVæreOk() {
        Periode trekkKravPeriode = new Periode(GYLDIG_ENDRINGS_PERIODE.getFraOgMed(), GYLDIG_ENDRINGS_PERIODE.getFraOgMed().plusWeeks(1));
        Periode nyKursPeriode = new Periode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(4));
        Kurs kurs = new Kurs(new Kursholder(UUID.randomUUID()), List.of(nyKursPeriode), INGEN_REISEDAG);

        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(nyKursPeriode)).addAllTrekkKravPerioder(List.of(trekkKravPeriode)).medKurs(kurs);
        List<Feil> feil = ytelseValidator.valider(olpYtelse, List.of(GYLDIG_ENDRINGS_PERIODE));

        assertThat(feil.size()).isEqualTo(0);
    }

    // test at trekkKravPeriode overlapper med søknadsperiode gir feil
    @Test
    void TrekkKravPeriodeOverlapperMedSøknadsperiodeGirFeil() {
        Periode trekkKravPeriode = new Periode(SØKNADSPERIODE.getFraOgMed().minusDays(1), SØKNADSPERIODE.getTilOgMed().plusDays(1));
        Kurs kurs = new Kurs(new Kursholder(UUID.randomUUID()), List.of(SØKNADSPERIODE), REISEDAG_I_DAG);
        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(SØKNADSPERIODE)).addTrekkKravPeriode(trekkKravPeriode).medKurs(kurs);

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil.size()).isEqualTo(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.trekkKravPerioder");
        assertThat(feil.get(0).getFeilmelding()).contains("Overlapper med trekk krav periode:");
    }

    @Test
    void SøknadsperiodeUtenKursSomOverlapperTrekkKreavPeriodeGir2Feil() {
        Periode trekkKravPeriode = new Periode(SØKNADSPERIODE.getFraOgMed().minusDays(1), SØKNADSPERIODE.getTilOgMed().plusDays(1));
        Opplæringspenger olpYtelse = new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(SØKNADSPERIODE)).addTrekkKravPeriode(trekkKravPeriode);

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil.size()).isEqualTo(2);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.trekkKravPerioder");
        assertThat(feil.get(0).getFeilmelding()).contains("Overlapper med trekk krav periode:");
        assertThat(feil.get(1).getFelt()).isEqualTo("ytelse.kurs.kursperioder");
        assertThat(feil.get(1).getFeilmelding()).contains("Periodene er ikke komplett, periode som mangler er:");
    }
}
