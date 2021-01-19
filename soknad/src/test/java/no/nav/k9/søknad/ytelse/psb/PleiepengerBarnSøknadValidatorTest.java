package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.aktivitet.*;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.ArbeidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningOpphold;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningSvar;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PleiepengerBarnSøknadValidatorTest {
    private static final PleiepengerSyktBarnValidator validator = new PleiepengerSyktBarnValidator();

    @Test
    public void minimumSøknadNullTest() {
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarn();
        JsonUtils.toString(psb);
        verifyIngenFeil(psb);
    }

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadsperiodeKanIkkeVæreNull() {
        var builder = TestUtils.komplettBuilder();
        builder.setSøknadsperiode(null);
        verifyHarFeil(builder);
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknad = TestUtils.komplettBuilder();
        Tilsynsordning tilsynsordning = new Tilsynsordning(TilsynsordningSvar.JA, Map.of(
                new Periode(LocalDate.now(), LocalDate.now()),
                new TilsynsordningOpphold(Duration.ofDays(2))));
        søknad.setTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstaker = new ArrayList<>(søknad.getArbeid().getArbeidstaker());
        assertNotNull(søknadsperiode.getFraOgMed());
        assertNotNull(søknadsperiode.getTilOgMed());
        arbeidstaker.add(
                new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                        søknadsperiode,
                        new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(8)),
                        new Periode(søknadsperiode.getFraOgMed().plusDays(7), søknadsperiode.getTilOgMed().minusDays(7)),
                        new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(8)))));
        søknad.getArbeid().setArbeidstaker(arbeidstaker);
        assertEquals(2, verifyHarFeil(søknad).size());

    }

    @Test
    public void søknadMedNullJobberNormaltTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add(new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), null))));

        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add(new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(null, BigDecimal.valueOf(8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add(new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), null, null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(-20), BigDecimal.valueOf(8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add(new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(-8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFeilArbeidstaker() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add(new Arbeidstaker(null, null, null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add( new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void ÅpneOgOverlappendePerioderForFrilanserOgSelvstendig() {
        var søknad = TestUtils.komplettBuilder();
        var selvstendig = SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                .virksomhetstyper(List.of(VirksomhetType.ANNEN)).build();

        var arbeidAktivitet = ArbeidAktivitet
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
                        .virksomhetNavn("testUlf")
                        .build()
                ).build();
        søknad.setArbeidAktivitet(arbeidAktivitet);
        verifyIngenFeil(søknad);
    }

    @Test
    public void ArbeidstakerInfoUtenJobberNormaltPerUke() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add( new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), null))));
        arbeid.setArbeidstaker(arbeidstaker);
        søknad.setArbeid(arbeid);

        verifyHarFeil(søknad);
    }

    @Test
    public void ArbeidstakerInfoMedJobberNormaltPerUkeSattTilNegativVerdi() {
        var søknad = TestUtils.komplettBuilder();
        var arbeid = søknad.getArbeid();
        var arbeidstaker = new ArrayList<>(arbeid.getArbeidstaker());
        arbeidstaker.add( new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), null, Map.of(
                søknad.getSøknadsperiode(),
                new ArbeidPeriodeInfo(BigDecimal.valueOf(8), BigDecimal.valueOf(-8)))));
        arbeid.setArbeidstaker(arbeidstaker);
        søknad.setArbeid(arbeid);

        verifyHarFeil(søknad);
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
