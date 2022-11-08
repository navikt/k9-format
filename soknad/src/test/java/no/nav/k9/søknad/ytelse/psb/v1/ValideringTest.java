package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagBosteder;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagLovbestemtFerie;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUtenlandsopphold;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

class ValideringTest {

    private static final Periode TEST_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private static final Søknad MINIMUM_SØKNAD = SøknadEksempel.minimumSøknad(TEST_PERIODE);

    @Test
    void komplettSøknadHarIngenValideringFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));
        var lovbestemtferie = new Periode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, lovbestemtferie, utelands, bosteder);
        verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    void minimumSøknadHarIngenValideringsFeil() {
        verifyIngenFeil(MINIMUM_SØKNAD);
    }

    @Test
    void mottattDatoKanIkkeVæreIFremtiden() {
        final Periode søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        final Søknad søknad = SøknadEksempel.minimumSøknad(søknadsperiode);
        verifyIngenFeil(søknad);
        søknad.setMottattDato(ZonedDateTime.now().plusSeconds(60));
        verifyHarFeil(søknad);
    }

    @Test
    void oppgittOpptjeningMåVæreSattForNySøknad() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));
        var lovbestemtferie = new Periode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(4));
        var psb = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, lovbestemtferie, utelands, bosteder);
        psb.ignorerOpplysningerOmOpptjening();

        assertThat(psb.skalHaOpplysningOmOpptjeningVedNyPeriode()).isFalse();
        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, new Feil("oppgittOpptjening", "påkrevd", "Opplysninger om opptjening må være oppgitt for ny søknadsperiode."));
    }

    @Test
    void barnKanIkkeVæreSøker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", "søkerSammeSomBarn", "Søker kan ikke være barn."));

    }

    @Test
    void søkerKanIkkeVæreNull() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        assertThrows(NullPointerException.class, () -> søknad.medSøker(null));
    }

    @Test
    void bostederKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    void bostederKanIkkeHaInvertertePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.bosteder.perioder.['" + bostedperiode + "']",
                "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        Søknad.SerDes.serialize(søknad);
    }

    @Test
    void utenlandsoppholdKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medUtenlandsopphold(lagUtenlandsopphold(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    void alleFelterISøknadInvertertPeriode() {
        var søknadsperiode = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().minusWeeks(2));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode)
                .medBosteder(YtelseEksempel.lagBosteder(søknadsperiode))
                .medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(søknadsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, List.of(søknadsperiode));

        feilInneholder(feil, "ytelse.søknadsperiode[0].tilOgMedFørFraOgMed", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.bosteder.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].arbeidstidInfo.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.uttak.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        assertThat(feil).size().isEqualTo(5);
    }

    @Test
    void alleFelterISøknadInvertertPeriodeGirFeil() {

        PleiepengerSyktBarnSøknadValidator søknadValidator = new PleiepengerSyktBarnSøknadValidator();

        var søknadsperiode = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().minusWeeks(2));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        List<Feil> feil = søknadValidator.valider(søknad);
        List<String> feilmeldinger = feil.stream().map(f -> f.getFeilmelding()).toList();
        assertThat(feilmeldinger).containsOnly("Fra og med (FOM) må være før eller lik til og med (TOM).");
    }

    @Test
    void søknadUtenSøknadsperiodeOgGylidgIntervalForEndring() {
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(2));
        var ytese = YtelseEksempel.lagYtelse()
                .medLovbestemtFerie(lagLovbestemtFerie(endringsperiode));
        var søknad = SøknadEksempel.søknad(ytese);

        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "missingArgument");
    }

    @Test
    void søknadLagerRiktigFeilmeldingPathForNullFeil() {
        var søknadsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(2));
        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode);

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(5));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiode);
        ytelse.getArbeidstid().leggeTilArbeidstaker(arbeidstaker);
        var feil = verifyHarFeil(SøknadEksempel.søknad(ytelse));

        var forventetPath =
                "ytelse.arbeidstid.arbeidstakerList[0].arbeidstidInfo.perioder['"
                        + søknadsperiode
                        + "'].jobberNormaltTimerPerDag";
        feilInneholder(feil, forventetPath, "nullFeil");

    }











    @Test
    public void uttaksperiodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var periodeUttak = new Periode(LocalDate.now().plusDays(2), LocalDate.now());
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        psb.medUttak(new Uttak().medPerioder(Map.of(periodeUttak, new Uttak.UttakPeriodeInfo(Duration.ofHours(8)))));

        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test void søknadsperiodeKanIkkeHaFomFørTom() {
        var søknadperiode = new Periode(LocalDate.now().plusDays(10), LocalDate.now().minusDays(10));
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadperiode);

        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void søknadsperiodeInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode);
        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void tilsynnInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode);
        Periode tilsynsordningPeriode = new Periode(LocalDate.now(), null);
        psb.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(tilsynsordningPeriode, new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)))));
        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.tilsynsordning.perioder.['" + tilsynsordningPeriode +"']",  "påkrevd", "Til og med (TOM) må være satt.");
    }

    @Test
    public void overlappendePerioderForSøknadsperiodelist() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiodeEn);
        psb.medSøknadsperiode(søknadsperiodeTo);

        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.søknadsperiode.perioder", "IllegalArgumentException");
    }

    @Test
    public void overlappendePerioderForUttaksperiodeMap() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var peridoeTo = new Periode(periodeEn.getTilOgMed().plusDays(1), periodeEn.getTilOgMed().plusWeeks(2));
        var periodeTre = new Periode(periodeEn.getFraOgMed().minusDays(3), periodeEn.getFraOgMed().plusDays(5));
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(periodeEn);

        psb.medUttak(YtelseEksempel.lagUttak(periodeEn, peridoeTo, periodeTre));

        var søknad = SøknadEksempel.søknad(psb);
        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.uttak.perioder", "IllegalArgumentException");
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now());
        var tilsynsordning = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().plusDays(10));

        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medTilsynsordning(YtelseEksempel.lagTilsynsordning(tilsynsordning));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.tilsynsordning.perioder", "ugyldigPeriode");
    }

    @Test
    public void invertertPeriodeForArbeidstakerPeriode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().minusMonths(2));

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiode);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medArbeidstid(arbeidstid);

        var feil = verifyHarFeil(SøknadEksempel.søknad(ytelse));
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void søknadMedArbeidsSomOverlapper() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(3));
        var periodeTo = new Periode(periodeEn.getFraOgMed().plusDays(7), periodeEn.getTilOgMed().minusDays(7));

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30))
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));

        var arbeidstidInfo = new ArbeidstidInfo().medPerioder(Map.of(
                periodeEn, arbeidstidPeriodeInfo,
                periodeTo, arbeidstidPeriodeInfo));

        var arbeidstaker = new Arbeidstaker()
                .medOrganisasjonsnummer(TestUtils.okOrgnummer())
                .medArbeidstidInfo(arbeidstidInfo);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(periodeEn)
                .medArbeidstid(arbeidstid);

        var feil = verifyHarFeil(SøknadEksempel.søknad(ytelse));
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }


}
