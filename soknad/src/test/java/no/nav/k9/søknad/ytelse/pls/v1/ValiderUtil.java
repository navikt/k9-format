package no.nav.k9.søknad.ytelse.pls.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;

public class ValiderUtil {

    private static final PleiepengerLivetsSluttfaseSøknadValidator validatorSøknad = new PleiepengerLivetsSluttfaseSøknadValidator();
    private static final PleiepengerLivetsSluttfaseYtelseValidator validatorYtelse = new PleiepengerLivetsSluttfaseYtelseValidator();

    public static List<Feil> verifyHarFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyHarFeil(PleipengerLivetsSluttfase ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(PleipengerLivetsSluttfase ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isEmpty();
        return feil;
    }

    public static List<Feil> valider(Søknad søknad) {
        try {
            return validatorSøknad.valider(søknad);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    public static List<Feil> valider(PleipengerLivetsSluttfase ytelse) {
        try {
            return validatorYtelse.valider(ytelse);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }

    }

}
