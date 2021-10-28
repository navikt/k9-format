package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.Tilsynsordning.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class YtelseTest {


    @Disabled("Trenger avklaring om dette er ønsket")
    @Test
    public void uttakKanIkkeVæreTom() {
        var søknadsperiode = new Periode(LocalDate.now().minusDays(2), LocalDate.now());
        var ytelse = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        ytelse.medUttak(new Uttak());
        verifyHarFeil(ytelse);
    }

    @Test
    public void uttaksperiodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var periodeUttak = new Periode(LocalDate.now().plusDays(2), LocalDate.now());
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        psb.medUttak(new Uttak().medPerioder(Map.of(periodeUttak, new Uttak.UttakPeriodeInfo(Duration.ofHours(8)))));

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test void søknadsperiodeKanIkkeHaFomFørTom() {
        var søknadperiode = new Periode(LocalDate.now().plusDays(10), LocalDate.now().minusDays(10));
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadperiode);

        final List<Feil> feil = verifyHarFeil(psb);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void søknadsperiodeInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode);
        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void tilsynnInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var psb = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode);
        psb.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(new Periode(LocalDate.now(), null), new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)))));
        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void overlappendePerioderForSøknadsperiodelist() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiodeEn);
        psb.medSøknadsperiode(søknadsperiodeTo);

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ytelse.søknadsperiode.perioder", "IllegalArgumentException");
    }

    @Test
    public void overlappendePerioderForUttaksperiodeMap() {
        var periodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var peridoeTo = new Periode(periodeEn.getTilOgMed().plusDays(1), periodeEn.getTilOgMed().plusWeeks(2));
        var periodeTre = new Periode(periodeEn.getFraOgMed().minusDays(3), periodeEn.getFraOgMed().plusDays(5));
        var psb = YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(periodeEn);

        psb.medUttak(YtelseEksempel.lagUttak(periodeEn, peridoeTo, periodeTre));

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ytelse.uttak.perioder", "IllegalArgumentException");
    }

    @Test
    public void søknadMedTilsynsordningOppholdLengreEnnPerioden() {
        var søknadsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now());
        var tilsynsordning = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().plusDays(10));

        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medTilsynsordning(YtelseEksempel.lagTilsynsordning(tilsynsordning));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad);
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
