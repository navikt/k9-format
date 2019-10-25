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


    void mottattDatoErPaakrevd(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        if (soknad.getMottattDato() == null) {
            resultat.add(new Feil("mottattDato", "mottattDatoPaakrevd", "Dato for mottak av søknaden må fylles ut."));
        }
    }

    void sokerNorskIdentitetsnummerPaakrevd(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        if (soknad.getSoker().getNorskIdentitetsnummer() == null) {
            resultat.add(new Feil("soker.norskIdentitetsnummer", "sokerNorskIdentitetsnummerPaakrevd", "Fødsels-/D-nummer for søker må fylles ut."));
        }
    }

    void validateSoknadsperiode(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
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

    private void validateMedlemskap(PleiepengerBarnSoknad soknad, List<Feil> resultat) {
        final List<Opphold> opphold = soknad.getMedlemskap().getOpphold();
        for (int i=0; i<opphold.size(); i++) {
            final Periode periode = opphold.get(i).getPeriode();
            if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getFraOgMed().isAfter(periode.getTilOgMed())) {
                resultat.add(new Feil("medlemskap.opphold[" + i + "].periode.tilOgMed", "periodeTilOgMedMaaVaereEtterFraOgMed", "Til og med (dato) må være etter fra og med (dato)."));
            }
        }
    }
}
