package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnSøknadValidator;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnYtelseValidator;

class ValiderUtil {

    private static final PleiepengerSyktBarnSøknadValidator validatorSøknad = new PleiepengerSyktBarnSøknadValidator();
    private static final PleiepengerSyktBarnYtelseValidator validatorYtelse = new PleiepengerSyktBarnYtelseValidator();


    public static List<Feil> verifyHarFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyHarFeil(PleiepengerSyktBarn ytelse) {
        return verifyHarFeil(ytelse, List.of());
    }

    public static List<Feil> verifyHarFeil(PleiepengerSyktBarn ytelse, List<Periode> gyldigEndringsInterval) {
        final List<Feil> feil = valider(ytelse, gyldigEndringsInterval);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(PleiepengerSyktBarn ytelse) {
        return verifyIngenFeil(ytelse, List.of());
    }

    public static List<Feil> verifyIngenFeil(PleiepengerSyktBarn ytelse, List<Periode> gyldigEndringsInterval) {
        final List<Feil> feil = valider(ytelse, gyldigEndringsInterval);
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

    public static List<Feil> valider(PleiepengerSyktBarn ytelse, List<Periode> gyldigEndringsInterval) {
        try {
            return validatorYtelse.valider(ytelse, gyldigEndringsInterval);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }

    }

}
