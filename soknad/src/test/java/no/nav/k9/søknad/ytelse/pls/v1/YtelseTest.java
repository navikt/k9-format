package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;

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
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class YtelseTest {

    @Test
    public void søknadsperiodeInneholderÅpnePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var psb = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void overlappendePerioderForArbeidstid() {
        var søknadsperiodeEn = new Periode(LocalDate.now(), LocalDate.now().plusDays(20));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        var psb = YtelseEksempel.ytelseForArbeidstaker(søknadsperiodeEn, søknadsperiodeTo);

        var feil = verifyHarFeil(psb);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }
    @Test
    public void invertertPeriodeForArbeidstakerPeriode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().minusMonths(2));

        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiode);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var ytelse = YtelseEksempel.lagYtelse()
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
                .medArbeidstid(arbeidstid);

        //var feil = verifyHarFeil(SøknadEksempel.søknad(ytelse));
        var feil = verifyHarFeil(ytelse);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "IllegalArgumentException");
    }

}
