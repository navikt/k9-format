package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholderFeilkode;
import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholderFeltOgFeilkode;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyIngenFeil;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.Beredskap;
import no.nav.k9.søknad.ytelse.psb.v1.Nattevåk;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;

class EndringTest {

    @Test
    public void endringssøknadUtenFeil() {
        var endringsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(1));
        var ytelse = YtelseEksempel.minimumEndringssøknad(endringsperiode);

        verifyIngenFeil(ytelse);
    }

    @Test
    public void endringssøknadMedSøknadsperioderUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var endringssøknad = YtelseEksempel.minimumYtelseMedEndring(søknadsperiode, endringsperiode);
        verifyIngenFeil(endringssøknad);

    }

    @Test
    public void endringssøknadMedIkkeKomplettUttak() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var ytelse = YtelseEksempel.komplettYtelse(søknadsperiode);
        ytelse.medUttak(new Uttak().medPerioder(Map.of(
                endringsperiode, new UttakPeriodeInfo(Duration.ofHours(8)))));

        ytelse.medEndringsperiode(endringsperiode);

        var feil = verifyHarFeil(ytelse);
        feilInneholderFeltOgFeilkode(feil, "uttak.perioder", "ikkeKomplettPeriode");
    }

    @Test
    public void endringssøknadMedPerioderUtenforGyldigperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var periodeUtenfor = new Periode(endringsperiode.getFraOgMed().minusMonths(1), endringsperiode.getFraOgMed().minusDays(1));

        var ytelse = YtelseEksempel.komplettYtelse(søknadsperiode);

        ytelse.medEndringsperiode(endringsperiode);
        ytelse.getUttak().leggeTilPeriode(periodeUtenfor, new UttakPeriodeInfo(Duration.ofHours(8)));
        ytelse.getTilsynsordning().leggeTilPeriode(periodeUtenfor, new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7)));
        ytelse.getBeredskap().leggeTilPeriode(periodeUtenfor, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()));
        ytelse.getNattevåk().leggeTilPeriode(periodeUtenfor, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()));

        var feil = verifyHarFeil(ytelse);
        feilInneholderFeilkode(feil, "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "beredskap.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "nattevåk.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "tilsynsordning.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "uttak.perioder", "ugyldigPeriode");
    }

    @Test
    public void endringssøknadPeriodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);
        psb.medEndringsperiode(new Periode(LocalDate.now().plusDays(2), LocalDate.now()));

        var feil = verifyHarFeil(psb);
        feilInneholderFeilkode(feil, "ugyldigPeriode");    }

}
