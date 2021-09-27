package no.nav.k9.søknad.ytelse.psb.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

public class ValiderUtil {

    private static final PleiepengerSyktBarnSøknadValidator validatorSøknad = new PleiepengerSyktBarnSøknadValidator();
    private static final PleiepengerSyktBarnYtelseValidator validatorYtelse = new PleiepengerSyktBarnYtelseValidator();


    public static List<Feil> verifyHarFeil(Søknad søknad) {
        return verifyHarFeil(søknad, List.of());
    }
    public static List<Feil> verifyHarFeil(Søknad søknad, List<Periode> gyldigEndringsInterval) {
        final List<Feil> feil = valider(søknad, gyldigEndringsInterval);
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
        return verifyIngenFeil(søknad, List.of());
    }

    public static List<Feil> verifyIngenFeil(Søknad søknad, List<Periode> gyldigEndringsInterval) {
        final List<Feil> feil = valider(søknad, gyldigEndringsInterval);
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

    public static List<Feil> valider(Søknad søknad, List<Periode> gyldigEndringsInterval) {
        try {
            return validatorSøknad.valider(søknad, gyldigEndringsInterval);
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
