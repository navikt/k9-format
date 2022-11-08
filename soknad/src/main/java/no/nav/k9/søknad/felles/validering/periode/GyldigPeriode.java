package no.nav.k9.søknad.felles.validering.periode;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {GyldigPeriodeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//package protected. Bruk @LukketPeriode istedet siden den er bundlet med @Valid (pga fom vs tom validering). Eller lag en @ÅpenPeriode bundlet med @Valid hvis du trenger det
public @interface GyldigPeriode {

    String message() default "Ugyldig periode";

    boolean krevFomDato() default false;

    boolean krevTomDato() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};
}
