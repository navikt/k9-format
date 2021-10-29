package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;

class TilsynsordningTest {

    private static final TestValidator validator = new TestValidator();


    @Test
    public void etablertTilsynTimerPerDagOverMaksVerdi() {
        Periode periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        Duration etablertTilsyn = Duration.ofHours(7).plusMinutes(31);
        var to = new Tilsynsordning().medPerioder(Map.of(
                periode,
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(etablertTilsyn)));

        var feil = validator.verifyHarFeil(to);
        feilInneholder(feil, "ugyldigVerdi");
    }

    @Test
    public void etablertTilsynTimerPerDagUnderMinVerdi() {
        Periode periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        Duration etablertTilsyn = Duration.ofMinutes(-1);
        var to = new Tilsynsordning().medPerioder(Map.of(
                periode,
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(etablertTilsyn)));

        var feil = validator.verifyHarFeil(to);
        feilInneholder(feil, "ugyldigVerdi");
    }

    @Test
    public void etablertTilsynTimerPerDagUnderOkMaks() {
        Periode periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        Duration etablertTilsyn = Duration.ofHours(7).plusMinutes(3);
        var to = new Tilsynsordning().medPerioder(Map.of(
                periode,
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(etablertTilsyn)));

        validator.verifyIngenFeil(to);
    }

    @Test
    public void etablertTilsynTimerPerDagUnderOkMin() {
        Periode periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        Duration etablertTilsyn = Duration.ofHours(0).plusMinutes(0);
        var to = new Tilsynsordning().medPerioder(Map.of(
                periode,
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(etablertTilsyn)));

        validator.verifyIngenFeil(to);
    }

}