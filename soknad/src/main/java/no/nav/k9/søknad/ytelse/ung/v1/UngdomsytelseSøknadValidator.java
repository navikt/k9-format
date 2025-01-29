package no.nav.k9.søknad.ytelse.ung.v1;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

import java.util.List;
import java.util.stream.Collectors;

public class UngdomsytelseSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Søknad søknad) {
        List<Feil> feil = validerSøknadsfelter(søknad);
        validerYtelse(søknad, feil);
        return feil;
    }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        return validerSøknadsfelter(søknad);
    }

    private List<Feil> validerYtelse(Søknad søknad, List<Feil> feil) {
        Ungdomsytelse ytelse = søknad.getYtelse();

        if (ytelse.getSøknadType() == UngSøknadstype.DELTAKELSE_SØKNAD && (ytelse.getStartdatoer() == null || ytelse.getStartdatoer().isEmpty())) {
            feil.add(new Feil("søktFraDatoer", PÅKREVD, "Deltakelsesøknad må sette minst en startdato"));
        }

        if (ytelse.getSøknadType() == UngSøknadstype.RAPPORTERING_SØKNAD && ytelse.getInntekter() == null) {
            feil.add(new Feil("inntekter", PÅKREVD, "Rapporteringsinnsending må sette inntekter"));
        }

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
