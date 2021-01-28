package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
import no.nav.k9.søknad.felles.aktivitet.Arbeidstaker;
import no.nav.k9.søknad.felles.aktivitet.Frilanser;
import no.nav.k9.søknad.felles.aktivitet.Organisasjonsnummer;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.aktivitet.VirksomhetType;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;

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
        Tilsynsordning tilsynsordning = new Tilsynsordning(Map.of(
                new Periode(LocalDate.now(), LocalDate.now()),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        søknad.setTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                new Periode(søknadsperiode.getFraOgMed().plusDays(7), søknadsperiode.getTilOgMed().minusDays(7)),
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        assertThat(verifyHarFeil(søknad)).hasSize(2);

    }

    @Test
    public void søknadMedNullJobberNormaltTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(null, Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(null)));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(-8), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(-7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFeilArbeidstaker() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, null, arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknad = TestUtils.komplettBuilder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                søknadsperiode,
                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker, arbeidstidInfo);
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

    private List<Feil> verifyHarFeil(PleiepengerSyktBarn builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(PleiepengerSyktBarn builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad.getYtelse());
        assertThat(feil).isEmpty();
    }

    private List<Feil> valider(PleiepengerSyktBarn builder) {
        try {
            return validator.valider(builder);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
}
