package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.TestUtils;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class EndringTest {

    @Test
    public void komplettEndringssøknadUtenFeil() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var psb = YtelseEksempel.komplettEndringssøknad(endringsperiode);

        //TODO ta bort nå endringsperiode funker
        psb.medEndringsperiode(endringsperiode);
        verifyIngenFeil(psb, List.of(gyldigEndringsInterval));
        assertEndringsperioderIJson(psb);
    }

    @Test
    public void endringssøknadMedSøknadsperioderUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var psb = YtelseEksempel.standardYtelseMedEndring(søknadsperiode, endringsperiode);
        verifyIngenFeil(psb, List.of(endringsperiode));
        assertEndringsperioderIJson(psb);
    }

    @Test
    public void endringssøknadMedMinimumSøknadsperioderUtenFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var psb = YtelseEksempel.standardYtelseMedEndring(søknadsperiode, endringsperiode);
        verifyIngenFeil(psb, List.of(endringsperiode));
        assertEndringsperioderIJson(psb);
    }

    @Test
    public void søknadMedEndringAvUttakUtenGyldigIntervalForEndring() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1));

        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);
        psb.getUttak().leggeTilPeriode(YtelseEksempel.lagUttak(endringsperiode).getPerioder());

        var feil = verifyHarFeil(psb, List.of());
        TestUtils.feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        assertThat(feil).size().isEqualTo(1);

        //TODO Ta med når endringsperioder utregnes
//        assertThat(endringsperiode).isEqualTo(psb.getEndringsperiode());
        assertEndringsperioderIJson(psb);
    }

    @Test
    public void endringssøknadMedPerioderUtenforGyldigperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var endringsperiode = List.of(new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1)));
        var periodeUtenfor = List.of(new Periode(LocalDate.now().minusMonths(2).minusMonths(1), LocalDate.now().minusDays(1).minusDays(1)));

        var ytelse = YtelseEksempel.komplettYtelse(søknadsperiode);

        YtelseEksempel.leggPåKomplettEndringsøknad(periodeUtenfor.get(0), ytelse);

        var feil = verifyHarFeil(ytelse, endringsperiode);
        feilInneholder(feil, "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.beredskap.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.nattevåk.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.tilsynsordning.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.uttak.perioder", "ugyldigPeriode");
        TestUtils.feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[1].perioder", "ugyldigPeriode");
    }

    @Test
    public void endringssøknadPeriodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);

        var feil = verifyHarFeil(psb, List.of(new Periode(LocalDate.now().plusDays(2), LocalDate.now())));
        feilInneholder(feil, "ugyldigPeriode");    }

    @Test
    public void kalkulertEndringsperiodeFinnerFlereSøknadsperioder() {
        var søknadsperiodeEN = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusWeeks(4), LocalDate.now().plusWeeks(6));
        var søknadsperiodeTre = new Periode(LocalDate.now().plusWeeks(7), LocalDate.now().plusWeeks(10));
        var søknadsperiodeFire = new Periode(LocalDate.now().plusWeeks(12), LocalDate.now().plusWeeks(13));

        var gyldigIntervalForEndring = List.of(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);

        var ytelse = YtelseEksempel.komplettEndringssøknad(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);

        verifyIngenFeil(ytelse, gyldigIntervalForEndring);

        var endringsperiode = ytelse.getUtledetEndringsperiode();
        assertThat(endringsperiode)
                .isNotEmpty()
                .size().isEqualTo(4);
        assertThat(endringsperiode).contains(søknadsperiodeEN);
        assertThat(endringsperiode).contains(søknadsperiodeTo);
        assertThat(endringsperiode).contains(søknadsperiodeTre);
        assertThat(endringsperiode).contains(søknadsperiodeFire);
        assertEndringsperioderIJson(ytelse);
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
