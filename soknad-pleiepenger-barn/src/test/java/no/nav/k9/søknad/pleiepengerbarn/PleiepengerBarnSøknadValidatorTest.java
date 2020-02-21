
package no.nav.k9.søknad.pleiepengerbarn;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PleiepengerBarnSøknadValidatorTest {
    private static final PleiepengerBarnSøknadValidator validator = new PleiepengerBarnSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        PleiepengerBarnSøknad.Builder builder = PleiepengerBarnSøknad.builder();
        PleiepengerBarnSøknad søknad = PleiepengerBarnSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        final PleiepengerBarnSøknad søknad = TestUtils.komplettSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    public void mottattDatoErPåkrevd() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();

        builder.mottattDato(null);
        verifyHarFeil(builder);

        builder.mottattDato(ZonedDateTime.now());
        verifyIngenFeil(builder);
    }

    @Test
    public void søkerNorskIdentitetsnummerPåkrevd() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();

        builder.søker(Søker.builder().build());
        verifyHarFeil(builder);

        builder.søker(Søker.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        verifyIngenFeil(builder);
    }

    @Test
    public void søknadsperiodeErPåkrevd() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();

        builder.søknadsperioder = new HashMap<>();
        verifyHarFeil(builder);

        builder.søknadsperiode(
                Periode.builder()
                        .fraOgMed(LocalDate.now())
                        .tilOgMed(LocalDate.now().plusDays(1))
                        .build(),
                        SøknadsperiodeInfo.builder().build())
                .build();
        verifyIngenFeil(builder);
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();
        Tilsynsordning tilsynsordning = Tilsynsordning.builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .opphold(Periode.builder().enkeltDag(LocalDate.now()).build(), TilsynsordningOpphold
                        .builder()
                        .lengde(Duration.ofDays(2))
                        .build())
                .build();

        builder.tilsynsordning(tilsynsordning);

        verifyHarFeil(builder);
    }

    @Test
    public void barnMedBådeFødselsdatoOgNorskIdentitetsnummer() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();
        final Barn barn = Barn.builder()
                .fødselsdato(LocalDate.now())
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                .build();
        builder.barn(barn);
        verifyHarFeil(builder);
    }

    @Test
    public void søknadMedUgyldigInfoOmArbeid() {
        final PleiepengerBarnSøknad.Builder builder = TestUtils.komplettBuilder();
        Arbeid arbeid = Arbeid
                .builder()
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                        .periode(
                            Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                            Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(120.00)).build()
                        ).build()
                ).build();

        builder.arbeid(arbeid);
        verifyHarFeil(builder);

        arbeid = Arbeid
                .builder()
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(100.00)).build()
                        ).build()
                ).build();
        builder.arbeid(arbeid);
        verifyHarFeil(builder);

        arbeid = Arbeid
                .builder()
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(-20.00)).build()
                        ).build()
                ).build();

        builder.arbeid(arbeid);
        verifyHarFeil(builder);

        arbeid = Arbeid
                .builder()
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(20.00)).build()
                        ).build()
                ).build();

        builder.arbeid(arbeid);
        verifyHarFeil(builder);
    }

    private List<Feil> verifyHarFeil(PleiepengerBarnSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(PleiepengerBarnSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private void verifyIngenFeil(PleiepengerBarnSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }
    private void verifyIngenFeil(PleiepengerBarnSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private List<Feil> valider(PleiepengerBarnSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
}
