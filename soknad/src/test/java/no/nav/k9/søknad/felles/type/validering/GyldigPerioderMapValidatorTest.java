package no.nav.k9.søknad.felles.type.validering;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;

class GyldigPerioderMapValidatorTest {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    static class TestDto {

        @GyldigePerioderMap(krevFomDato = true, krevTomDato = false)
        private Map<Periode, Boolean> åpnePerioder;

        @GyldigePerioderMap(krevFomDato = true, krevTomDato = true)
        private Map<Periode, Boolean> lukkedePerioder;

        public TestDto medÅpnePerioder(Map<Periode, Boolean> åpnePerioder) {
            this.åpnePerioder = åpnePerioder;
            return this;
        }

        public TestDto medLukkedePerioder(Map<Periode, Boolean> lukkedePerioder) {
            this.lukkedePerioder = lukkedePerioder;
            return this;
        }
    }

    @Test
    void perioderMedGylidgFraOgMedOgTilOgMed() {
        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(LocalDate.now(), LocalDate.now().plusDays(10)), true,
                new Periode(LocalDate.now(), LocalDate.now().plusDays(5)), true
        ));
        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).isEmpty();
    }

    @Test
    void perioderMedStartdatoEnDagEtterForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom.plusDays(1), tom.plusDays(5)), true
        ));

        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).isEmpty();
    }

    @Test
    void perioderMedStartdatoLikForegåendeSluttdato() {
        LocalDate tom = LocalDate.now().plusDays(10);

        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(LocalDate.now(), tom), true,
                new Periode(tom, tom.plusDays(5)), true
        ));

        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).isEmpty();
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
    void manglerPåkrevdFraOgMed() {
        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(null, null), true
        ));
        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).containsOnly("[påkrevd] Fra og med (FOM) må være satt.");
    }

    @Test
    void fraOgMedSattTilOgMedIkkeSatt() {
        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(LocalDate.now(), null), true
        ));
        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).isEmpty();
    }

    @Test
    void fraOgMedSattPåkrevdTilOgMedIkkeSatt() {
        var dto = new TestDto().medLukkedePerioder(Map.of(
                new Periode(LocalDate.now(), null), true
        ));
        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).containsOnly("[påkrevd] Til og med (TOM) må være satt.");
    }

    @Test
    void fraOgMedIkkeSattTilOgMedSatt() {
        var dto = new TestDto().medÅpnePerioder(Map.of(
                new Periode(null, LocalDate.now()), true
        ));
        Validator validator = VALIDATOR_FACTORY.getValidator();
        List<String> feilmeldinger = validator.validate(dto).stream().map(ConstraintViolation::getMessage).toList();
        Assertions.assertThat(feilmeldinger).containsOnly("[påkrevd] Fra og med (FOM) må være satt.");
    }
}
