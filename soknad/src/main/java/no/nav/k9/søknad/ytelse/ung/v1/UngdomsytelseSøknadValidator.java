package no.nav.k9.søknad.ytelse.ung.v1;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

public class UngdomsytelseSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Søknad søknad) {
        return List.of();
    }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
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
