package no.nav.k9.søknad.ytelse;

import java.util.List;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

public abstract class YtelseValidator {
    protected static final String PÅKREVD = "påkrevd";
    protected static final String UGYLDIG_ARGUMENT = "ugyldig argument";

    public abstract List<Feil> valider(Ytelse søknad);
    
    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        return valider(ytelse);
    }

    public void forsikreValidert(Ytelse ytelse) {
        List<Feil> feil = valider(ytelse);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }
}
