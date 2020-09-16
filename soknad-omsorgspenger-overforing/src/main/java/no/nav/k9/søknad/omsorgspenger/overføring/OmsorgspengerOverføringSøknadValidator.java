package no.nav.k9.søknad.omsorgspenger.overføring;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OmsorgspengerOverføringSøknadValidator extends SøknadValidator<OmsorgspengerOverføringSøknad> {
    public OmsorgspengerOverføringSøknadValidator() {}

    @Override
    public List<Feil> valider(OmsorgspengerOverføringSøknad søknad) {
        List<Feil> feil = new ArrayList<>();

        validerSøknadId(søknad.søknadId, feil);
        validerVersjon(søknad.versjon, feil);
        validerMottattDato(søknad.mottattDato, feil);
        validerSøker(søknad.søker, feil);
        validerMottaker(søknad.mottaker, feil);
        validerBarn(søknad.barn, feil);

        return feil;
    }

    private static void validerSøknadId(SøknadId søknadId, List<Feil> feil) {
        if (søknadId == null) {
            feil.add(new Feil("søknadId", PÅKREVD, "ID må settes i søknaden."));
        }
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()){
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", PÅKREVD, "Mottatt dato må settes i søknaden."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker == null) {
            feil.add(new Feil("søker", PÅKREVD, "Søker må settes i søknaden."));
        } else if (søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerMottaker(Mottaker mottaker, List<Feil> feil) {
        if (mottaker == null) {
            feil.add(new Feil("mottaker", PÅKREVD, "Mottaker må settes i søknaden."));
        } else if (mottaker.norskIdentitetsnummer == null) {
            feil.add(new Feil("mottaker.norskIdentitetsnummer", PÅKREVD, "Mottakers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(List<Barn> barn, List<Feil> feil) {
        if (barn == null || barn.isEmpty()) return;
        var index = 0;
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("barn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(new Feil("barn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
            }
            index++;
        }
    }
}

