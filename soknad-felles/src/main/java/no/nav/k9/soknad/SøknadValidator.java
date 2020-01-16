package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Feil;

import java.util.List;

public abstract class SøknadValidator<S> {
    protected static final String PÅKREVD = "påkrevd";

    public abstract List<Feil> valider(S søknad);
    public void forsikreValidert(S søknad) {
        List<Feil> feil = valider(søknad);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }
}
