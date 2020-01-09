package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Feil;
import no.nav.k9.soknad.felles.Periode;
import no.nav.k9.soknad.felles.Periodisert;

import java.util.ArrayList;
import java.util.List;

public class PeriodeValidator {

    public List<Feil> validerTillattOverlapp(
            List<? extends Periodisert> perioder,
            String felt) {
        return valider(perioder, felt, true);
    }

    public List<Feil> validerIkkeTillattOverlapp(
            List<? extends Periodisert> perioder,
            String felt) {
        return valider(perioder, felt, false);
    }

    public List<Feil> valider(
            Periodisert periode,
            String felt) {
        List<Feil> feil = new ArrayList<>();
        validerGyldigPeriode(periode, felt, feil);
        return feil;
    }

    private static List<Feil> valider(
            List<? extends Periodisert> perioder,
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
            List<? extends Periodisert> perioder,
            String felt,
            List<Feil> feil) {
        int index = 0;
        for (Periodisert periodisert : perioder) {
            validerGyldigPeriode(periodisert, felt(felt, index++), feil);
        }
    }

    private static void validerGyldigPeriode(
            Periodisert periodisert,
            String felt,
            List<Feil> feil) {
        if (periodisert == null || periodisert.getPeriode() == null) {
            feil.add(new Feil(felt, "paakrevd", "Perioden må være satt."));
        } else {
            if (periodisert.getPeriode().fraOgMed == null) {
                feil.add(new Feil(felt, "paakrevd", "Fra og med (FOM) må være satt."));
            }
            if (periodisert.getPeriode().fraOgMed != null && periodisert.getPeriode().tilOgMed != null && periodisert.getPeriode().tilOgMed.isBefore(periodisert.getPeriode().fraOgMed)) {
                feil.add(new Feil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
            }
        }
    }

    private static void validerOverlappendePerioder(
            List<? extends Periodisert> perioder,
            String felt,
            List<Feil> feil) {
        for (int i = 0; i < perioder.size(); i++) {
            Periode sjekkPeriode = perioder.get(i) != null ? perioder.get(i).getPeriode() : null;
            for (int j = 0; j < perioder.size(); j++) {
                if (i == j) break;
                if (overlapper(sjekkPeriode, perioder.get(j) != null ? perioder.get(j).getPeriode() : null)) {
                    feil.add(new Feil(felt(felt, i), "overlappendePerioder", "Perioden overlapper andre perioder som er angitt."));
                    return;
                }
            }
        }
    }

    private static String felt(String felt, int index) {
        return felt + "[" + index + "]";
    }

    private static String felt(String felt, int index, String underfelt){
        return felt(felt,index) + "." + underfelt;
    }

    private static boolean overlapper(Periode periode1, Periode periode2) {
        // Ugyldige perioder
        if (periode1 == null || periode2 == null || periode1.fraOgMed == null || periode2.fraOgMed == null) {
            return false;
        }

        // Hele dager; Kan ikke føre opp fler perioder samme dag
        if ((periode2.fraOgMed.equals(periode1.tilOgMed)) || (periode1.fraOgMed.equals(periode2.fraOgMed))) return true;

        // Om ingen av periodene har en tilOgMed kan de ikke overlappe
        if (periode1.tilOgMed == null && periode2.tilOgMed == null) return false;


        return (periode1.tilOgMed == null || !periode1.tilOgMed.isBefore(periode2.fraOgMed)) && (periode2.tilOgMed == null || !periode1.fraOgMed.isAfter(periode2.tilOgMed));
    }

}
