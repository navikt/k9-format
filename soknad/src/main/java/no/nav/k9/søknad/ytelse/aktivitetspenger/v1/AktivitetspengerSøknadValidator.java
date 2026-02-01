package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

import java.util.List;
import java.util.stream.Collectors;

public class AktivitetspengerSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Søknad søknad) {
        List<Feil> feil = validerSøknadsfelter(søknad);
        // legg til valideringer her dersom ikke behovet dekkes av annotasjoner på feltene
        return feil;
    }

    private List<Feil> validerSøknadsfelter(Søknad søknad) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());

        if (AvbrytendeValideringsfeil.harAvbrytendeValideringsfeil(validate)) {
            return feil;
        }

        validerFelterPåSøknad(søknad, feil);
        return feil;
    }
}
