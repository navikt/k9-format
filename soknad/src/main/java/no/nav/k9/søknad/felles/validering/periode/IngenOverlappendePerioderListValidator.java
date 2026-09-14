package no.nav.k9.søknad.felles.validering.periode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.List;

public class IngenOverlappendePerioderListValidator implements ConstraintValidator<IngenOverlappendePerioder, List<Periode>> {

    @Override
    public boolean isValid(List<Periode> perioder, ConstraintValidatorContext constraintContext) {
        if (perioder == null || perioder.isEmpty()) {
            return true;
        }

        return OverlappendePeriodeSjekk.valider(constraintContext, perioder);
    }
}
