package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Periode;
import no.nav.k9.soknad.felles.Periodisert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PeriodeValidatorTest {

    private static final PeriodeValidator validator = new PeriodeValidator();
    private static final String felt = "test";

    @Test
    public void perioderMedGylidgFraOgMedOgTilOgMed() {
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(10)).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(5)).build())
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(tom).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(tom.plusDays(1)).tilOgMed(tom.plusDays(5)).build())
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(Collections.emptyList()));
    }

    @Test
    public void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(tom).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(tom).tilOgMed(tom.plusDays(5)).build())
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt), is(Collections.emptyList()));
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void overlappendeÅpnePerioder() {
        List<EnPeriodisertEntitet> enLukketOgEnÅpenPeriode = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(10)).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now().plusDays(5)).build())
        );

        assertThat(validator.validerIkkeTillattOverlapp(enLukketOgEnÅpenPeriode, felt), is(not(Collections.emptyList())));


        List<EnPeriodisertEntitet> toÅpnePerioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now().plusDays(10)).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now().plusDays(5)).build())
        );

        assertThat(validator.validerIkkeTillattOverlapp(toÅpnePerioder, felt), is(Collections.emptyList()));

        List<EnPeriodisertEntitet> toÅpnePerioderFraSammeDato = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).build()),
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).build())
        );

        assertThat(validator.validerIkkeTillattOverlapp(toÅpnePerioderFraSammeDato, felt), is(not(Collections.emptyList())));

    }

    @Test
    public void tilOgMedFørFraOgmed() {
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().minusDays(5)).build())
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void manglerFraOgMedOgTilOgMed() {
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().build())
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    @Test
    public void fraOgMedSattTilOgMedIkkeSatt() {
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().fraOgMed(LocalDate.now()).build())
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(Collections.emptyList()));
    }

    @Test
    public void fraOgMedIkkeSattTilOgMedSatt() {
        List<EnPeriodisertEntitet> perioder = List.of(
                new EnPeriodisertEntitet(Periode.builder().tilOgMed(LocalDate.now()).build())
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt), is(not(Collections.emptyList())));
    }

    private static class EnPeriodisertEntitet implements Periodisert {
        private final Periode periode;

        private EnPeriodisertEntitet(Periode periode) {
            this.periode = periode;
        }
        @Override
        public Periode getPeriode() {
            return periode;
        }
    }
}
