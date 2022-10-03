package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;

public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Set<Versjon> STØTTEDE_VERSJONER = Set.of(
            Versjon.of("1.0.0"),
            Versjon.of("1.1.0") //støtte for normalarbeidstid ved delvis fravær
    );

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon != null && versjon.erGyldig() && !STØTTEDE_VERSJONER.contains(versjon)) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Bare følgende versjoner er støttet: " + STØTTEDE_VERSJONER));
        }
    }

    @Override
    public List<Feil> valider(Søknad søknad) { return valider(søknad, List.of()); }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());

        validerFelterPåSøknad(søknad, feil);

        OmsorgspengerUtbetaling ytelse = (OmsorgspengerUtbetaling) søknad.getYtelse();
        feil.addAll(new OmsorgspengerUtbetalingValidator(søknad.getVersjon()).validerMedGyldigEndringsperodeHvisDenFinnes(søknad.getYtelse(), gyldigeEndringsperioder));

        return feil;
    }
}
