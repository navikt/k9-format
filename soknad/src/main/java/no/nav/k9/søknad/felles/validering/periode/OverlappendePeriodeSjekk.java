package no.nav.k9.søknad.felles.validering.periode;

import jakarta.validation.ConstraintValidatorContext;
import no.nav.fpsak.tidsserie.LocalDateInterval;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.List;

final class OverlappendePeriodeSjekk {

    private OverlappendePeriodeSjekk() {
    }

    /**
     * Returnerer null hvis periodene ikke overlapper, ellers en feilmelding som navngir de to
     * overlappende periodene.
     */
    static String valider(List<Periode> perioder) {
        // Ufullstendige perioder (mangler fom/tom) valideres av @GyldigePerioderMap, ikke her.
        var komplettePerioder = perioder.stream()
                .filter(periode -> periode != null && periode.getFraOgMed() != null && periode.getTilOgMed() != null)
                .toList();

        for (int i = 0; i < komplettePerioder.size(); i++) {
            for (int j = i + 1; j < komplettePerioder.size(); j++) {
                Periode periode1 = komplettePerioder.get(i);
                Periode periode2 = komplettePerioder.get(j);
                if (tilInterval(periode1).overlaps(tilInterval(periode2))) {
                    return "Periodene " + periode1 + " og " + periode2 + " overlapper";
                }
            }
        }
        return null;
    }

    private static LocalDateInterval tilInterval(Periode periode) {
        return new LocalDateInterval(periode.getFraOgMed(), periode.getTilOgMed());
    }

    public static boolean valider(ConstraintValidatorContext constraintContext, List<Periode> perioder) {
        String overlappFeilmelding = valider(perioder);
        if (overlappFeilmelding == null) {
            return true;
        }

        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(
                        "[ugyldigPeriodeInterval] Perioder kan ikke overlappe: " + overlappFeilmelding)
                .addConstraintViolation();
        return false;
    }
}


