package no.nav.k9.søknad.ytelse.olp.v1;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

public class OpplæringspengerSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

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

    @Override
    public List<Feil> valider(Søknad søknad) {
        return valider(søknad, List.of());
    }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());

        if (AvbrytendeValideringsfeil.harAvbrytendeValideringsfeil(validate)) {
            return feil;
        }

        Opplæringspenger ytelse = (Opplæringspenger) søknad.getYtelse();
        validerInneholderBegrunnelseForInnsending(søknad, ytelse, feil);

        validerVersjon(søknad.getVersjon(), feil);
        validerBarnIkkeErSøker(søknad.getSøker(), søknad.getBerørtePersoner(), feil);
        feil.addAll(new OpplæringspengerYtelseValidator().validerMedGyldigEndringsperodeHvisDenFinnes(søknad.getYtelse(), gyldigeEndringsperioder));

        return feil;
    }

    private void validerInneholderBegrunnelseForInnsending(Søknad søknad, Opplæringspenger olp, List<Feil> feil) {
        if ((olp).getTrekkKravPerioder() != null &&
                !(olp).getTrekkKravPerioder().isEmpty()) {
            if (søknad.getBegrunnelseForInnsending().getTekst() == null ||
                    søknad.getBegrunnelseForInnsending().getTekst().isEmpty()) {
                feil.add(new Feil("begrunnelseForInnsending", "påkrevd", "Søknad inneholder trekk krav perioder uten begrunnelse for innsending."));
            }
        }
    }

}
