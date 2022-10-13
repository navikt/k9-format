package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.util.List;

import no.nav.k9.søknad.felles.Feil;

class ValideringsAvbrytendeFeilException extends RuntimeException {

    private final List<Feil> feilList;

    public ValideringsAvbrytendeFeilException(List<Feil> feilList) {
        this.feilList = feilList;
    }

    public List<Feil> getFeilList() {
        return feilList;
    }
}
