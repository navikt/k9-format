package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Medlemskap;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Utenlandsopphold;
import no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap.Utenlandsopphold.UtenlandsoppholdPeriodeInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AktivitetspengerMedlemskapValidatorTest {
    public static final String UTENLANDSK_NID = "010185-1234";
    private final LocalDate SØKNADSPERIODE_FOM = LocalDate.now();
    private final Periode PERIODE = new Periode(LocalDate.now().minusYears(1), LocalDate.now().minusMonths(6));
    private final Periode SENERE_PERIODE = new Periode(LocalDate.now().minusMonths(5), LocalDate.now().minusMonths(1));

    private final AktivitetspengerSøknadValidator søknadValidator = new AktivitetspengerSøknadValidator();

    @Test
    void skalValidereOk() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, false, null),
                SENERE_PERIODE, periodeInfo(Landkode.DANMARK, true, UTENLANDSK_NID))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).isEmpty();
    }

    @Test
    void TomtMedlemskapSkalValidereOk() {
        List<Feil> feil = søknadValidator.valider(søknad(ytelse(Medlemskap.tomt())));
        assertThat(feil).isEmpty();
    }


    @Test
    void UtenlandsoppholdUtenLandSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, new UtenlandsoppholdPeriodeInfo(null, false, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].land");
    }

    @Test
    void UtenlandsoppholdUtenJobbetIPeriodenSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, new UtenlandsoppholdPeriodeInfo(Landkode.SVERIGE, null, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].jobbetIPerioden");
    }

    @Test
    void PeriodeUtenTilOgMedSkalKasteFeil() {
        Periode åpenPeriode = new Periode(LocalDate.now().minusMonths(5), null);
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(åpenPeriode, periodeInfo(Landkode.DANMARK, true, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).isNotEmpty();
        assertThat(feil.get(0).getFelt()).startsWith("ytelse.medlemskap.utenlandsopphold.perioder");
    }

    @Test
    void OverlappendePerioderSkalKasteFeil() {
        var overlappende = new LinkedHashMap<Periode, UtenlandsoppholdPeriodeInfo>();
        overlappende.put(new Periode(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30)),
                periodeInfo(Landkode.SVERIGE, false, null));
        overlappende.put(new Periode(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 5, 31)),
                periodeInfo(Landkode.DANMARK, true, null));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse(medlemskap(overlappende))));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.harIngenOverlappendePerioder");
    }

    @Test
    void IdentitetsnummerSkalIkkeLekkeIToString() {
        var periodeInfo = periodeInfo(Landkode.DANMARK, true, UTENLANDSK_NID);

        assertThat(periodeInfo.toString()).doesNotContain(UTENLANDSK_NID);
    }

    @Test
    void IdentitetsnummerMedLinjeskiftSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, "19850101\n-1234"))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].utenlandskNasjonalId");
    }

    @Test
    void ForLangtIdentitetsnummerSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, "1".repeat(51)))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].utenlandskNasjonalId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"19850101-1234", "QQ123456C", "12345678901", "ÅKE 123/45", "010185.1234"})
    void UtenlandskeIdentitetsnummerformaterSkalValidereOk(String utenlandskNId) {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, utenlandskNId))));

        assertThat(søknadValidator.valider(søknad(ytelse))).isEmpty();
    }

    @Test
    void ValideringsmetodeSkalIkkeSerialiseres() {
        Søknad søknad = søknad(ytelse(medlemskap(Map.of(PERIODE, periodeInfo(Landkode.SVERIGE, false, null)))));

        assertThat(JsonUtils.toString(søknad)).doesNotContain("harIngenOverlappendePerioder");
    }

    @Test
    void SkalKunneSerialiseresOgDeserialiseres() {
        Søknad original = søknad(ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, false, null),
                SENERE_PERIODE, periodeInfo(Landkode.DANMARK, true, UTENLANDSK_NID)))));

        Søknad rundtur = JsonUtils.fromString(JsonUtils.toString(original), Søknad.class);

        Aktivitetspenger ytelse = rundtur.getYtelse();
        Map<Periode, UtenlandsoppholdPeriodeInfo> perioder = ytelse.getMedlemskap().utenlandsopphold().perioder();
        assertThat(perioder).hasSize(2);
        assertThat(perioder.get(PERIODE)).isEqualTo(periodeInfo(Landkode.SVERIGE, false, null));
        assertThat(perioder.get(SENERE_PERIODE)).isEqualTo(periodeInfo(Landkode.DANMARK, true, UTENLANDSK_NID));
        assertThat(søknadValidator.valider(rundtur)).isEmpty();
    }

    private UtenlandsoppholdPeriodeInfo periodeInfo(Landkode land, Boolean jobbetIPerioden, String identitetsnummer) {
        return new UtenlandsoppholdPeriodeInfo(land, jobbetIPerioden, identitetsnummer);
    }

    private Medlemskap medlemskap(Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
        return new Medlemskap(new Utenlandsopphold(perioder));
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
