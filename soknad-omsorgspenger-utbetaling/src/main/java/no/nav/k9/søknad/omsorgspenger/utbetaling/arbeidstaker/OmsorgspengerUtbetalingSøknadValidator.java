package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;

/**
 * @deprecated ikke bruk
 */
@Deprecated(forRemoval = true, since = "5.0.2")
@SuppressWarnings("removal")
public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<OmsorgspengerUtbetalingSøknad> {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(OmsorgspengerUtbetalingSøknad søknad) {
        List<Feil> feil = validerSøknad(søknad);
        validerVersjon(søknad.versjon, feil);
        validerSøker(søknad.søker, feil);
        return feil;
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null || !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker != null && søker.getPersonIdent() == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private List<Feil> validerSøknad(OmsorgspengerUtbetalingSøknad søknad) {
        var constraints = VALIDATOR_FACTORY.getValidator().validate(søknad);
        if (constraints != null && !constraints.isEmpty()) {
            return constraints.stream()
                .map((v) -> new Feil(v.getPropertyPath().toString(), PÅKREVD, v.getMessage()))
                .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }
}
