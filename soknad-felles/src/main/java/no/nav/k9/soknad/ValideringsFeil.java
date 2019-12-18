package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Feil;

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
