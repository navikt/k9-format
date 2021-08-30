package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnSøknadValidator;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnYtelseValidator;

class ValiderUtil {

    private static final PleiepengerSyktBarnSøknadValidator validatorSøknad = new PleiepengerSyktBarnSøknadValidator();
    private static final PleiepengerSyktBarnYtelseValidator validatorYtelse = new PleiepengerSyktBarnYtelseValidator();


    protected static List<Feil> verifyHarFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    protected static List<Feil> verifyHarFeil(PleiepengerSyktBarn ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    protected static void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isEmpty();
    }

    protected static void verifyIngenFeil(PleiepengerSyktBarn ytelse) {
        final List<Feil> feil = valider(ytelse);
        assertThat(feil).isEmpty();
    }

    private static List<Feil> valider(Søknad søknad) {
        try {
            return validatorSøknad.valider(søknad);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    private static List<Feil> valider(PleiepengerSyktBarn ytelse) {
        try {
            return validatorYtelse.valider(ytelse);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

}
