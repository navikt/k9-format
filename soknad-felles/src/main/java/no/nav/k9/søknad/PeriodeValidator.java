package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Periode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeriodeValidator {

    public List<Feil> validerTillattOverlapp(
            Map<Periode, ?> perioder,
            String felt) {
        return valider(perioder, felt, true);
    }

    public List<Feil> validerIkkeTillattOverlapp(
            Map<Periode, ?> perioder,
            String felt) {
        return valider(perioder, felt, false);
    }

    public List<Feil> valider(
            Periode periode,
            String felt) {
        List<Feil> feil = new ArrayList<>();
        validerGyldigPeriode(periode, felt, feil);
        return feil;
    }

    private static List<Feil> valider(
            Map<Periode, ?> perioder,
            String felt,
            Boolean tillattOverlapp) {
        List<Feil> feil = new ArrayList<>();
        validerGyldigePerioder(perioder, felt, feil);
        if (!tillattOverlapp) {
            validerOverlappendePerioder(perioder, felt, feil);
        }
        return feil;
    }

    private static void validerGyldigePerioder(
            Map<Periode, ?> perioder,
            String felt,
            List<Feil> feil) {
        perioder.forEach((periode, value) -> validerGyldigPeriode(periode, felt(felt, periode), feil));
    }

    private static void validerGyldigPeriode(
            Periode periode,
            String felt,
            List<Feil> feil) {
        if (periode == null) {
            feil.add(new Feil(felt, "påkrevd", "Perioden må være satt."));
        } else {
            if (periode.fraOgMed == null) {
                feil.add(new Feil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
            }
            if (periode.fraOgMed != null && periode.tilOgMed != null && periode.tilOgMed.isBefore(periode.fraOgMed)) {
                feil.add(new Feil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
            }
        }
    }

    private static void validerOverlappendePerioder(
            Map<Periode, ?> perioder,
            String felt,
            List<Feil> feil) {
        perioder.forEach((sjekkPeriode, sjekkPerideValue) -> {
            perioder.forEach((periode, periodeValue) -> {
                if (!sjekkPeriode.equals(periode) && overlapper(sjekkPeriode, periode)) {
                    feil.add(new Feil(felt(felt, sjekkPeriode), "overlappendePerioder", "Perioden overlapper perioden '" + periode.iso8601 + "'."));
                }
            });
        });
    }

    private static String felt(String felt, Periode periode) {
        return felt + "[" + periode.iso8601 + "]";
    }

    private static boolean overlapper(Periode periode1, Periode periode2) {
        // Ugyldige perioder
        if (periode1 == null || periode2 == null || periode1.fraOgMed == null || periode2.fraOgMed == null) {
            throw new IllegalArgumentException("Ugyldige perioder");
        }

        // Hele dager; Kan ikke føre opp fler perioder samme dag
        if ((periode2.fraOgMed.equals(periode1.tilOgMed)) || (periode1.fraOgMed.equals(periode2.fraOgMed))) {
            return true;
        }

        // Om ingen av periodene har en tilOgMed (Flere åpne perioder) vil de alltid overlappe
        if (periode1.tilOgMed == null && periode2.tilOgMed == null) {
            return true;
        }

        return (periode1.tilOgMed == null || !periode1.tilOgMed.isBefore(periode2.fraOgMed)) && (periode2.tilOgMed == null || !periode1.fraOgMed.isAfter(periode2.tilOgMed));
    }

}
