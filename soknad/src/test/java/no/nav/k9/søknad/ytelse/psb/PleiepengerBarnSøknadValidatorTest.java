package no.nav.k9.søknad.ytelse.psb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.Opptjening;
import no.nav.k9.søknad.felles.opptjening.arbeidstaker.Arbeidstaker;
import no.nav.k9.søknad.felles.opptjening.snf.Frilanser;
import no.nav.k9.søknad.felles.opptjening.snf.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnValidator;
import no.nav.k9.søknad.ytelse.psb.v1.SøknadsperiodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningOpphold;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningSvar;

public class PleiepengerBarnSøknadValidatorTest {
    private static final PleiepengerSyktBarnValidator validator = new PleiepengerSyktBarnValidator();

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadsperiodeErPåkrevd() {
        var builder = TestUtils.komplettBuilder();

        builder.setPerioder(new HashMap<>());
        verifyHarFeil(builder);

        builder.setPerioder(Map.of(Periode.builder()
                        .fraOgMed(LocalDate.now())
                        .tilOgMed(LocalDate.now().plusDays(1))
                        .build(),
                SøknadsperiodeInfo.builder().build()));
        verifyIngenFeil(builder);
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknad = TestUtils.komplettBuilder();
        Tilsynsordning tilsynsordning = Tilsynsordning.builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .opphold(Periode.builder().enkeltDag(LocalDate.now()).build(),
                        TilsynsordningOpphold.builder()
                                .lengde(Duration.ofDays(2))
                                .build())
                .build();

        søknad.setTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedUgyldigInfoOmArbeid() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = new ArrayList<>(søknad.getArbeidstaker());
        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(120.00))
                                .build())
                .build());

        søknad.setArbeidstaker(arbeid);
        verifyHarFeil(søknad);

        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(100.00))
                                .build())
                .build());
        søknad.setArbeidstaker(arbeid);
        verifyHarFeil(søknad);

        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(-20.00))
                                .build())
                .build());

        søknad.setArbeidstaker(arbeid);
        verifyHarFeil(søknad);

        arbeid.add(Arbeidstaker.builder()
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(20.00))
                                .build())
                .build());

        søknad.setArbeidstaker(arbeid);
        verifyHarFeil(søknad);
    }

    @Test
    public void ÅpneOgOverlappendePerioderForFrilanserOgSelvstendig() {
        var søknad = TestUtils.komplettBuilder();
        var selvstendig = SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder().build();

        var opptjening = Opptjening
                .builder()
                .frilanser(Frilanser.builder()
                        .startdato(LocalDate.parse("2020-01-01"))
                        .jobberFortsattSomFrilans(true)
                        .build())
                .selvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                        .periode(Periode.parse("2020-01-01/2020-02-02"), selvstendig)
                        .periode(Periode.parse("2020-01-05/2020-02-01"), selvstendig)
                        .periode(Periode.parse("2020-01-01/.."), selvstendig)
                        .periode(Periode.parse("2020-01-05/.."), selvstendig)
                        .build()
                ).build();
        søknad.setOpptjening(opptjening);
        verifyIngenFeil(søknad);
    }

    @Test
    public void ArbeidstakerInfoUtenJobberNormaltPerUke() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = new ArrayList<>(søknad.getArbeidstaker());
        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(100.00))
                                .jobberNormaltPerUke(null)
                                .build())
                .build());

        søknad.setArbeidstaker(arbeid);

        Assertions.assertThat(verifyHarFeil(søknad)).hasSize(1);
    }

    @Test
    public void ArbeidstakerInfoMedJobberNormaltPerUkeOverEnUke() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = new ArrayList<>(søknad.getArbeidstaker());
        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(100.00))
                                .jobberNormaltPerUke(Duration.ofDays(7).plusSeconds(1))
                                .build()
                ).build());

        søknad.setArbeidstaker(arbeid);

        Assertions.assertThat(verifyHarFeil(søknad)).hasSize(1);
    }

    @Test
    public void ArbeidstakerInfoMedJobberNormaltPerUkeSattTilNegativVerdi() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = new ArrayList<>(søknad.getArbeidstaker());
        arbeid.add(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                .periode(Periode.builder()
                                .fraOgMed(LocalDate.now())
                                .tilOgMed(LocalDate.now().plusDays(3))
                                .build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(100.00))
                                .jobberNormaltPerUke(Duration.ZERO.minusDays(1))
                                .build()).build());

        søknad.setArbeidstaker(arbeid);

        Assertions.assertThat(verifyHarFeil(søknad)).hasSize(1);
    }

    private List<Feil> verifyHarFeil(PleiepengerSyktBarn builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(PleiepengerSyktBarn builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad.getYtelse());
        assertThat(feil, is(Collections.emptyList()));
    }

    private List<Feil> valider(PleiepengerSyktBarn builder) {
        try {
            return validator.valider(builder);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
}
