package no.nav.k9.søknad.felles.validering.periode;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IngenOverlappendePerioderMapValidator.class, IngenOverlappendePerioderListValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IngenOverlappendePerioder {

    String message() default "[ugyldigPeriodeInterval] Perioder kan ikke overlappe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
