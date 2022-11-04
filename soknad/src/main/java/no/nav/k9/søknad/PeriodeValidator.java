package no.nav.k9.søknad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

public class PeriodeValidator {

    private static List<Feil> valider(Map<Periode, ?> perioder,
                                      String felt,
                                      Boolean tillatOverlapp,
                                      Boolean tillatÅpnePerioder) {
        List<Feil> feil = new ArrayList<>();
        validerGyldigePerioder(perioder, felt, tillatÅpnePerioder, feil);
        if (!tillatOverlapp) {
            validerOverlappendePerioder(perioder, felt, feil);
        }
        return feil;
    }

    private static void validerGyldigePerioder(Map<Periode, ?> perioder,
                                               String felt,
                                               Boolean tillatÅpnePerioder,
                                               List<Feil> feil) {
        perioder.forEach((periode, value) -> validerGyldigPeriode(periode, felt(felt, periode), tillatÅpnePerioder, feil));
    }

    public static void validerGyldigPeriode(@NotNull Periode periode,
                                             String felt,
                                             Boolean tillatÅpnePerioder,
                                             List<Feil> feil) {
        if (periode == null) {
            return;
        }
        if (!tillatÅpnePerioder && periode.getTilOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (periode.getFraOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
    }


    private static void validerOverlappendePerioder(Map<Periode, ?> perioder, String felt, List<Feil> feil) {
        perioder.forEach((sjekkPeriode, sjekkPerideValue) ->
            validerOverlappendePerioder(perioder, sjekkPeriode, felt, feil));
    }

    public static void validerOverlappendePerioder(Map<Periode, ?> perioder, Periode sjekkPeriode , String felt, List<Feil> feil) {
        perioder.forEach((periode, periodeValue) -> {
            if (!sjekkPeriode.equals(periode) && overlapper(sjekkPeriode, periode)) {
                feil.add(new Feil(felt(felt, sjekkPeriode), "overlappendePerioder", "Perioden overlapper perioden '" + periode.getIso8601() + "'."));
            }
        });
    }

    private static String felt(String felt, Periode periode) {
        return felt + "[" + periode.getIso8601() + "]";
    }

    private static boolean overlapper(Periode periode1, Periode periode2) {
        // Ugyldige perioder
        if (periode1 == null || periode2 == null || periode1.getFraOgMed() == null || periode2.getFraOgMed() == null) {
            throw new IllegalArgumentException("Ugyldige perioder");
        }

        // Hele dager; Kan ikke føre opp fler perioder samme dag
        if ((periode2.getFraOgMed().equals(periode1.getTilOgMed())) || (periode1.getFraOgMed().equals(periode2.getFraOgMed()))) {
            return true;
        }

        // Om ingen av periodene har en tilOgMed (Flere åpne perioder) vil de alltid overlappe
        if (periode1.getTilOgMed() == null && periode2.getTilOgMed() == null) {
            return true;
        }

        return (periode1.getTilOgMed() == null || !periode1.getTilOgMed().isBefore(periode2.getFraOgMed())) && (periode2.getTilOgMed() == null || !periode1.getFraOgMed().isAfter(periode2.getTilOgMed()));
    }

    public List<Feil> validerTillattOverlappOgÅpnePerioder(Map<Periode, ?> perioder, String felt) {
        return valider(perioder, felt, true, true);
    }

    public List<Feil> validerTillattOverlapp(Map<Periode, ?> perioder, String felt) {
        return valider(perioder, felt, true, false);
    }

    public List<Feil> validerIkkeTillattOverlapp(Map<Periode, ?> perioder, String felt) {
        return valider(perioder, felt, false, false);
    }

    public List<Feil> valider(Periode periode, String felt) {
        List<Feil> feil = new ArrayList<>();
        validerGyldigPeriode(periode, felt, false, feil);
        return feil;
    }

    public static void validerPeriodeInnenforSøknadsperiode(Periode periode, String felt, Periode søknadsperiode, List<Feil> feil) {
        if (søknadsperiode == null) {
            return;
        }
        if (periode.getFraOgMed().isBefore(søknadsperiode.getFraOgMed())) {
            feil.add(new Feil( felt, "ugyldigPeriode",  "Perioden er før søknadsperioden, " + periode.getFraOgMed().toString()));
        }
        else if (periode.getTilOgMed().isAfter(søknadsperiode.getTilOgMed())) {
            feil.add(new Feil( felt, "ugyldigPeriode",   "Perioden er etter søknadsperioden, " + periode.getTilOgMed().toString()));
        }
    }
}
