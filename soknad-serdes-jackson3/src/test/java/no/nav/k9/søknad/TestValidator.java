package no.nav.k9.søknad;


import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import no.nav.k9.søknad.felles.Feil;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TestValidator {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    public List<Feil> valider(Object test) {

        var validate = VALIDATOR_FACTORY.getValidator().validate(test);

        return validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());
    }

    public List<Feil> verifyHarFeil(Object test) {
        final List<Feil> feil = valider(test);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public List<Feil> verifyIngenFeil(Object test) {
        final List<Feil> feil = valider(test);
        assertThat(feil).isEmpty();
        return feil;
    }

}
