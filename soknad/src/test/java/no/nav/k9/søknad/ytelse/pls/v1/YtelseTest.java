package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUttak;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class YtelseTest {

    @Test
    public void skal_tolerere_flere_aktiviteter_i_samme_periode() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now());
        var arbeidstid = new ArbeidstidInfo()
                .medPerioder(Map.of(søknadsperiode,
                        new ArbeidstidPeriodeInfo()
                                .medFaktiskArbeidTimerPerDag(Duration.ZERO)
                                .medJobberNormaltTimerPerDag(Duration.ofHours(7))));
        var arbeidstidMedFlereAktiviteter = new Arbeidstid()
                .medArbeidstaker(List.of(
                        new Arbeidstaker().medOrganisasjonsnummer(Organisasjonsnummer.of("123456789")).medArbeidstidInfo(arbeidstid),
                        new Arbeidstaker().medOrganisasjonsnummer(Organisasjonsnummer.of("123456788")).medArbeidstidInfo(arbeidstid)));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiode)
                .medUttak(lagUttak(søknadsperiode))
                .medArbeidstid(arbeidstidMedFlereAktiviteter);

        verifyIngenFeil(ytelse);
    }

    @Test
    public void overlappendePerioderForArbeidstid() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiodeEn)
                .medUttak(lagUttak(søknadsperiodeEn))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(søknadsperiodeEn, søknadsperiodeTo))));

        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }

    @Test
    public void invertertPeriodeForArbeidstakerPeriode() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now());
        var søknadsperiodeInvertert = new Periode(LocalDate.now(), LocalDate.now().minusMonths(2));

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiodeInvertert);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiode)
                .medUttak(lagUttak(søknadsperiode))
                .medArbeidstid(arbeidstid);

        var feil = verifyHarFeil(ytelse);
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

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(List.of(periodeEn, periodeTo))
                .medUttak(lagUttak(periodeEn, periodeTo))
                .medArbeidstid(arbeidstid);

        //var feil = verifyHarFeil(SøknadEksempel.søknad(ytelse));
        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }

    @Test
    public void overlappendePerioderForSøknadsperiodelist() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now());
        var søknadsperiodeTo = new Periode(LocalDate.now(), LocalDate.now().plusDays(1));
        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(søknadsperiodeEn)
                .medSøknadsperiode(søknadsperiodeTo);

        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.søknadsperiode.perioder", "IllegalArgumentException");
    }

    @Test
    public void overlappendePerioderForUttaksperiodeMap() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now());
        var periodeTo = new Periode(LocalDate.now(), LocalDate.now().plusDays(1));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(periodeEn)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(periodeEn))))
                .medUttak(lagUttak(periodeEn, periodeTo));

        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.uttak.perioder", "IllegalArgumentException");
    }

    @Test
    public void søknadsperioderUtenUttak() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(1));
        var periodeTo = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().plusWeeks(3));

        var ytelse = YtelseEksempel.lagYtelse()
                .medSøknadsperiode(List.of(periodeEn, periodeTo))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(periodeEn, periodeTo))))
                .medUttak(lagUttak(periodeEn)); // Kun en av periodene her

        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.uttak.perioder", "ikkeKomplettPeriode");
    }
}
