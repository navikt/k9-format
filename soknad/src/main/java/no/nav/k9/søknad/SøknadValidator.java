package no.nav.k9.søknad;

import java.util.List;

import no.nav.k9.søknad.felles.Feil;

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
