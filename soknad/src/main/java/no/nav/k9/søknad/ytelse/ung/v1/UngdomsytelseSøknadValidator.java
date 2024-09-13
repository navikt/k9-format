package no.nav.k9.søknad.ytelse.ung.v1;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

class UngSøknadValidator extends SøknadValidator {

    @Override
    public List<Feil> valider(Object søknad) {
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
