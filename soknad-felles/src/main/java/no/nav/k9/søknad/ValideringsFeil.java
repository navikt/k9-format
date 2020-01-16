package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Feil;

import java.util.List;

public class ValideringsFeil extends RuntimeException {
    private final List<Feil> feil;
    ValideringsFeil(List<Feil> feil) {
        super(feil.toString());
        this.feil = feil;
    }

    public List<Feil> getFeil() {
        return feil;
    }
}
