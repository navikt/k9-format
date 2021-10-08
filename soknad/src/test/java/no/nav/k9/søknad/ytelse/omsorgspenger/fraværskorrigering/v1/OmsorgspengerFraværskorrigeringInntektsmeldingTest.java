package no.nav.k9.søknad.ytelse.omsorgspenger.fraværskorrigering.v1;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.fravær.FraværÅrsak;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;

class OmsorgspengerFraværskorrigeringInntektsmeldingTest {

    private OmsorgspengerFraværskorrigeringInntektsmeldingValidator validatorSøknad = new OmsorgspengerFraværskorrigeringInntektsmeldingValidator();

    private Organisasjonsnummer orgnr1 = Organisasjonsnummer.of("999999999");
    private Organisasjonsnummer orgnr2 = Organisasjonsnummer.of("816338352");
    private UUID internRef = UUID.randomUUID();

    @Test
    public void skal_returnere_ingen_feil_for_komplett_søknad() {
        var fulltFraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var delvisFraværPeriode = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, null, fulltFraværPeriode, null),
                lagFraværsperiode(orgnr1, null, delvisFraværPeriode, Duration.ofHours(4)))
        );

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).isEmpty();
    }

    @Test
    public void skal_returnere_feil_for_nulling_som_overstiger_enkeltdag() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, null, fraværPeriode, Duration.ofHours(0))
        ));

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[0]", "nullingPeriodeOversteget");
    }

    @Test
    public void skal_returnere_feil_for_delvis_fravær_som_overstiger_7h_30m() {
        var fraværPeriode = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-01"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, null, fraværPeriode, Duration.parse("PT7H31M"))
        ));

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[0]", "varighetOversteget");
    }

    @Test
    public void skal_returnere_feil_for_overlappende_perioder() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-02"), LocalDate.parse("2021-09-03"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, null, fraværPeriode1, null),
                lagFraværsperiode(orgnr1, null, fraværPeriode2, null)
        ));

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).hasSize(2);
        feilInneholder(feil, "fraværsperioder.perioder[2021-09-01/2021-09-02]", "overlappendePerioder");
    }

    @Test
    public void skal_returnere_feil_for_flere_orgnr() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, null, fraværPeriode1, null),
                lagFraværsperiode(orgnr2, null, fraværPeriode2, null)
        ));

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[1]", "ikkeUniktOrgNr");
    }

    @Test
    public void skal_returnere_feil_for_flere_arbeidsforhold_id() {
        var fraværPeriode1 = new Periode(LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"));
        var fraværPeriode2 = new Periode(LocalDate.parse("2021-09-03"), LocalDate.parse("2021-09-04"));
        var ytelse = new OmsorgspengerFraværskorrigeringInntektsmelding(List.of(
                lagFraværsperiode(orgnr1, internRef, fraværPeriode1, null),
                lagFraværsperiode(orgnr1, null, fraværPeriode2, null)
        ));

        var feil = validatorSøknad.valider(ytelse);;

        assertThat(feil).hasSize(1);
        feilInneholder(feil, "fraværsperioder[1]", "ikkeUnikArbeidsforholdId");
    }

    private FraværPeriode lagFraværsperiode(Organisasjonsnummer organisasjonsnummer, UUID internRef, Periode søknadsperiode, Duration duration) {
        return new FraværPeriode(
                søknadsperiode,
                duration,
                FraværÅrsak.ORDINÆRT_FRAVÆR,
                null,
                List.of(AktivitetFravær.ARBEIDSTAKER),
                organisasjonsnummer,
                internRef
        );
    }

}
