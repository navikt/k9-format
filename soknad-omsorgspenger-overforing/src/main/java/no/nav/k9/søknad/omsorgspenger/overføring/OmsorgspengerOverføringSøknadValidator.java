package no.nav.k9.søknad.omsorgspenger.overføring;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import no.nav.k9.søknad.felles.Versjon;

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
}

