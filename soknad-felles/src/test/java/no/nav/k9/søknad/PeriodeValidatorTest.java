package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Periode;
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
                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(10)).build(), true,
                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(5)).build(), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(tom).build(), true,
                Periode.builder().fraOgMed(tom.plusDays(1)).tilOgMed(tom.plusDays(5)).build(), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(Collections.emptyList()));
    }

    @Test
    public void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(tom).build(), true,
                Periode.builder().fraOgMed(tom).tilOgMed(tom.plusDays(5)).build(), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void tilOgMedFørFraOgmed() {
        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().minusDays(5)).build(), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void manglerFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().build(), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void fraOgMedSattTilOgMedIkkeSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().fraOgMed(LocalDate.now()).build(), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void fraOgMedIkkeSattTilOgMedSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                Periode.builder().tilOgMed(LocalDate.now()).build(), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }
}
