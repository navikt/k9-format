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

    @Test
    public void søknadMedTrekkKravUtenFeil() {
        var søknad = SøknadEksempel.komplettSøknad(SØKNADSPERIODE);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(
                new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1)));

        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadMedTrekkKravMedFeil() {
        var søknad = SøknadEksempel.komplettSøknad(SØKNADSPERIODE);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(
                new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(1)));
        ((PleiepengerSyktBarn)søknad.getYtelse()).medNattevåk(YtelseEksempel.lagNattevåk(List.of(
                new Periode(LocalDate.now().minusWeeks(1), LocalDate.now().plusMonths(2)))));

        var feil = verifyHarFeil(søknad);
        feilInneholderFeilkode(feil, "ugyldigTrekkKrav");
    }

}
