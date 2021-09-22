package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyIngenFeil;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

class TrekkKravTest
{

    @Test
    public void søknadMedTrekkKravUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var trekkKravPeriode = new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1));

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(trekkKravPeriode);

        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadMedNattevåkInnenforTrekKKravPeriode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var trekkKravPeriode = new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1));
        Periode periodeMedFeil = new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().plusMonths(2));

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);

        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(trekkKravPeriode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).medNattevåk(YtelseEksempel.lagNattevåk(periodeMedFeil));
        ((PleiepengerSyktBarn)søknad.getYtelse()).medEndringsperiode(periodeMedFeil);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.nattevåk.perioder", "ugyldigPeriodeInterval");
    }

    @Test
    public void søknadOverlapperMedTrekkKravPeriode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var trekkKravPeriodeMedFeil = new Periode(LocalDate.now().plusWeeks(3), søknadsperiode.getTilOgMed());

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(trekkKravPeriodeMedFeil);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.søknadperiode.perioder", "ugyldigPeriodeInterval");
    }

    @Test
    public void søknadMedEndringOgTrekkKravUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().minusDays(1));
        var trekkKravPeriode = new Periode(gyldigEndringsInterval.getFraOgMed(), endringsperiode.getFraOgMed().minusDays(1));

        var psb = YtelseEksempel.standardYtelseMedEndring(søknadsperiode, endringsperiode);
        psb.addTrekkKravPeriode(trekkKravPeriode);

        verifyIngenFeil(psb, List.of(gyldigEndringsInterval));
    }

    @Test
    public void søknadsperiodeOverlapperMedTrekkKrav() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var trekkKravPerioderSomOverlapper = new Periode(LocalDate.now().minusWeeks(3), søknadsperiode.getFraOgMed());

        var søknad = SøknadEksempel.minimumSøknad(søknadsperiode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(trekkKravPerioderSomOverlapper);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.søknadperiode.perioder", "ugyldigPeriodeInterval");
        feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriodeInterval");
    }

}
