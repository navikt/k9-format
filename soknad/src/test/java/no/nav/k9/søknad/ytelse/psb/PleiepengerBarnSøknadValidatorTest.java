package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.ytelse.psb.v1.LukketPeriode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;
import no.nav.k9.søknad.ytelse.psb.v1.Beredskap;
import no.nav.k9.søknad.ytelse.psb.v1.Nattevåk;
import no.nav.k9.søknad.ytelse.psb.v1.Omsorg;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnValidator;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class PleiepengerBarnSøknadValidatorTest {
    private static final PleiepengerSyktBarnValidator validator = new PleiepengerSyktBarnValidator();

    @Test
    public void minimumSøknadNullTest() {
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarnMedDelperioder();
        JsonUtils.toString(psb);
        verifyIngenFeil(psb);
    }

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void minimumSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.minimumSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void komplettSøknadGammelVersjonSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettGammelVersjonSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void minimumSøknadGammelVersjonSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.minimumGammelVersjonSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void komplettSøknadSkalIkkeHaFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(3));
        var psb = TestUtils.komplettYtelsePsb(søknadsperiode);
        verifyIngenFeil(psb);
    }

    @Test
    public void minimumSøknadMedOmsorgNullPåFelterSkalIkkeHaValideringsfeil() {
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarnMedDelperioder();
        psb.medOmsorg(new Omsorg().medBeskrivelseAvOmsorgsrollen(null).medRelasjonTilBarnet(null));
        verifyIngenFeil(psb);
    }

    @Test void søknadPerioderFeilFomFørTom() {
        var søknadperiode = new Periode(LocalDate.now().plusDays(10), LocalDate.now().minusDays(10));
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarn(søknadperiode);

        final List<Feil> feil = valider(psb);
        assertThat(feil).isNotEmpty();
    }

    @Test
    public void uttakKanIkkeVæreTom() {
        var ytelse = TestUtils.komplettYtelsePsbMedDelperioder();
        ytelse.medUttak(new Uttak());
        verifyHarFeil(ytelse);
    }

    @Test
    public void endringssøknadUtenFeil() {
        var endringsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(1));
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(1));
        var ytelse = TestUtils.fullEndringssøknad(periode, endringsperiode);

        verifyIngenFeil(ytelse);
    }

    @Test
    public void søknadOgEndringUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var endringssøknad = TestUtils.minimumSøknadOgEndringsSøknad(søknadsperiode, endringsperiode);
        verifyIngenFeil(endringssøknad);

    }

    @Test
    public void søknadOgEndringMedFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var periodeUtenfor = new Periode(endringsperiode.getFraOgMed().minusMonths(1), endringsperiode.getFraOgMed().minusDays(1));

        var ytelse = TestUtils.komplettYtelsePsb(søknadsperiode);
//        ytelse.medUttak(new Uttak().medPerioder(Map.of(
//                periodeUtenfor, new UttakPeriodeInfo(Duration.ofHours(8)),
//                endringsperiode, new UttakPeriodeInfo(Duration.ofHours(8)))));

