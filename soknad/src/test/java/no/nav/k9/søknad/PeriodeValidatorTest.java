package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.type.Periode;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PeriodeValidatorTest {

    private static final PeriodeValidator validator = new PeriodeValidator();
    private static final String felt = "test";

    @Test
    public void perioderMedGylidgFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), LocalDate.now().plusDays(10)), true,
                new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom.plusDays(1), tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(Collections.emptyList()));
    }

    @Test
    public void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom, tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void tilOgMedFørFraOgmed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), LocalDate.now().minusDays(5)), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void manglerFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void fraOgMedSattTilOgMedIkkeSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void fraOgMedIkkeSattTilOgMedSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, LocalDate.now()), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }
}
