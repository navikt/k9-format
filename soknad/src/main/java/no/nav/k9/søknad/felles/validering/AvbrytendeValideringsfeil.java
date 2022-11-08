package no.nav.k9.søknad.felles.validering;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;

/**
 * brukes for å markere valideringsfeil som bør stoppe videre validering for å unngå duplikate- eller følgefeil
 */
public class AvbrytendeValideringsfeil implements Payload {

    public static <T> boolean harAvbrytendeValideringsfeil(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream().anyMatch(v -> v.getConstraintDescriptor().getPayload().stream().anyMatch(payload -> payload == AvbrytendeValideringsfeil.class));
    }

}
