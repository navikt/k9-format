package no.nav.k9.søknad.ytelse.psb.v1;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;

public class PleiepengerSyktBarnSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    public PleiepengerSyktBarnSøknadValidator() {
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon != null && !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerBarnIkkeErSøker(Søker søker, List<Person> barnList, List<Feil> feil) {
        if (søker == null || barnList == null || barnList.isEmpty()) {
            return;
        }
        if (barnList.stream().anyMatch(person -> person.getPersonIdent() != null && person.getPersonIdent().equals(søker.getPersonIdent()))) {
            feil.add(new Feil("søker", "søkerSammeSomBarn", "Søker kan ikke være barn."));
        }
    }

    private Feil toFeil(ConstraintViolation<Søknad> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    @Override
    public List<Feil> valider(Søknad søknad) {
        return valider(søknad, List.of());
    }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        validerVersjon(søknad.getVersjon(), feil);
        validerBarnIkkeErSøker(søknad.getSøker(), søknad.getBerørtePersoner(), feil);
        feil.addAll(new PleiepengerSyktBarnYtelseValidator().valider(søknad.getYtelse(), gyldigeEndringsperioder));

        return feil;
    }

    public void forsikreValidert(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        List<Feil> feil = valider(søknad, gyldigeEndringsperioder);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

}
