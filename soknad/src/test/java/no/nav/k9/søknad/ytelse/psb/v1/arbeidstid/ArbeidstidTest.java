package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class ArbeidstidTest {
    private static final TestValidator validator = new TestValidator();


    @Disabled
    @Test
    public void søknadMedNullJobberNormaltTimerPerDag() {
        var søknadsperiode = new Periode(LocalDate.now().minusDays(20), LocalDate.now());
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = YtelseEksempel.lagArbeidstaker(arbeidstidPeriodeInfo, søknadsperiode);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(arbeidstaker));

        var feil = validator.verifyHarFeil(arbeidstid);
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[1].arbeidstidInfo.perioder[" + søknadsperiode + "].jobberNormaltTimerPerDag", "nullFeil");
    }

    @Disabled
    @Test
    public void søknadMedNullFaktiskArbeidTimerPerDag() {
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var feil = validator.verifyHarFeil(arbeidstidPeriodeInfo);
        feilInneholder(feil, "nullFeil");
    }

    @Test
    public void søknadMedNegativNormaltArbeidTimerPerDag() {
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30))
                .medJobberNormaltTimerPerDag(Duration.ofHours(-8));
        var feil = validator.verifyHarFeil(arbeidstidPeriodeInfo);
        feilInneholder(feil, "ugyldigVerdi");
    }

    @Test
    public void søknadMedNegativFaktiskArbeidTimerPerDag() {
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(-7).plusMinutes(30))
                .medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var feil = validator.verifyHarFeil(arbeidstidPeriodeInfo);
        feilInneholder(feil, "ugyldigVerdi");
    }

    @Test
    public void manglerOrgnummerOgNorskidentitetForArbeidstaker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30))
                .medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstidInfo = new ArbeidstidInfo().medPerioder(Map.of(søknadsperiode, arbeidstidPeriodeInfo));
        var arbeidstaker = new Arbeidstaker().medArbeidstidInfo(arbeidstidInfo);

        var feil = validator.verifyHarFeil(arbeidstaker);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void søknadMedIkkeEntydigInfoForArbeidstaker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30))
                .medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        var arbeidstidInfo = new ArbeidstidInfo().medPerioder(Map.of(søknadsperiode, arbeidstidPeriodeInfo));
        var arbeidstaker = new Arbeidstaker()
                .medOrganisasjonsnummer(TestUtils.okOrgnummer())
                .medNorskIdentitetsnummer(TestUtils.okNorskIdentitetsnummer())
                .medArbeidstidInfo(arbeidstidInfo);

        var feil = validator.verifyHarFeil(arbeidstaker);
        feilInneholder(feil, "ikkeEntydig");
    }

    @Test
    public void søknadMedFaktisArbeidStørreEnnFaktisArbeid() {
        var jobberNormalt = Duration.ofHours(3);
        var jobberFaktisk = Duration.ofHours(7);

        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medFaktiskArbeidTimerPerDag(jobberFaktisk)
                .medJobberNormaltTimerPerDag(jobberNormalt);
        var arbeidstidInfo = new ArbeidstidInfo().medPerioder(Map.of(søknadsperiode, arbeidstidPeriodeInfo));
        var arbeidstaker = new Arbeidstaker()
                .medOrganisasjonsnummer(TestUtils.okOrgnummer())
                .medArbeidstidInfo(arbeidstidInfo);

        validator.verifyIngenFeil(arbeidstaker);
    }
}