package no.nav.k9.søknad.omsorgspenger;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerUtvidetRett;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/** byttet til {@link OmsorgspengerUtvidetRett} */
@Deprecated(forRemoval = true)
public class OmsorgspengerSøknadValidator extends SøknadValidator<OmsorgspengerSøknad> {

    OmsorgspengerSøknadValidator() {
    }

    private static void validerSøknadId(SøknadId søknadId, List<Feil> feil) {
        if (søknadId == null) {
            feil.add(new Feil("søknadId", PÅKREVD, "ID må settes i søknaden."));
        }
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()) {
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
        } else if (søker.getPersonIdent() == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(Barn barn, List<Feil> feil) {
        if (barn == null) {
            feil.add(new Feil("barn", PÅKREVD, "Barn må settes i søknaden."));
        } else if (barn.getPersonIdent() == null && barn.getFødselsdato() == null) {
            feil.add(new Feil("barn", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        } else if (barn.getPersonIdent() != null && barn.getFødselsdato() != null) {
            feil.add(new Feil("barn", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        }
    }

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
}
