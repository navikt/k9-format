package no.nav.k9.søknad.omsorgspenger;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

class OmsorgspengerSøknadValidator extends SøknadValidator<OmsorgspengerSøknad> {

    OmsorgspengerSøknadValidator() {}

    @Override
    public List<Feil> valider(OmsorgspengerSøknad søknad) {
        List<Feil> feil = new ArrayList<>();

        validerSøknadId(søknad.søknadId, feil);
        validerVersjon(søknad.versjon, feil);
        validerMottattDato(søknad.mottattDato, feil);
        validerSøker(søknad.søker, feil);
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

    private static void validerBarn(Barn barn, List<Feil> feil) {
        if (barn == null) {
            feil.add(new Feil("barn", PÅKREVD, "Barn må settes i søknaden."));
        } else if (barn.norskIdentitetsnummer == null && barn.fødselsdato == null) {
            feil.add(new Feil("barn", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        } else if (barn.norskIdentitetsnummer != null && barn.fødselsdato != null) {
            feil.add(new Feil("barn", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        }
    }
}
