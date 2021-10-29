package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.TestUtils.mandagenFør;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.komplettYtelseMedEndring;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagArbeidstaker;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagBeredskap;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagNattevåk;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagTilsynsordning;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUttak;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagYtelse;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.minimumYtelseMedSøknadsperiode;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

class EndringTest {

    @Test
    public void komplettEndringssøknadUtenFeil() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var psb = komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(psb);

        verifyIngenFeil(søknad, List.of(gyldigEndringsInterval));
    }

    @Test
    public void komplettEndringssøknadUtenFeilUtenGyldigPeriode() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var psb = komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(psb);

        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "ytelse.søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder.");
    }

    @Test
    public void endringssøknadMedBareTilsyn() {
        var gyldigIntervalForEndring = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var endringsperiode = new Periode(LocalDate.now().plusDays(2), LocalDate.now().plusWeeks(2));

        var psb = lagYtelse()
                .medTilsynsordning(lagTilsynsordning(endringsperiode));
        verifyIngenFeil(psb, List.of(gyldigIntervalForEndring));
    }

    @Test
    public void endringssøknadMedSøknadsperioderUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var ytelse = minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(lagArbeidstaker(søknadsperiode, endringsperiode))))
                .medTilsynsordning(lagTilsynsordning(søknadsperiode, endringsperiode))
                .medBeredskap(lagBeredskap(søknadsperiode, endringsperiode))
                .medNattevåk(lagNattevåk(søknadsperiode, endringsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        verifyIngenFeil(søknad, List.of(endringsperiode));
    }

    @Test
    public void endringssøknadMedBareEndringAvTilsyn() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var ytelse = lagYtelse()
                .medTilsynsordning(lagTilsynsordning(endringsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        verifyIngenFeil(søknad, List.of(gyldigEndringsInterval));
    }

    @Test
    public void søknadMedEndringAvUttakUtenGyldigIntervalForEndring() {
        var endringsperiode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now().minusDays(1));

        var ytelse = lagYtelse()
                .medUttak(lagUttak(endringsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, List.of());
        TestUtils.feilInneholder(feil, "ytelse.søknadsperiode", "missingArgument");
        assertThat(feil).size().isEqualTo(1);
    }

    @Test
    public void endringssøknadMedPerioderUtenforGyldigperiode() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));
        var periodeUtenforGyldigInterval =new Periode(LocalDate.now().minusMonths(2).minusMonths(1), LocalDate.now().minusDays(1).minusDays(1));

        var ytelse = komplettYtelseMedEndring(periodeUtenforGyldigInterval);
        var søknad = SøknadEksempel.søknad(ytelse);


        var feil = verifyHarFeil(søknad, List.of(gyldigEndringsInterval));
        feilInneholder(feil, "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.beredskap.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.nattevåk.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.tilsynsordning.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "ugyldigPeriode");
    }

    @Test
    public void kalkulertEndringsperiodeFinnerFlereSøknadsperioder() {
        var søknadsperiodeEN = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusWeeks(4), LocalDate.now().plusWeeks(6));
        var søknadsperiodeTre = new Periode(LocalDate.now().plusWeeks(7), LocalDate.now().plusWeeks(10));
        var søknadsperiodeFire = new Periode(LocalDate.now().plusWeeks(12), LocalDate.now().plusWeeks(13));

        var gyldigIntervalForEndring = List.of(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);

        var ytelse = komplettYtelseMedEndring(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);
        ytelse.medEndringsperiode(List.of(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire));

        verifyIngenFeil(ytelse, gyldigIntervalForEndring);

        var endringsperiode = ytelse.getEndringsperiode();
        assertThat(endringsperiode)
                .isNotEmpty()
                .size().isEqualTo(4);
        assertThat(endringsperiode).contains(søknadsperiodeEN);
        assertThat(endringsperiode).contains(søknadsperiodeTo);
        assertThat(endringsperiode).contains(søknadsperiodeTre);
        assertThat(endringsperiode).contains(søknadsperiodeFire);
        assertEndringsperioderIJson(ytelse);
    }

    @Test
    public void endringsperioderKanInneholdeHelgSomIkkeErMedIGyldigIntervalForEndring() {
        var stp = mandagenFør(LocalDate.now());
        var stpTo = mandagenFør(LocalDate.now().plusWeeks(1));
        var stpTre = mandagenFør(LocalDate.now().plusWeeks(2));

        var gyldigIntervalForEndring = List.of(
                new Periode(stp, stp.plusDays(4)),
                new Periode(stpTo, stpTo.plusDays(4)),
                new Periode(stpTre, stpTre.plusDays(4))
        );
        var endringsperiode = new Periode(stp, stpTre.plusDays(6));

        var psb = komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(psb);

        verifyIngenFeil(søknad, gyldigIntervalForEndring);
    }
    @Test
    public void endringsperioderIkkeInneholdeDagerSomErUtenforGyldigIntervalOgIkkeErHelg() {
        var stp = mandagenFør(LocalDate.now());
        var stpTo = mandagenFør(LocalDate.now().plusWeeks(1));
        var stpTre = mandagenFør(LocalDate.now().plusWeeks(2));

        var gyldigIntervalForEndring = List.of(
                new Periode(stp, stp.plusDays(4)),
                new Periode(stpTo, stpTo.plusDays(4)),
                new Periode(stpTre, stpTre.plusDays(4))
        );
        var endringsperiode = new Periode(stp, stpTre.plusDays(7));

        var psb = komplettYtelseMedEndring(endringsperiode);
        var søknad = SøknadEksempel.søknad(psb);

        var feil = verifyHarFeil(søknad, gyldigIntervalForEndring);
        feilInneholder(feil, "ytelse.beredskap.perioder", "ugyldigPeriode");
        feilInneholder(feil, "ytelse.nattevåk.perioder", "ugyldigPeriode");
        feilInneholder(feil, "ytelse.tilsynsordning.perioder", "ugyldigPeriode");
        feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder", "ugyldigPeriode");
    }
    
    @Test
    public void kunDokumentklassifiseringSkalFungere() {
        var gyldigIntervalForEndring = List.of(new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2)));

        var ytelse = new PleiepengerSyktBarn().medBarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("22211111111")));
        var søknad = new Søknad(SøknadId.of("lala"), Versjon.of("1.0.0"), ZonedDateTime.now(), new Søker(NorskIdentitetsnummer.of("22222222222")), ytelse);

        verifyIngenFeil(søknad, gyldigIntervalForEndring);
    }

    private void assertEndringsperioderIJson(PleiepengerSyktBarn ytelse) {
        var endringsperioder = new ArrayList<Periode>();
        try {
            var jsonArray = (ArrayNode) JsonUtils.getObjectMapper()
                    .readTree(JsonUtils.toString(ytelse))
                    .get("endringsperiode");

            jsonArray.forEach(jsonNode -> endringsperioder.add(new Periode(jsonNode.asText())));
        } catch (Exception ex) {
            throw new IllegalStateException("Feil ved sjekk på endringsperioder i JSON", ex);
        }
        assertThat(endringsperioder).containsExactlyElementsOf(ytelse.getEndringsperiode());
    }
}