//        ytelse.medEndringsperiode(endringsperiode);
        ytelse.getTilsynsordning().leggeTilPeriode(periodeUtenfor, new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)));
        ytelse.getBeredskap().leggeTilPeriode(periodeUtenfor, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()));
        ytelse.getNattevåk().leggeTilPeriode(periodeUtenfor, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()));
        ytelse.getArbeidstid().leggeTilArbeidstaker(new Arbeidstaker(null, Organisasjonsnummer.of("199999999"),
                new ArbeidstidInfo(Map.of(
                        endringsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(4)),
                        søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(0)),
                        periodeUtenfor, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(6))
                        ))));

        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isNotEmpty();
        feilInneholderFeilkode(feil, "ugyldigPeriode");
        feilInneholderFeilkode(feil, "ikkeKomplettPeriode");
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
        ytelse.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(arbeidstaker)));
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
        ytelse.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(arbeidstaker)));
        assertThat(valider(ytelse)).size().isEqualTo(2);
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
        Tilsynsordning tilsynsordning = new Tilsynsordning().medPerioder(Map.of(
                new Periode(søknad.getSøknadsperiode().getFraOgMed(), søknad.getSøknadsperiode().getTilOgMed().plusDays(10)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30))));
        søknad.medTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
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
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(null, Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), null)));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
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
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
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
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, null, arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
        var søknadsperiode = søknad.getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        søknad.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadsperiodeInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarn(søknadsperiode);
        var feil = verifyHarFeil(psb);
        feilInneholderFeilkode(feil, "påkrevd");
    }

    @Test
    public void tilsynnInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var psb = TestUtils.minimumSøknadPleiepengerSyktBarn(søknadsperiode);
        psb.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(new Periode(LocalDate.now(), null), new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)))));
        var feil = verifyHarFeil(psb);
        feilInneholderFeilkode(feil, "påkrevd");
    }

    @Test
    public void åpneOgOverlappendePerioderForFrilanserOgSelvstendig() {
        var søknad = TestUtils.komplettYtelsePsbMedDelperioder();
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

    @Test
    public void feilISøknadsperidodeFomTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = TestUtils.komplettYtelsePsb(søknadsperiode);
        psb.medSøknadsperiode(new Periode(LocalDate.now().plusDays(2), LocalDate.now()));

        var feil = verifyHarFeil(psb);
        feilInneholderFeilkode(feil, "ugyldigPeriode");
    }

    @Test
    public void feilIEndringsperiodeFomTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = TestUtils.komplettYtelsePsb(søknadsperiode);
//        psb.medEndringsperiode(new Periode(LocalDate.now().plusDays(2), LocalDate.now()));

        var feil = verifyHarFeil(psb);
        assertThat(feil.size()).isEqualTo(1);
    }

    @Test
    public void feilIUttaksperiodeTomErNull() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = TestUtils.komplettYtelsePsb(søknadsperiode);
        var uttakPeriode = new LukketPeriode(LocalDate.now().plusDays(2), null);
        psb.medUttak(new Uttak().medPerioder(Map.of(uttakPeriode, new UttakPeriodeInfo(Duration.ofHours(8)))));

        var feil = valider(psb);
        feilInneholderFeilkode(feil, "påkrevd");
    }

    @Test
    public void feilITrePerioderISammeObjekt() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var stpEn = LocalDate.now();
        var periodeEn = new Periode(stpEn.plusDays(2), stpEn.minusDays(2));

        var stpTo = LocalDate.now().plusWeeks(1);
        var periodeTo = new Periode(stpTo.plusDays(2), stpTo.minusDays(2));

        var stpTre = LocalDate.now().plusWeeks(2);
        var periodeTre = new Periode(stpTre.plusDays(2), stpTre.minusDays(2));

        var psb = TestUtils.komplettYtelsePsb(søknadsperiode);
        psb.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(new Arbeidstaker(null, Organisasjonsnummer.of("1122"), new ArbeidstidInfo(Map.of(
                periodeEn, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ZERO),
                periodeTo, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ZERO),
                periodeTre, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ZERO)))))));

        var feil = verifyHarFeil(psb);
        feilInneholderFeilkode(feil, "ugyldigPeriode");
        assertThat(feil.size()).isEqualTo(3);
    }


    private void feilInneholderFeilkode(List<Feil> feil, String feilkode) {
        assertThat(feil
                .stream()
                .filter(f -> f.getFeilkode().equals(feilkode))
                .collect(Collectors.toList())
        ).isNotEmpty();
    }

    private List<Feil> verifyHarFeil(PleiepengerSyktBarn ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(PleiepengerSyktBarn ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad.getYtelse());
        assertThat(feil).isEmpty();
    }

    private List<Feil> valider(PleiepengerSyktBarn ytelse) {
        try {
            return validator.valider(ytelse);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
}
