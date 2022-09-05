package no.nav.k9.søknad.ytelse.pls.v1;

import java.util.List;
import java.util.stream.Collectors;

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

public class PleiepengerLivetsSluttfaseSøknadValidator extends SøknadValidator<Søknad> {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Søknad søknad) {
        return valider(søknad, List.of());
    }

    public List<Feil> valider(Søknad søknad, List<Periode> gyldigeEndringsperioder) {
        var validate = VALIDATOR_FACTORY.getValidator().validate(søknad);

        List<Feil> feil = validate.stream()
                .map(Feil::toFeil)
                .collect(Collectors.toList());

        validerInneholderBegrunnelseDersomTrekkAvPerioder(søknad, feil);
        validerFelterPåSøknad(søknad, feil);
        validerPleietrengendeIkkeErSøker(søknad.getSøker(), søknad.getBerørtePersoner(), feil);
        validerPleietrengendeIkkeErSøker(søknad.getSøker(), List.of(((PleipengerLivetsSluttfase) søknad.getYtelse()).getPleietrengende()), feil);
        feil.addAll(new PleiepengerLivetsSluttfaseYtelseValidator().valider(søknad.getYtelse(), gyldigeEndringsperioder));

        return feil;
    }

    private static void validerInneholderBegrunnelseDersomTrekkAvPerioder(Søknad søknad, List<Feil> feil) {
        PleipengerLivetsSluttfase ytelse = søknad.getYtelse();
        if (ytelse.getTrekkKravPerioder() != null && !ytelse.getTrekkKravPerioder().isEmpty()) {
            if (søknad.getBegrunnelseForInnsending().getTekst() == null || søknad.getBegrunnelseForInnsending().getTekst().isEmpty()) {
                feil.add(new Feil("begrunnelseForInnsending", "påkrevd", "Søknad inneholder trekk krav perioder uten begrunnelse for innsending."));
            }
        }
    }

    private static void validerPleietrengendeIkkeErSøker(Søker søker, List<Person> pleietrengende, List<Feil> feil) {
        if (søker == null || pleietrengende == null || pleietrengende.isEmpty()) {
            return;
        }
        if (pleietrengende.stream().anyMatch(person -> person.getPersonIdent() != null && person.getPersonIdent().equals(søker.getPersonIdent()))) {
            feil.add(new Feil("søker", "søkerSammeSomPleietrengende", "Søker kan ikke være samme person som er i livets sluttfase."));
        }
    }
}
