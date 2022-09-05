package no.nav.k9.søknad.ytelse;

import java.util.List;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;

/** @deprecated bruk istedet {@link no.nav.k9.søknad.SøknadValidator}, da den gjør mer helhetlig validering */
@Deprecated(forRemoval = true, since = "6.1.1")
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
    
    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        List<Feil> feil = valider(ytelse, gyldigeEndringsperioder);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }
}
