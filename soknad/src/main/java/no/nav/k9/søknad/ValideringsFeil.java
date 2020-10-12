package no.nav.k9.søknad;

import java.util.List;

import no.nav.k9.søknad.felles.Feil;

public class ValideringsFeil extends RuntimeException {
    private final List<Feil> feil;

    public ValideringsFeil(List<Feil> feil) {
        super(feil.toString());
        this.feil = feil;
    }

    public List<Feil> getFeil() {
        return feil;
    }
}
