package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholderFeilkode;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyIngenFeil;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

class TrekkKravTest
{
    private final Periode SØKNADSPERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private final Periode TREKK_KRAV_PERIODE = new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1));

    @Test
    public void søknadMedTrekkKravUtenFeil() {
        var søknad = SøknadEksempel.komplettSøknad(SØKNADSPERIODE);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(
                TREKK_KRAV_PERIODE);

        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadMedTrekkKravMedFeil() {
        var søknad = SøknadEksempel.komplettSøknad(SØKNADSPERIODE);
        Periode periodeMedFeil = new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().plusMonths(2));

        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(TREKK_KRAV_PERIODE);
        ((PleiepengerSyktBarn)søknad.getYtelse()).medNattevåk(YtelseEksempel.lagNattevåk(List.of(periodeMedFeil)));

        var feil = verifyHarFeil(søknad);
        feilInneholderFeilkode(feil, "ugyldigTrekkKrav");
    }

    @Test
    public void søknadOverlapperMedTrekkKravPeriode() {
        var søknad = SøknadEksempel.komplettSøknad(SØKNADSPERIODE);
        var trekkKravPeriodeMedFeil = new Periode(LocalDate.now().plusWeeks(3), SØKNADSPERIODE.getTilOgMed());
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(trekkKravPeriodeMedFeil);

        var feil = verifyHarFeil(søknad);
        feilInneholderFeilkode(feil, "ugyldigTrekkKrav");
    }

    @Test
    public void søknadMedEndringOgTrekkKravUtenFeil() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().minusDays(1));
        var trekkKravPeriode = new Periode(gyldigEndringsInterval.getFraOgMed(), endringsperiode.getFraOgMed().minusDays(1));

        var psb = YtelseEksempel.standardYtelseMedEndring(SØKNADSPERIODE, endringsperiode);
        psb.addTrekkKravPeriode(trekkKravPeriode);

        verifyIngenFeil(psb, List.of(gyldigEndringsInterval));
    }

}
