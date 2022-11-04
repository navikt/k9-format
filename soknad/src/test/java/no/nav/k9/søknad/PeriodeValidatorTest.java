package no.nav.k9.søknad;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;

class PeriodeValidatorTest {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final PeriodeValidator validator = new PeriodeValidator();
    private static final String felt = "test";

    @Test
    void perioderMedGylidgFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), LocalDate.now().plusDays(10)), true,
                new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom.plusDays(1), tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isEmpty();
    }

    @Test
    void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom, tom.plusDays(5)), true
        );

        assertThat(validator.validerTillattOverlapp(perioder, felt)).isEmpty();
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    void tilOgMedFørFraOgmed() {
        Periode periode = new Periode("2022-01-06/2022-01-01");
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<Periode>> resultat = validator.validate(periode);
        List<String> feilmeldinger = resultat.stream().map(ConstraintViolation::getMessage).toList();
        assertThat(feilmeldinger).containsOnly("[ugyldigPeriode] Fra og med (FOM) må være før eller lik til og med (TOM).");
    }

    @Test
    void manglerFraOgMedOgTilOgMed() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    void fraOgMedSattTilOgMedIkkeSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(LocalDate.now(), null), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }

    @Test
    void fraOgMedIkkeSattTilOgMedSatt() {
        Map<Periode, Boolean> perioder = Map.of(
                new Periode(null, LocalDate.now()), true
        );
        assertThat(validator.validerIkkeTillattOverlapp(perioder, felt)).isNotEmpty();
    }
}
