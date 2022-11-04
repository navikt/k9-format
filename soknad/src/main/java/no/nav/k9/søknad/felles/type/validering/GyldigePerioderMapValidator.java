package no.nav.k9.søknad.felles.type.validering;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import no.nav.k9.søknad.felles.type.Periode;

public class GyldigePerioderMapValidator implements ConstraintValidator<GyldigePerioderMap, Map<Periode, ?>> {

    @Override
    public void initialize(GyldigePerioderMap gyldigePerioderMap) {
        //kan i fremtiden velge å ta inn parametre for å spesialisere eksempelvis: sjekke for overlapp, sjekke om periode er åpen
    }

    @Override
    public boolean isValid(Map<Periode, ?> value, ConstraintValidatorContext constraintContext) {
        int i = 0;
        boolean ok = true;
        for (Periode periode : value.keySet()) {
            if (periode.isTilOgMedFørFraOgMed()) {
                constraintContext.disableDefaultConstraintViolation();
                constraintContext.buildConstraintViolationWithTemplate("[ugyldigPeriode] Fra og med (FOM) må være før eller lik til og med (TOM).")
                        .addContainerElementNode("['" + periode + "']", Periode.class, i)
                        .addConstraintViolation();
                ok = false;
            }
            i++;
        }
        return ok;
    }

}