package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.TestUtils.mandagenFør;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.YtelseEksempel.lagArbeidstaker;
import static no.nav.k9.søknad.ytelse.pls.v1.YtelseEksempel.lagUttak;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.pls.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

class ValiderEndringTest {

    @Test
    void komplettEndringssøknadUtenFeil() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var ytelseMedEndring = YtelseEksempel.komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(ytelseMedEndring);

        verifyIngenFeil(søknad, List.of(gyldigEndringsInterval));
    }

    @Test
    void komplettEndringssøknadUtenFeilUtenGyldigPeriode() {
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var ytelseMedEndring = YtelseEksempel.komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(ytelseMedEndring);

        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder.");
    }


    @Test
    void endringssøknadMedSøknadsperioderUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var ytelse = YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(lagArbeidstaker(søknadsperiode, endringsperiode))));
        var søknad = SøknadEksempel.søknad(ytelse);

        verifyIngenFeil(søknad, List.of(endringsperiode));
    }


    @Test
    void søknadMedEndringAvUttakUtenGyldigIntervalForEndring() {
        var endringsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().minusDays(1));

        var ytelse = YtelseEksempel.lagYtelse()
                .medUttak(lagUttak(endringsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, List.of());
        TestUtils.feilInneholder(feil, "ytelse.søknadsperiode", "missingArgument");
        assertThat(feil).size().isEqualTo(1);
    }

    @Test
    void endringssøknadMedPerioderUtenforGyldigperiode() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var periodeUtenforGyldigInterval = new Periode(LocalDate.now().minusMonths(2).minusMonths(1), LocalDate.now().minusDays(1).minusDays(1));

        var ytelse = YtelseEksempel.komplettYtelseMedEndring(periodeUtenforGyldigInterval);
        var søknad = SøknadEksempel.søknad(ytelse);


        var feil = verifyHarFeil(søknad, List.of(gyldigEndringsInterval));
        feilInneholder(feil, "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "ugyldigPeriode");
    }

    @Test
    void kalkulertEndringsperiodeFinnerFlereSøknadsperioder() {
        var søknadsperiodeEN = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusWeeks(4), LocalDate.now().plusWeeks(6));
        var søknadsperiodeTre = new Periode(LocalDate.now().plusWeeks(7), LocalDate.now().plusWeeks(10));
        var søknadsperiodeFire = new Periode(LocalDate.now().plusWeeks(12), LocalDate.now().plusWeeks(13));

        var gyldigIntervalForEndring = List.of(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);

        var ytelse = YtelseEksempel.komplettYtelseMedEndring(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);
        var søknad = SøknadEksempel.søknad(ytelse);
        verifyIngenFeil(søknad, gyldigIntervalForEndring);

        var endringsperiode = ytelse.getEndringsperiode();
        assertThat(endringsperiode)
                .isNotEmpty()
                .size().isEqualTo(4);
        assertThat(endringsperiode).contains(søknadsperiodeEN);
        assertThat(endringsperiode).contains(søknadsperiodeTo);
        assertThat(endringsperiode).contains(søknadsperiodeTre);
        assertThat(endringsperiode).contains(søknadsperiodeFire);
    }

    @Test
    void endringsperioderKanInneholdeHelgSomIkkeErMedIGyldigIntervalForEndring() {
        var stp = mandagenFør(LocalDate.now());
        var stpTo = mandagenFør(LocalDate.now().plusWeeks(1));
        var stpTre = mandagenFør(LocalDate.now().plusWeeks(2));

        var gyldigIntervalForEndring = List.of(
                new Periode(stp, stp.plusDays(4)),
                new Periode(stpTo, stpTo.plusDays(4)),
                new Periode(stpTre, stpTre.plusDays(4))
        );
        var endringsperiode = new Periode(stp, stpTre.plusDays(6));

        var ytelse = YtelseEksempel.komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        verifyIngenFeil(søknad, gyldigIntervalForEndring);
    }

    @Test
    void endringsperioderIkkeInneholdeDagerSomErUtenforGyldigIntervalOgIkkeErHelg() {
        var stp = mandagenFør(LocalDate.now());
        var stpTo = mandagenFør(LocalDate.now().plusWeeks(1));
        var stpTre = mandagenFør(LocalDate.now().plusWeeks(2));

        var gyldigIntervalForEndring = List.of(
                new Periode(stp, stp.plusDays(4)),
                new Periode(stpTo, stpTo.plusDays(4)),
                new Periode(stpTre, stpTre.plusDays(4))
        );
        var endringsperiode = new Periode(stp, stpTre.plusDays(7));

        var ytelse = YtelseEksempel.komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, gyldigIntervalForEndring);
        feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "ugyldigPeriode");
    }

    @Test
    void kunDokumentklassifiseringSkalFungere() {
        var gyldigIntervalForEndring = List.of(new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2)));

        var ytelse = new PleipengerLivetsSluttfase().medPleietrengende(new Pleietrengende().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("22211111111")));
        var søknad = new Søknad(SøknadId.of("lala"), Versjon.of("1.0.0"), ZonedDateTime.now(), new Søker(NorskIdentitetsnummer.of("22222222222")), ytelse);

        verifyIngenFeil(søknad, gyldigIntervalForEndring);
    }

}
