package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;

public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<OmsorgspengerUtbetalingSøknad> {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(OmsorgspengerUtbetalingSøknad søknad) {
        List<Feil> feil = validerSøknad(søknad);
        validerVersjon(søknad.versjon, feil);
        validerSøker(søknad.søker, feil);
        validerFosterbarn(søknad.fosterbarn, feil);
        return feil;
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null || !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker != null && søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerFosterbarn(List<Barn> barn, List<Feil> feil) {
        if (barn == null || barn.isEmpty())
            return;
        var index = 0;
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd",
                    "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(
                    new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
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
