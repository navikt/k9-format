package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;

public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<Søknad> {


    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    public OmsorgspengerUtbetalingSøknadValidator() {
    }

    @Override
    public List<Feil> valider(Søknad søknad) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());

        validerFelterPåSøknad(søknad, feil);
        feil.addAll(new OmsorgspengerUtbetalingValidator().valider(søknad.getYtelse()));

        return feil;
    }
}
