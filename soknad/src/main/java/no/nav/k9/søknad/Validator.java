package no.nav.k9.søknad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;

public class Validator {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    protected static final String PÅKREVD = "påkrevd";
    private final PeriodeValidator periodeValidator;

    public Validator() {
        this.periodeValidator = new PeriodeValidator();
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
        if (barn == null || barn.isEmpty()) return;
        var index = 0;
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
        }
    }

    public List<Feil> valider(Søknad søknad) {
        List<Feil> feil = validerSøknad(søknad);
        validerVersjon(søknad.getVersjon(), feil);
        validerSøker(søknad.getSøker(), feil);
        validerUtenlandsopphold(søknad.getUtenlandsopphold(), feil);
        validerBosteder(søknad.getBosteder(), feil);
        validerYtelse(søknad, feil);
        return feil;
    }

    public void forsikreValidert(Søknad søknad) {
        var feil = valider(søknad);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    private void validerYtelse(Søknad søknad, List<Feil> feil) {
        var ytelse = søknad.getYtelse();
        // TODO: Hent ut ytelse-validator
        var validator = ytelse.getValidator();

        // TODO: validere
        var ytelseFeil = validator.valider(ytelse);
        feil.addAll(ytelseFeil);
    }

    private void validerUtenlandsopphold(Utenlandsopphold utenlandsopphold, List<Feil> feil) {
        if (utenlandsopphold == null) return;
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(utenlandsopphold.perioder, "utenlandsopphold.perioder"));
    }

    private void validerBosteder(Bosteder bosteder, List<Feil> feil) {
        if (bosteder == null) return;
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(bosteder.perioder, "bosteder.perioder"));
    }

    private List<Feil> validerSøknad(Søknad søknad) {
        var constraints = VALIDATOR_FACTORY.getValidator().validate(søknad);
        if (constraints != null && !constraints.isEmpty()) {
            return constraints.stream()
                    .map((v) -> new Feil(v.getPropertyPath().toString(), PÅKREVD, v.getMessage()))
                    .collect(Collectors.toList());
        } else return new ArrayList<>();
    }
}
