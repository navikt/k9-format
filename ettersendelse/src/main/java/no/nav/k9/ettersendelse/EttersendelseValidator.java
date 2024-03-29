package no.nav.k9.ettersendelse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;

public class EttersendelseValidator extends SøknadValidator<Ettersendelse> {
    @Override
    public List<Feil> valider(Ettersendelse ettersendelse) {
        List<Feil> feil = new ArrayList<>();

        // I en ettersendelse må ikke søknadId settes.
        // Men om den er satt kan vi ruete deen samme veg som selve søknaden.
        validerVersjon(ettersendelse.getVersjon(), feil);
        validerMottattDato(ettersendelse.getMottattDato(), feil);
        validerSøker(ettersendelse.getSøker(), feil);
        validerYtelse(ettersendelse.getYtelse(), feil);
        //TODO valider type og pleietrengende når feltene tas i bruk

        return feil;
    }

    private static void validerYtelse(Ytelse ytelse, List<Feil> feil) {
        if (ytelse == null) {
            feil.add(new Feil("ytelse", PÅKREVD, "Ytelse må settes i ettersendelsen."));
        }
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i ettersendelsen."));
        } else if (!versjon.erGyldig()){
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", PÅKREVD, "Mottatt dato må settes i ettersendelsen."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker == null) {
            feil.add(new Feil("søker", PÅKREVD, "Søker må settes i ettersendelsen."));
        } else if (søker.getPersonIdent() == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i ettersendelsen."));
        }
    }
}
