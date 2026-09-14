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
    void boddINorgeOgJobbetUtenforNorgeSkalKreveUtenlandsopphold() {
        assertThat(feilFor(medlemskapMedFlagg(true, null, true, true))).isEmpty();
    }

    @Test
    void boddINorgeOgIkkeJobbetUtenforNorgeSkalVæreGyldigUtenUtenlandsopphold() {
        assertThat(feilFor(medlemskapMedFlagg(true, null, false, false))).isEmpty();
    }

    @Test
    void ikkeBoddINorgeMenJobbetINorgeOgUtenforNorgeSkalKreveUtenlandsopphold() {
        assertThat(feilFor(medlemskapMedFlagg(false, true, true, true))).isEmpty();
    }

    @Test
    void ikkeBoddINorgeOgIkkeJobbetINorgeSkalKreveUtenlandsopphold() {
        assertThat(feilFor(medlemskapMedFlagg(false, false, null, true))).isEmpty();
    }

    @Test
    void ikkeBoddINorgeMenJobbetINorgeOgIkkeUtenforNorgeSkalVæreGyldigUtenUtenlandsopphold() {
        assertThat(feilFor(medlemskapMedFlagg(false, true, false, false))).isEmpty();
    }

    // Ugyldige kombinasjoner: utenlandsopphold stemmer ikke med flaggene
    @Test
    void boddINorgeOgJobbetUtenforNorgeUtenUtenlandsoppholdSkalKasteFeil() {
        assertThat(feilFor(medlemskapMedFlagg(true, null, true, false))).isNotEmpty();
    }

    @Test
    void ikkeBoddINorgeOgIkkeJobbetINorgeUtenUtenlandsoppholdSkalKasteFeil() {
        assertThat(feilFor(medlemskapMedFlagg(false, false, null, false))).isNotEmpty();
    }

    @Test
    void ikkeBoddINorgeMenJobbetINorgeOgUtenforNorgeUtenUtenlandsoppholdSkalKasteFeil() {
        assertThat(feilFor(medlemskapMedFlagg(false, true, true, false))).isNotEmpty();
    }

    @Test
    void harBoddINorgeIkkeSattSkalKasteFeil() {
        assertThat(feilFor(medlemskapMedFlagg(null, null, null, false))).isNotEmpty();
    }

    private List<Feil> feilFor(Medlemskap medlemskap) {
        return søknadValidator.valider(søknad(ytelse(medlemskap)));
    }

    private Medlemskap medlemskapMedFlagg(Boolean harBoddINorge, Boolean harJobbetINorge, Boolean harJobbetUtenforNorge, boolean harUtenlandsopphold) {
        Map<Periode, UtenlandsoppholdPeriodeInfo> perioder = harUtenlandsopphold
                ? Map.of(PERIODE, periodeInfo(Landkode.SVERIGE, true, null))
                : Map.of();
        return new Medlemskap(harBoddINorge, harJobbetINorge, harJobbetUtenforNorge, new Utenlandsopphold(perioder));
    }


    @Test
    void utenlandsoppholdUtenLandSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, new UtenlandsoppholdPeriodeInfo(null, false, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].land");
    }

    @Test
    void utenlandsoppholdUtenJobbetIPeriodenSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, new UtenlandsoppholdPeriodeInfo(Landkode.SVERIGE, null, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].jobbetIPerioden");
    }

    @Test
    void periodeUtenTilOgMedSkalKasteFeil() {
        Periode åpenPeriode = new Periode(LocalDate.now().minusMonths(5), null);
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(åpenPeriode, periodeInfo(Landkode.DANMARK, true, null))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).isNotEmpty();
        assertThat(feil.get(0).getFelt()).startsWith("ytelse.medlemskap.utenlandsopphold.perioder");
    }

    @Test
    void overlappendePerioderSkalKasteFeil() {
        var overlappende = new LinkedHashMap<Periode, UtenlandsoppholdPeriodeInfo>();
        overlappende.put(new Periode(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30)),
                periodeInfo(Landkode.SVERIGE, false, null));
        overlappende.put(new Periode(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 5, 31)),
                periodeInfo(Landkode.DANMARK, true, null));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse(medlemskap(overlappende))));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder");
        assertThat(feil.get(0).getFeilmelding()).contains("2024-01-01/2024-06-30").contains("2024-03-01/2024-05-31");
    }

    @Test
    void identitetsnummerSkalIkkeLekkeIToString() {
        var periodeInfo = periodeInfo(Landkode.DANMARK, true, UTENLANDSK_NID);

        assertThat(periodeInfo.toString()).doesNotContain(UTENLANDSK_NID);
    }

    @Test
    void identitetsnummerMedLinjeskiftSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, "19850101\n-1234"))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].utenlandskNasjonalId");
    }

    @Test
    void forLangtIdentitetsnummerSkalKasteFeil() {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, "1".repeat(51)))));

        List<Feil> feil = søknadValidator.valider(søknad(ytelse));
        assertThat(feil).hasSize(1);
        assertThat(feil.get(0).getFelt()).isEqualTo("ytelse.medlemskap.utenlandsopphold.perioder['" + PERIODE.getIso8601() + "'].utenlandskNasjonalId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"19850101-1234", "QQ123456C", "12345678901", "ÅKE 123/45", "010185.1234"})
    void utenlandskeIdentitetsnummerformaterSkalValidereOk(String utenlandskNId) {
        Aktivitetspenger ytelse = ytelse(medlemskap(Map.of(
                PERIODE, periodeInfo(Landkode.SVERIGE, true, utenlandskNId))));

        assertThat(søknadValidator.valider(søknad(ytelse))).isEmpty();
    }

    @Test
    void valideringsmetodeSkalIkkeSerialiseres() {
        Søknad søknad = søknad(ytelse(medlemskap(Map.of(PERIODE, periodeInfo(Landkode.SVERIGE, false, null)))));

        assertThat(JsonUtils.toString(søknad)).doesNotContain("harIngenOverlappendePerioder");
    }

    @Test
    void skalKunneSerialiseresOgDeserialiseres() {
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
        return new Medlemskap(true, null, true, new Utenlandsopphold(perioder));
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
