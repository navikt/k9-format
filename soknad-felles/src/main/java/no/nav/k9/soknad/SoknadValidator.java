package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Feil;

import java.util.List;

public abstract class SoknadValidator<S> {
    public abstract List<Feil> valider(S soknad);
    public void forsikreValidert(S soknad) {
        List<Feil> feil = valider(soknad);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }
}
