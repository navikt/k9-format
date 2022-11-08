package no.nav.k9.søknad.felles.validering.periode;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import no.nav.k9.søknad.felles.type.Periode;

public class GyldigePerioderMapValidator implements ConstraintValidator<GyldigePerioderMap, Map<Periode, ?>> {

    private boolean krevFom;
    private boolean krevTom;

    @Override
    public void initialize(GyldigePerioderMap konfig) {
        //kan i fremtiden velge å ta inn flere parametre for å spesialisere. Eksempelvis: sjekke for overlapp
        krevFom = konfig.krevFomDato();
        krevTom = konfig.krevTomDato();
    }

    @Override
    public boolean isValid(Map<Periode, ?> value, ConstraintValidatorContext constraintContext) {
        int i = 0;
        boolean ok = true;
        if (value == null) {
            return true; //hvis påkrevd, bruk @NotNull
        }
        for (Periode periode : value.keySet()) {
            if (krevFom && periode.getFraOgMed() == null) {
                constraintContext.disableDefaultConstraintViolation();
                constraintContext.buildConstraintViolationWithTemplate("[påkrevd] Fra og med (FOM) må være satt.")
                        .addContainerElementNode("['" + periode + "']", Periode.class, i)
                        .addConstraintViolation();
                ok = false;
            }
            if (krevTom && periode.getTilOgMed() == null) {
                constraintContext.disableDefaultConstraintViolation();
                constraintContext.buildConstraintViolationWithTemplate("[påkrevd] Til og med (TOM) må være satt.")
                        .addContainerElementNode("['" + periode + "']", Periode.class, i)
                        .addConstraintViolation();
                ok = false;
            }
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