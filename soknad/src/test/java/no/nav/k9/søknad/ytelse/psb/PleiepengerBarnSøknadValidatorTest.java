package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjeningAktivitet.*;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void uttakKanIkkeVæreNull() {
        var builder = TestUtils.komplettYtelsePsb();
        builder.medUttak(null);
        verifyHarFeil(builder);
    }

    @Test
    public void perioderIkkekompletHelg() {
        var søknadsperiodeFom = LocalDate.of(2021, 01, 04);
        var søknadsperiodeTom = LocalDate.of(2021, 01, 31);

        var arbeidstidPeriode = List.of(
                new Periode(søknadsperiodeFom, søknadsperiodeFom.plusDays(4)),
                new Periode(søknadsperiodeFom.plusDays(7), søknadsperiodeFom.plusDays(4+7)),
                new Periode(søknadsperiodeFom.plusDays(14), søknadsperiodeTom));

        var ytelse = TestUtils.komplettYtelsePsb(new Periode(søknadsperiodeFom, søknadsperiodeTom));
        var arbeidstaker = new Arbeidstaker(
                null,
                Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        Map.of(
                                arbeidstidPeriode.get(0), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0)),
                                arbeidstidPeriode.get(1), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0)),
                                arbeidstidPeriode.get(2), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0))
                        )
                )
        );
        ytelse.medArbeidstid(new Arbeidstid(List.of(arbeidstaker), null, null));
        verifyIngenFeil(ytelse);
    }

    @Test
    public void perioderIkkekompletIkkeHelg() {
        var søknadsperiodeFom = LocalDate.of(2021, 01, 04);
        var søknadsperiodeTom = LocalDate.of(2021, 01, 31);

        var arbeidstidPeriode = List.of(
                new Periode(søknadsperiodeFom, søknadsperiodeFom.plusDays(3)),
                new Periode(søknadsperiodeFom.plusDays(7), søknadsperiodeFom.plusDays(4+7)),
                new Periode(søknadsperiodeFom.plusDays(14), søknadsperiodeFom.plusDays(4+14)),
                new Periode(søknadsperiodeFom.plusDays(22), søknadsperiodeTom));

        var ytelse = TestUtils.komplettYtelsePsb(new Periode(søknadsperiodeFom, søknadsperiodeTom));
        var arbeidstaker = new Arbeidstaker(
                null,
                Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        Map.of(
                                arbeidstidPeriode.get(0), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0)),
                                arbeidstidPeriode.get(1), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0)),
                                arbeidstidPeriode.get(2), new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0))
                        )
                )
        );
        ytelse.medArbeidstid(new Arbeidstid(List.of(arbeidstaker), null, null));
        assertThat(valider(ytelse)).size().isEqualTo(2);
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknad = TestUtils.komplettYtelsePsb();
        Tilsynsordning tilsynsordning = new Tilsynsordning(Map.of(
                new Periode(søknad.getSøknadsperiode().getFraOgMed(), søknad.getSøknadsperiode().getTilOgMed().plusDays(10)),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));
        søknad.medTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(
                        søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30)),
                        new Periode(søknadsperiode.getFraOgMed().plusDays(7), søknadsperiode.getTilOgMed().minusDays(7)),
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);

    }

    @Test
    public void søknadMedNullJobberNormaltTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(null, Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), null)));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(-8), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(-7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFeilArbeidstaker() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, null, arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknad = TestUtils.komplettYtelsePsb();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void ÅpneOgOverlappendePerioderForFrilanserOgSelvstendig() {
        var søknad = TestUtils.komplettYtelsePsb();
        var selvstendig = SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                .virksomhetstyper(List.of(VirksomhetType.ANNEN)).build();

        var arbeidAktivitet = OpptjeningAktivitet
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
        søknad.medOpptjeningAktivitet(arbeidAktivitet);
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
