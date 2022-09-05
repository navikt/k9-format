package no.nav.k9.søknad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;


/** @deprecated bruk istedet spesifikke subklasser av {@link no.nav.k9.søknad.SøknadValidator} */
@Deprecated(forRemoval = true, since = "6.1.1")
public class Validator {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    protected static final String PÅKREVD = "påkrevd";

    public Validator() {
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

    public List<Feil> valider(Søknad dok) {
        List<Feil> feil = validerTilFeil(dok);
        validerVersjon(dok.getVersjon(), feil);
        validerSøker(dok.getSøker(), feil);
        validerYtelse(dok, feil);
        return feil;
    }

    public void forsikreValidert(Søknad dok) {
        var feil = valider(dok);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    private void validerYtelse(Søknad dok, List<Feil> feil) {
        var ytelse = dok.getYtelse();
        var validator = ytelse.getValidator(dok.getVersjon());

        var ytelseFeil = validator.valider(ytelse);
        feil.addAll(ytelseFeil);
    }

    public static List<Feil> validerTilFeil(Object obj) {
        var constraints = VALIDATOR_FACTORY.getValidator().validate(obj);
        if (constraints != null && !constraints.isEmpty()) {
            return constraints.stream()
                    .map((v) -> new Feil(v.getPropertyPath().toString(), PÅKREVD, v.getMessage()))
                    .collect(Collectors.toList());
        } else return new ArrayList<>();
    }
}
