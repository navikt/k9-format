package no.nav.k9.søknad.ytelse.ung.v1;

import java.util.List;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;

class UngSøknadValidator extends SøknadValidator {

    @Override
    public List<Feil> valider(Object søknad) {
        return List.of();
    }

}
