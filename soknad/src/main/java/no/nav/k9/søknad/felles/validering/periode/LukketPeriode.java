package no.nav.k9.søknad.felles.validering.periode;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.Valid;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})

@Valid
@GyldigPeriode(krevFomDato = true, krevTomDato = true)
public @interface LukketPeriode {

    String message() default "Ugyldig periode";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
