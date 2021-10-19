package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.TestUtils;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class YtelseTest {

    /*
    Verdier er satt riktig Tester
     */

    @Test
    public void omsorgKanInneholdeNullPåRelasjonOgBeskrivelse() {
        var psb = YtelseEksempel.minimumYtelseMedDelperioder();
        psb.medOmsorg(new Omsorg().medBeskrivelseAvOmsorgsrollen(null).medRelasjonTilBarnet(null));
        verifyIngenFeil(psb);
    }

    @Disabled("Trenger avklaring om dette er ønsket")
    @Test
    public void uttakKanIkkeVæreTom() {
        var ytelse = YtelseEksempel.komplettYtelseMedDelperioder();
        ytelse.medUttak(new Uttak());
        verifyHarFeil(ytelse);
    }

    /*
    Periode Tester
     */

    @Test void søknadsperiodeKanIkkeHaFomFørTom() {
        var søknadperiode = new Periode(LocalDate.now().plusDays(10), LocalDate.now().minusDays(10));
        var psb = YtelseEksempel.minimumYtelse(søknadperiode);

        final List<Feil> feil = verifyHarFeil(psb);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void uttaksperiodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var periodeUttak = new Periode(LocalDate.now().plusDays(2), LocalDate.now());
        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);
        psb.medUttak(new Uttak().medPerioder(Map.of(periodeUttak, new UttakPeriodeInfo(Duration.ofHours(8)))));

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void søknadsperiodeInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var psb = YtelseEksempel.minimumYtelse(søknadsperiode);
        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void tilsynnInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var psb = YtelseEksempel.minimumYtelse(søknadsperiode);
        psb.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(new Periode(LocalDate.now(), null), new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)))));
        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void overlappendePerioderForSøknadsperiodelist() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var psb = YtelseEksempel.komplettYtelse(søknadsperiodeEn);
        psb.medSøknadsperiode(søknadsperiodeTo);

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ytelse.søknadsperiode.perioder", "IllegalArgumentException");
    }

    @Test
    public void overlappendePerioderForUttaksperiodeMap() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var peridoeTo = new Periode(periodeEn.getTilOgMed().plusDays(1), periodeEn.getTilOgMed().plusWeeks(2));
        var periodeTre = new Periode(periodeEn.getFraOgMed().minusDays(3), periodeEn.getFraOgMed().plusDays(5));
        var psb = YtelseEksempel.komplettYtelse(periodeEn);

        psb.medUttak(YtelseEksempel.lagUttak(periodeEn, peridoeTo, periodeTre));

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ytelse.uttak.perioder", "IllegalArgumentException");
    }


    /*
    Komplette Perioder Test
     */

    //TODO legge på uttaktest
    //TODO legge på arbeidsid innenfor gyildig periode test

    /*
    Perioder utenfor gyldigperiode
     */

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknad = YtelseEksempel.komplettYtelseMedDelperioder();
        Tilsynsordning tilsynsordning = new Tilsynsordning().medPerioder(Map.of(
                new Periode(søknad.getSøknadsperiode().getFraOgMed(), søknad.getSøknadsperiode().getTilOgMed().plusDays(10)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30))));
        søknad.medTilsynsordning(tilsynsordning);

        verifyHarFeil(søknad);
    }

    /*
    OpptjeningAktivitet Test
     */

    @Test
    public void frilanserKanHaÅpenPeriode() {
        var søknad = YtelseEksempel.komplettYtelseMedDelperioder();

        var arbeidAktivitet = new OpptjeningAktivitet()
                .medFrilanser(new Frilanser()
                        .medStartDato(LocalDate.parse("2020-01-01")));
        søknad.medOpptjeningAktivitet(arbeidAktivitet);
        verifyIngenFeil(søknad);
    }

    @Test
    public void selvstendigNæringsdrivendeKanHaÅpnePerioder() {
        var søknad = YtelseEksempel.komplettYtelseMedDelperioder();
        var selvstendig = SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                .virksomhetstyper(List.of(VirksomhetType.ANNEN)).build();

        var arbeidAktivitet = new OpptjeningAktivitet()
                .medSelvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                        .periode(Periode.parse("2020-01-01/.."), selvstendig)
                        .virksomhetNavn("testUlf")
                        .build());
        søknad.medOpptjeningAktivitet(arbeidAktivitet);
        verifyIngenFeil(søknad);
    }

    @Test
    public void selvstendigNæringsdrivendeKanHaOverlappendePerioder() {
        var søknad = YtelseEksempel.komplettYtelseMedDelperioder();
        var selvstendig = SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                .virksomhetstyper(List.of(VirksomhetType.ANNEN)).build();

        var arbeidAktivitet = new OpptjeningAktivitet()
                .medSelvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                        .periode(Periode.parse("2020-01-01/2020-02-02"), selvstendig)
                        .periode(Periode.parse("2020-01-05/2020-02-01"), selvstendig)
                        .virksomhetNavn("testUlf")
                        .build());
        søknad.medOpptjeningAktivitet(arbeidAktivitet);
        verifyIngenFeil(søknad);
    }

    /*
    Arbeidstid Tester
     */

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var søknad = YtelseEksempel.komplettYtelseMedDelperioder();
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
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder());
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();

        var arbeidstaker = YtelseEksempel.lagArbeidstaker(new ArbeidstidPeriodeInfo(null, Duration.ofHours(7).plusMinutes(30)), søknadsperiode);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[1].arbeidstidInfo.perioder[" + søknadsperiode + "].jobberNormaltTimerPerDag", "påkrevd");
    }

    @Test
    public void invertertPeriodeForArbeidstakerPeriode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelse(søknadsperiode));
        var arbeidstidperiode = new Periode(LocalDate.now().plusDays(2), LocalDate.now().minusDays(2));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidperiode);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(arbeidstaker)));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList" + TestUtils.periodeString(0) + ".perioder" + TestUtils.periodeString(arbeidstidperiode) , "ugyldigPeriode");
    }

    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var søknad = (SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder()));
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), null)));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder());
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(-8), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNegativFaktiskArbeidTimerPerDag() {
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder());
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(-7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedNullFeilArbeidstaker() {
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder());
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of(søknadsperiode, new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(null, null, arbeidstidInfo);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        verifyHarFeil(søknad);
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelseMedDelperioder());
        var søknadsperiode = søknad.getYtelse().getSøknadsperiode();
        var arbeidstidInfo = new ArbeidstidInfo(
                Map.of( søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30))));
        var arbeidstaker = new Arbeidstaker(NorskIdentitetsnummer.of("29099012345"), Organisasjonsnummer.of("88888888"), arbeidstidInfo);
        ((PleiepengerSyktBarn) søknad.getYtelse()).getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ikkeEntydigId");
    }

    @Test
    public void søknadMedFaktisArbeidStørreEnnFaktisArbeid() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var søknad = SøknadEksempel.søknad(YtelseEksempel.komplettYtelse(søknadsperiode));
        var jobberNormalt = Duration.ofHours(3);
        var jobberFaktisk = Duration.ofHours(7);
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(new ArbeidstidPeriodeInfo(jobberNormalt, jobberFaktisk), søknadsperiode);

        ((PleiepengerSyktBarn) søknad.getYtelse()).medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(arbeidstaker)));
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ugyldigArbeidstid");
    }

}
