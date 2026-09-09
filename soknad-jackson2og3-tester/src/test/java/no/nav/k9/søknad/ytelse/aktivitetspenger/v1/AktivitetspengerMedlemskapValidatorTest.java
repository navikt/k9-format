package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Arbeidssteder;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Bosteder;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Medlemskap;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AktivitetspengerMedlemskapValidatorTest {
    private final LocalDate SØKNADSPERIODE_FOM = LocalDate.now();
    private final Periode BOSTED_PERIODE = new Periode(LocalDate.now().minusYears(1), LocalDate.now().minusMonths(6));
    private final Periode ARBEID_PERIODE = new Periode(LocalDate.now().minusMonths(5), LocalDate.now().minusMonths(1));

    private final AktivitetspengerSøknadValidator søknadValidator = new AktivitetspengerSøknadValidator();

    @Test
    void skalValidereOk() {
        Aktivitetspenger ytelse = ytelse(new Medlemskap()
                .medBostederUtenforNorge(bosteder(BOSTED_PERIODE, Landkode.SVERIGE))
                .medArbeidsstederUtenforNorge(arbeidssteder(ARBEID_PERIODE, Landkode.DANMARK, "010185-1234")));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).isEmpty();
    }

    @Test
    void TomtMedlemskapSkalValidereOk() {
        List<Feil> feil = søknadValidator.valider(søknad(ytelse(new Medlemskap())));
        assertThat(feil).isEmpty();
    }

    @Test
    void BostedUtenLandSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(new Medlemskap().medBostederUtenforNorge(
                new Bosteder().medPerioder(Map.of(BOSTED_PERIODE, new Bosteder.BostedPeriodeInfo()))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.bostederUtenforNorge.perioder['" + BOSTED_PERIODE.getIso8601() + "'].land");
    }

    @Test
    void ArbeidsstedUtenLandSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(new Medlemskap().medArbeidsstederUtenforNorge(
                new Arbeidssteder().medPerioder(Map.of(ARBEID_PERIODE, new Arbeidssteder.ArbeidsstedPeriodeInfo().medIdentitetsnummer("010185-1234")))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.arbeidsstederUtenforNorge.perioder['" + ARBEID_PERIODE.getIso8601() + "'].land");
    }

    @Test
    void ArbeidsstedUtenIdentitetsnummerSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(new Medlemskap().medArbeidsstederUtenforNorge(
                new Arbeidssteder().medPerioder(Map.of(ARBEID_PERIODE, new Arbeidssteder.ArbeidsstedPeriodeInfo().medLand(Landkode.DANMARK)))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.arbeidsstederUtenforNorge.perioder['" + ARBEID_PERIODE.getIso8601() + "'].identitetsnummer");
    }

    @Test
    void PeriodeUtenTilOgMedSkalKasteFeil() {
        Periode åpenPeriode = new Periode(LocalDate.now().minusMonths(5), null);
        Aktivitetspenger ytelse = ytelse(new Medlemskap()
                .medArbeidsstederUtenforNorge(arbeidssteder(åpenPeriode, Landkode.DANMARK, "010185-1234")));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).isNotEmpty();
        assertThat(feil.get(0).getFelt()).startsWith("ytelse.medlemskap.arbeidsstederUtenforNorge.perioder");
    }

    @Test
    void IdentitetsnummerSkalIkkeLekkeIToString() {
        var periodeInfo = new Arbeidssteder.ArbeidsstedPeriodeInfo()
                .medLand(Landkode.DANMARK)
                .medIdentitetsnummer("010185-1234");

        assertThat(periodeInfo.toString()).doesNotContain("010185-1234");
    }

    private Bosteder bosteder(Periode periode, Landkode land) {
        return new Bosteder().medPerioder(Map.of(periode, new Bosteder.BostedPeriodeInfo().medLand(land)));
    }

    private Arbeidssteder arbeidssteder(Periode periode, Landkode land, String identitetsnummer) {
        return new Arbeidssteder().medPerioder(Map.of(periode,
                new Arbeidssteder.ArbeidsstedPeriodeInfo().medLand(land).medIdentitetsnummer(identitetsnummer)));
    }

    private Aktivitetspenger ytelse(Medlemskap medlemskap) {
        return new Aktivitetspenger().medSøknadsperiodeFom(SØKNADSPERIODE_FOM).medMedlemskap(medlemskap);
    }

    private Søknad søknad(Aktivitetspenger ytelse) {
        return new Søknad(
                new SøknadId("1"),
                new Versjon("1.0.0"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                ytelse);
    }
}
