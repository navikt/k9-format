package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholderFeilkode;
import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholderFeltOgFeilkode;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.EndringsperiodeKalkulator;

class EndringTest {

    @Test
    public void komplettEndringssøknadUtenFeil() {
        var gyldigEndringsInterval = new Periode(LocalDate.now().minusMonths(2), LocalDate.now().plusWeeks(3));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(3));

        var psb = YtelseEksempel.komplettEndringssøknad(endringsperiode);
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
        var endringsperiodeList = List.of(new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusDays(1)));

        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);
        psb.getUttak().leggeTilPeriode(YtelseEksempel.lagUttak(endringsperiodeList).getPerioder());

        var feil = verifyHarFeil(psb, List.of());
        feilInneholderFeltOgFeilkode(feil, "uttak.perioder", "ugyldigPeriode");
        assertThat(feil).size().isEqualTo(1);
        assertThat(endringsperiodeList).isEqualTo(psb.getEndringsperiode());
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
        feilInneholderFeilkode(feil, "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "beredskap.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "nattevåk.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "tilsynsordning.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "uttak.perioder", "ugyldigPeriode");
        feilInneholderFeltOgFeilkode(feil, "arbeidstid.arbeidstaker[1].perioder", "ugyldigPeriode");
    }

    @Test
    public void endringssøknadPeriodeKanIkkeHaFomFørTom() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var psb = YtelseEksempel.komplettYtelse(søknadsperiode);

        var feil = verifyHarFeil(psb, List.of(new Periode(LocalDate.now().plusDays(2), LocalDate.now())));
        feilInneholderFeilkode(feil, "ugyldigPeriode");
    }

    @Test
    public void kalkulertEndringsperiodeFinnerFlereSøknadsperioder() {
        var søknadsperiodeEN = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var søknadsperiodeTo = new Periode(LocalDate.now().plusWeeks(4), LocalDate.now().plusWeeks(6));
        var søknadsperiodeTre = new Periode(LocalDate.now().plusWeeks(7), LocalDate.now().plusWeeks(10));
        var søknadsperiodeFire = new Periode(LocalDate.now().plusWeeks(12), LocalDate.now().plusWeeks(13));

        var gyldigIntervalForEndring = List.of(søknadsperiodeEN, søknadsperiodeTo, søknadsperiodeTre, søknadsperiodeFire);

        var ytelse = YtelseEksempel.komplettEndringssøknad(gyldigIntervalForEndring);

        verifyIngenFeil(ytelse, gyldigIntervalForEndring);

        var endringsperiode = EndringsperiodeKalkulator.getEndringsperiode(ytelse);
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
