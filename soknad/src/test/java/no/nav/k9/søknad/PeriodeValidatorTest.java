package no.nav.k9.søknad;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;

public class PeriodeValidatorTest {

    private static final PeriodeValidator validator = new PeriodeValidator();
    private static final String felt = "test";

    @Test
    public void perioderMedGylidgFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), LocalDate.now().plusDays(10)), true,
                new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    public void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom.plusDays(1), tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isEmpty();
    }

    @Test
    public void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom, tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    public void tilOgMedFørFraOgmed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), LocalDate.now().minusDays(5)), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    public void manglerFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    public void fraOgMedSattTilOgMedIkkeSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    public void fraOgMedIkkeSattTilOgMedSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, LocalDate.now()), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }
}
