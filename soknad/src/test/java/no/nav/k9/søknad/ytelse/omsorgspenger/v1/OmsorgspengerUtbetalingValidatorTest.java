package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.DelvisFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.fravær.FraværÅrsak;
import no.nav.k9.søknad.felles.fravær.SøknadÅrsak;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;

class OmsorgspengerUtbetalingValidatorTest {

    private OmsorgspengerUtbetalingValidator validatorSøknad = new OmsorgspengerUtbetalingValidator(Versjon.of("1.1.0"));

    private Organisasjonsnummer orgnr1 = Organisasjonsnummer.of("999999999");
    private Organisasjonsnummer orgnr2 = Organisasjonsnummer.of("816338352");
    private String arbforholdId1 = "123";

    @Test
    void skal_returnere_ingen_feil_for_minimum_json_søknad() {
        var søknad = TestUtils.minimumJsonSøknad();

        var feil = new OmsorgspengerUtbetalingSøknadValidator().valider(søknad);

        assertThat(feil).isEmpty();
    }

    @Test
    void skal_returnere_ingen_feil_for_komplett_søknad() {
        var fulltFraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var delvisFraværPeriode = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fulltFraværPeriode, null),
                lagFraværskorrigeringImGammelVariant(orgnr1, arbforholdId1, delvisFraværPeriode, Duration.ofHours(4)));

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).isEmpty();
    }

    @Test
    void skal_returnere_feil_for_periode_over_flere_år() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-01-01"), LocalDate.parse("2022-01-01"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode, null));

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder", "perioderOverFlereÅr");
    }

    @Test
    void skal_returnere_feil_for_nulling_som_overstiger_enkeltdag() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringImGammelVariant(orgnr1, arbforholdId1, fraværPeriode, Duration.ofHours(0)));

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[0]", "nullingPeriodeOversteget");
    }

    @Test
    void skal_returnere_feil_for_delvis_fravær_som_overstiger_7h_30m() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-01"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringImGammelVariant(orgnr1, arbforholdId1, fraværPeriode, Duration.parse("PT7H31M")));

        var feil = validatorSøknad.valider(ytelse);

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

        var feil = validatorSøknad.valider(ytelse);


        assertThat(feil).hasSize(2);
        feilInneholder(feil, "fraværsperioderKorrigeringIm.perioder[2021-09-01/2021-09-02]", "overlappendePerioder");
    }

    @Test
    void skal_returnere_feil_for_identiske_perioder() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));

        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingMedFraværskorrigeringIm(
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode1, null),
                lagFraværskorrigeringIm(orgnr1, arbforholdId1, fraværPeriode2, null));

        var feil = validatorSøknad.valider(ytelse);


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

        var feil = validatorSøknad.valider(ytelse);

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

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioderKorrigeringIm[1]", "ikkeUnikArbeidsforholdId");
    }

    @Test
    void skal_returnere_feil_for_delvis_fravær_uten_normalarbeidstid_satt() {
        var periode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingSøknadBruker(
                lagSøknadsperiode(orgnr1, periode, new DelvisFravær(null, Duration.ofHours(2))));

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[0].delvisFravær.normalarbeidstid", "manglerNormalarbeidstidForDelvisFravær");
    }

    @Test
    void skal_returnere_feil_for_delvis_fravær_uten_fravær_satt() {
        var periode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingSøknadBruker(
                lagSøknadsperiode(orgnr1, periode, new DelvisFravær(Duration.ofHours(2), null)));

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[0].delvisFravær.fravær", "manglerFraværForDelvisFravær");
    }

    @Test
    void skal_ikke_tillate_overlappende_periode() {
        var periode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var periode2 = new Periode(LocalDate.parse("2021-09-02"), LocalDate.parse("2021-09-03"));
        OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingSøknadBruker(
                lagSøknadsperiode(orgnr1, periode1, null),
                lagSøknadsperiode(orgnr1, periode2, null)
        );

        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(2);
        feilInneholder(feil, "fraværsperioder[2021-09-01/2021-09-02]", "overlappendePerioder");
        feilInneholder(feil, "fraværsperioder[2021-09-02/2021-09-03]", "overlappendePerioder");
    }

    @Test
    void skal_ikke_tillate_at_duration_ikke_tilsvarer_som_delvisFravær() {
        var periode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
            FraværPeriode fraværPeriode = new FraværPeriode(
                    periode,
                    Duration.ofHours(2),
                    new DelvisFravær(Duration.ofHours(4), Duration.ofHours(2)), //50% fravær fra 4 times-jobb skal oppgis som 4 timer i duration ( 50% av 7.5 timer, og rundet oppover til nærmeste halvtime)
                    FraværÅrsak.ORDINÆRT_FRAVÆR,
                    SøknadÅrsak.NYOPPSTARTET_HOS_ARBEIDSGIVER,
                    List.of(AktivitetFravær.ARBEIDSTAKER),
                    Organisasjonsnummer.of("999999999"),
                    null
            );

            OmsorgspengerUtbetaling ytelse = byggOmsorgspengerUtbetalingSøknadBruker(
                fraværPeriode
        );



        var feil = validatorSøknad.valider(ytelse);

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[0]", "avvikDurationOgDelvisFravær");
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

    private OmsorgspengerUtbetaling byggOmsorgspengerUtbetalingSøknadBruker(FraværPeriode... fraværPerioder) {
        return new OmsorgspengerUtbetaling(
                null,
                new OpptjeningAktivitet(),
                Arrays.asList(fraværPerioder),
                List.of(),
                null,
                null
        );
    }

    private FraværPeriode lagFraværskorrigeringImGammelVariant(Organisasjonsnummer organisasjonsnummer, String arbeidsforholdId, Periode søknadsperiode, Duration duration) {
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

    private FraværPeriode lagFraværskorrigeringIm(Organisasjonsnummer organisasjonsnummer, String arbeidsforholdId, Periode søknadsperiode, DelvisFravær delvisFravær) {
        return new FraværPeriode()
                .medPeriode(søknadsperiode)
                .medAktivitetFravær(AktivitetFravær.ARBEIDSTAKER)
                .medDelvisFravær(delvisFravær)
                .medArbeidsgiverOrgNr(organisasjonsnummer)
                .medArbeidsforholdId(arbeidsforholdId);
    }

    private FraværPeriode lagSøknadsperiode(Organisasjonsnummer organisasjonsnummer, Periode søknadsperiode, DelvisFravær delvisFravær) {
        return new FraværPeriode()
                .medPeriode(søknadsperiode)
                .medFraværÅrsak(FraværÅrsak.ORDINÆRT_FRAVÆR)
                .medSøknadsårsak(SøknadÅrsak.NYOPPSTARTET_HOS_ARBEIDSGIVER)
                .medAktivitetFravær(AktivitetFravær.ARBEIDSTAKER)
                .medDelvisFravær(delvisFravær)
                .medArbeidsgiverOrgNr(organisasjonsnummer);
    }

    static class TestUtils {

        private static String jsonFromFile(String filename) {
            try {
                return Files.readString(Path.of("src/test/resources/ytelse/omp/utbetaling/" + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static Søknad minimumJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad-omp-utbetaling-snf.json"));
        }
    }
}
