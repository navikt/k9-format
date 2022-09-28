package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Språk;
import no.nav.k9.søknad.felles.type.SøknadId;

/**
 * test av tidligere versjon (1.0.0)
 * <p>
 * målet er å bevare støtte for gamle versjoner *
 */
class OmsorgspengerUtbetalingValidatorV1_0_0Test {

    private OmsorgspengerUtbetalingValidator validatorYtelse = new OmsorgspengerUtbetalingValidator(Versjon.of("1.0.0"));
    private SøknadValidator<Søknad> søknadValidator = new OmsorgspengerUtbetalingSøknadValidator();

    private Organisasjonsnummer orgnr1 = Organisasjonsnummer.of("999999999");
    private Organisasjonsnummer orgnr2 = Organisasjonsnummer.of("816338352");
    private String arbforholdId1 = "123";

    @Test
    void skal_returnere_ingen_feil_for_minimum_json_søknad() {
        var søknad = TestUtils.minimumJsonSøknad();

        List<Feil> feil = søknadValidator.valider(søknad);

        assertThat(feil).isEmpty();
    }

    @Test
    void skal_returnere_ingen_feil_for_komplett_json_søknad() {
        var søknad = TestUtils.komplettJsonSøknad();

        List<Feil> feil = søknadValidator.valider(søknad);

        assertThat(feil).isEmpty();
    }

    @Test
    void skal_returnere_ingen_feil_for_komplett_søknad() {
        var fulltFraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var delvisFraværPeriode = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fulltFraværPeriode, null),
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, delvisFraværPeriode, Duration.ofHours(4)));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).isEmpty();
    }

    @Test
    void skal_returnere_feil_for_periode_over_flere_år() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-01-01"), LocalDate.parse("2022-01-01"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode, null));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder", "perioderOverFlereÅr");
    }

    @Test
    void skal_returnere_feil_for_nulling_som_overstiger_enkeltdag() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode, Duration.ofHours(0)));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[0]", "nullingPeriodeOversteget");
    }

    @Test
    void skal_returnere_feil_for_delvis_fravær_som_overstiger_7h_30m() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-01"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode, Duration.parse("PT7H31M")));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[0]", "varighetOversteget");
    }

    @Test
    void skal_returnere_feil_for_overlappende_perioder() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-02"), LocalDate.parse("2021-09-03"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode1, null),
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode2, null));

        List<Feil> feil = lagSøknadOgValider(ytelse);


        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm.perioder[0, 1]", "overlappendePerioder");
    }

    @Test
    void skal_returnere_feil_for_identiske_perioder() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode1, null),
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode2, null));

        List<Feil> feil = lagSøknadOgValider(ytelse);


        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm.perioder[2021-09-01/2021-09-02]", "likePerioder");
    }

    @Test
    void skal_returnere_feil_for_flere_orgnr() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));
        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode1, null),
                lagFraværskorrigeringIm(orgnr2, arbforholdId1, fraværPeriode2, null));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[1]", "ikkeUniktOrgNr");
    }

    @Test
    void skal_returnere_feil_for_flere_arbeidsforhold_id() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));
        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode1, null),
                lagFraværskorrigeringIm(orgnr1, null, fraværPeriode2, null));

        List<Feil> feil = lagSøknadOgValider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[1]", "ikkeUnikArbeidsforholdId");
    }

    // Søknad med fraværskorrigering av IM

    private OmsorgspengerUtbetaling byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(FraværPeriode... fraværPerioder) {
        return new OmsorgspengerUtbetaling(
                null,
                new OpptjeningAktivitet(),
                List.of(),
                Arrays.asList(fraværPerioder),
                null,
                null
        );
    }

    private FraværPeriode lagFraværskorrigeringIm(Organisasjonsnummer organisasjonsnummer, String arbeidsforholdId, Periode søknadsperiode, Duration duration) {
        return new FraværPeriode(
                søknadsperiode,
                duration,
                null,
                null,
                null,
                List.of(AktivitetFravær.ARBEIDSTAKER),
                organisasjonsnummer,
                arbeidsforholdId
        );
    }

    static class TestUtils {

        private static String jsonFromFile(String filename) {
            try {
                return Files.readString(Path.of("src/test/resources/ytelse/omp/utbetaling/v1.0.0/" + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static Søknad minimumJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad-omp-utbetaling-snf.json"));
        }

        static Søknad komplettJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad-omp-utbetaling-snf.json"));
        }

    }

    private List<Feil> lagSøknadOgValider(OmsorgspengerUtbetaling ytelse) {
        Søknad søknad = lagSøknad(ytelse);
        return søknadValidator.valider(søknad);
    }

    private Søknad lagSøknad(OmsorgspengerUtbetaling ytelse) {
        return new Søknad(
                new SøknadId("foo"),
                new Versjon("1.0.0"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("11111111111")),
                Språk.NORSK_BOKMÅL,
                ytelse);
    }
}
