package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUttak;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.pls.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

class ValideringTest {

    private static final Periode TEST_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private static final Søknad MINIMUM_SØKNAD = SøknadEksempel.søknadMedArbeidstid(TEST_PERIODE);

    @Test
    void komplettSøknadHarIngenValideringFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.ytelseMedUtenlandstilsnitt(søknadsperiode, utelands, bosteder);
        verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    void minimumSøknadHarIngenValideringsFeil() {
        verifyIngenFeil(MINIMUM_SØKNAD);
    }

    @Test
    void barnKanIkkeVæreSøker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", "søkerSammeSomPleietrengende", "Søker kan ikke være samme person som er i livets sluttfase."));
    }

    @Test
    void søkerKanIkkeVæreNull() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        assertThrows(NullPointerException.class, () -> søknad.medSøker(null));
    }

    @Test
    void bostederKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medBosteder(YtelseEksempel.lagBosteder(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    void bostederKanIkkeHaInvertertePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));
        var utelandsperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medBosteder(YtelseEksempel.lagBosteder(bostedperiode)).medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING, utelandsperiode));

        var feil = verifyHarFeil(søknad);
        assertThat(feil).hasSize(2);
        feilInneholder(feil, "ytelse.bosteder.perioder.['" + bostedperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder.['" + bostedperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        Søknad.SerDes.serialize(søknad);
    }

    @Test
    void utenlandsoppholdKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    void alleFelterISøknadInvertertPeriode() {
        LocalDate startdato = LocalDate.of(2021, 12, 14);
        var søknadsperiode = new Periode(startdato.plusWeeks(2), startdato.minusWeeks(2));
        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode)
                .medBosteder(YtelseEksempel.lagBosteder(søknadsperiode))
                .medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(søknadsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad);

        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].arbeidstidInfo.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.bosteder.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.uttak.perioder.['" + søknadsperiode + "']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.søknadsperiode[0].tilOgMedFørFraOgMed", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        assertThat(feil).size().isEqualTo(5);
    }

    @Test
    void lovbestemtFerieKanIkkeVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var lovbestemtFerie = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medLovbestemtFerie(YtelseEksempel.lagLovbestemtFerie(lovbestemtFerie));

        verifyHarFeil(søknad);
    }

    @Test
    void lovbestemtFerieISøknadsperiodeFeilerIkke() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var lovbestemtFerie = new Periode(LocalDate.now().plusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medLovbestemtFerie(YtelseEksempel.lagLovbestemtFerie(lovbestemtFerie));

        verifyIngenFeil(søknad);
    }

    @Test
    void skal_tolerere_flere_aktiviteter_i_samme_periode() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now());
        var arbeidstid = new ArbeidstidInfo()
                .medPerioder(Map.of(søknadsperiode,
                        new ArbeidstidPeriodeInfo()
                                .medFaktiskArbeidTimerPerDag(Duration.ZERO)
                                .medJobberNormaltTimerPerDag(Duration.ofHours(7))));
        var arbeidstidMedFlereAktiviteter = new Arbeidstid()
                .medArbeidstaker(List.of(
                        new Arbeidstaker().medOrganisasjonsnummer(Organisasjonsnummer.of("111111111")).medArbeidstidInfo(arbeidstid),
                        new Arbeidstaker().medOrganisasjonsnummer(Organisasjonsnummer.of("222222222")).medArbeidstidInfo(arbeidstid)));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiode)
                .medUttak(lagUttak(søknadsperiode))
                .medArbeidstid(arbeidstidMedFlereAktiviteter);
        var søknad = SøknadEksempel.søknad(ytelse);
        verifyIngenFeil(søknad);
    }

    @Test
    void overlappendePerioderForArbeidstid() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiodeEn)
                .medUttak(lagUttak(søknadsperiodeEn))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(søknadsperiodeEn, søknadsperiodeTo))));

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }

    @Test
    void invertertPeriodeForArbeidstakerPeriode() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now());
        var søknadsperiodeInvertert = new Periode(LocalDate.now(), LocalDate.now().minusMonths(2));

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiodeInvertert);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiode)
                .medUttak(lagUttak(søknadsperiode))
                .medArbeidstid(arbeidstid);

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    void søknadMedArbeidsSomOverlapper() {
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

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(List.of(periodeEn, periodeTo))
                .medUttak(lagUttak(periodeEn, periodeTo))
                .medArbeidstid(arbeidstid);

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }

    @Test
    void overlappendePerioderForSøknadsperiodelist() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now());
        var søknadsperiodeTo = new Periode(LocalDate.now(), LocalDate.now().plusDays(1));
        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiodeEn)
                .medSøknadsperiode(søknadsperiodeTo);

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.søknadsperiode.perioder", "IllegalArgumentException");
    }

    @Test
    void overlappendePerioderForUttaksperiodeMap() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now());
        var periodeTo = new Periode(LocalDate.now(), LocalDate.now().plusDays(1));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(periodeEn)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(periodeEn))))
                .medUttak(lagUttak(periodeEn, periodeTo));

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.uttak.perioder", "IllegalArgumentException");
    }

    @Test
    void søknadsperioderUtenUttak() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(1));
        var periodeTo = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().plusWeeks(3));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(List.of(periodeEn, periodeTo))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(periodeEn, periodeTo))))
                .medUttak(lagUttak(periodeEn)); // Kun en av periodene her

        var søknad = SøknadEksempel.søknad(ytelse);
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.uttak.perioder", "ikkeKomplettPeriode");
    }

}
