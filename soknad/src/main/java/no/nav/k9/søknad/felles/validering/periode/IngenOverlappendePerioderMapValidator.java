package no.nav.k9.søknad.felles.validering.periode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.Map;

public class IngenOverlappendePerioderMapValidator implements ConstraintValidator<IngenOverlappendePerioder, Map<Periode, ?>> {

    @Override
    public boolean isValid(Map<Periode, ?> perioder, ConstraintValidatorContext constraintContext) {
        if (perioder == null || perioder.isEmpty()) {
            return true;
        }

        return OverlappendePeriodeSjekk.valider(constraintContext, perioder.keySet().stream().toList());
    }

}
