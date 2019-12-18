package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.Feil;
import no.nav.k9.soknad.felles.Opphold;
import no.nav.k9.soknad.felles.Periode;

import java.util.ArrayList;
import java.util.List;

public class InnsendingValidator {

    public List<Feil> validate(PleiepengerBarnSoknad soknad) {
        final List<Feil> resultat = new ArrayList<>();

        mottattDatoErPaakrevd(soknad, resultat);
        sokerNorskIdentitetsnummerPaakrevd(soknad, resultat);
        validateSoknadsperiode(soknad, resultat);
        // TODO: relasjonTilBarnet
        validateMedlemskap(soknad, resultat);

        return resultat;
    }


    private static void mottattDatoErPaakrevd(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        if (soknad.getMottattDato() == null) {
            resultat.add(new Feil("mottattDato", "mottattDatoPaakrevd", "Dato for mottak av søknaden må fylles ut."));
        }
    }

    private static void sokerNorskIdentitetsnummerPaakrevd(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        if (soknad.getSoker().norskIdentitetsnummer == null) {
            resultat.add(new Feil("soker.norskIdentitetsnummer", "sokerNorskIdentitetsnummerPaakrevd", "Fødsels-/D-nummer for søker må fylles ut."));
        }
    }

    private static void validateSoknadsperiode(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        final Periode periode = soknad.getPeriode();
        if (periode.getFraOgMed() == null) {
            resultat.add(new Feil("periode.fraOgMed", "periodeFraOgMedPaakrevd", "Fra og med (dato) for perioden det søkes om må fylles ut."));
        }
        if (periode.getTilOgMed() == null) {
            resultat.add(new Feil("periode.tilOgMed", "periodeTilOgMedPaakrevd", "Til og med (dato) for perioden det søkes om må fylles ut."));
        }
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getFraOgMed().isAfter(periode.getTilOgMed())) {
            resultat.add(new Feil("periode.tilOgMed", "periodeTilOgMedMaaVaereEtterFraOgMed", "Til og med (dato) må være etter fra og med (dato)."));
        }
    }

    private static void validateMedlemskap(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        final List<Opphold> oppholdene = soknad.getMedlemskap().getOpphold();
        for (int i=0; i<oppholdene.size(); i++) {
            final Opphold opphold = oppholdene.get(i);

            validateOppholdsperiode(resultat, opphold, i);
            finnOverlappendePeriodeFoerIndex(resultat, oppholdene, i);
        }
    }

    private static void validateOppholdsperiode(List<Feil> resultat, Opphold opphold, int index) {
        if (opphold.getLand() == null) {
            resultat.add(new Feil("medlemskap.opphold[" + index + "].land", "oppholdslandMaaVaereSatt", "Oppholdsland for periode må være satt."));
        }

        final Periode periode = opphold.getPeriode();
        if (periode == null) {
            resultat.add(new Feil("medlemskap.opphold[" + index + "].periode", "oppholdPeriodePaakrevd", "Periode for oppholdet må være satt."));
            return;
        }
        if (periode.getFraOgMed() == null && periode.getTilOgMed() == null) {
            resultat.add(new Feil("medlemskap.opphold[" + index + "].periode", "oppholdPeriodeFraOgMedPaakrevd", "Perioden må ha satt fra og med (dato) og/eller til og med (dato)."));
        }
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getFraOgMed().isAfter(periode.getTilOgMed())) {
            resultat.add(new Feil("medlemskap.opphold[" + index + "].periode.tilOgMed", "periodeTilOgMedMaaVaereEtterFraOgMed", "Til og med (dato) må være etter fra og med (dato)."));
        }
    }

    private static void finnOverlappendePeriodeFoerIndex(List<Feil> resultat, List<Opphold> oppholdene, int index) {
        final Periode periode = oppholdene.get(index).getPeriode();
        for (int j = 0; j<index; j++) {
            final Periode sjekkPeriode = oppholdene.get(j).getPeriode();
            if (overlaps(periode, sjekkPeriode)) {
                resultat.add(new Feil("medlemskap.opphold[" + j + "].periode", "oppholdsperiodeOverlapper", "Angitt periode for opphold overlapper med en annen periode."));
                return;
            }
        }
    }

    private static final boolean overlaps(Periode periode1, Periode periode2) {
        if (periode1 == null || periode2 == null) {
            return false;
        }
        return (periode2.getFraOgMed() == null  || periode1.getTilOgMed() == null || !periode1.getTilOgMed().isBefore(periode2.getFraOgMed()))
                && (periode1.getFraOgMed() == null || periode2.getTilOgMed() == null || !periode1.getFraOgMed().isAfter(periode2.getTilOgMed()));
    }
}
