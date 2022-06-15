package no.nav.k9.søknad;

import java.time.ZonedDateTime;
import java.util.List;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;

public abstract class SøknadValidator<S> {
    protected static final String PÅKREVD = "påkrevd";
    protected static final String UGYLDIG_ARGUMENT = "ugyldig argument";

    public abstract List<Feil> valider(S søknad);

    public void forsikreValidert(S søknad) {
        List<Feil> feil = valider(søknad);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }
    
    
    /**
     * Felles validering av felter på søknad. Må kalles eksplisitt på av
     * subklassene til denne klassen hvis den skal brukes,
     * 
     * @param søknad Søknaden som valideres.
     * @param feil En liste der eventuelt feil skal legges til.
     */
    public final void validerFelterPåSøknad(Søknad søknad, List<Feil> feil) {
        validerMottattDato(søknad, feil);
        validerVersjon(søknad.getVersjon(), feil);
    }
    
    private void validerMottattDato(Søknad søknad, List<Feil> feil) {
        final int maximumClockSkewSeconds = 10;
        if (søknad.getMottattDato().minusSeconds(maximumClockSkewSeconds).isAfter(ZonedDateTime.now())) {
            feil.add(new Feil("mottattDato", "fremtidigDatoIkkeTillat", "Mottattdato kan ikke være satt til fremtiden."));
        }
    }
    
    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon != null && !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }
}
