package no.nav.k9.søknad.felles.validering.periode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import no.nav.k9.søknad.felles.type.Periode;

public class GyldigPeriodeValidator implements ConstraintValidator<GyldigPeriode, Periode> {

    private boolean krevFom;
    private boolean krevTom;

    @Override
    public void initialize(GyldigPeriode konfig) {
        krevFom = konfig.krevFomDato();
        krevTom = konfig.krevTomDato();
    }

    @Override
    public boolean isValid(Periode periode, ConstraintValidatorContext constraintContext) {
        if (periode == null) {
            return true; //hvis påkrevd, bruk @NotNull
        }
        boolean ok = true;
        if (krevFom && periode.getFraOgMed() == null) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("[påkrevd] Fra og med (FOM) må være satt.")
                    .addConstraintViolation();
            ok = false;
        }
        if (krevTom && periode.getTilOgMed() == null) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("[påkrevd] Til og med (TOM) må være satt.")
                    .addConstraintViolation();
            ok = false;
        }
        if (periode.isTilOgMedFørFraOgMed()) {
            //valideres direkte på Periode vha @Valid
            //unngår å ta med her, for å unngå duplikater
        }
        return ok;
    }

}