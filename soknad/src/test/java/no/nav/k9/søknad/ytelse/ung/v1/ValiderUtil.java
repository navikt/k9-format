package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.pls.v1.PleiepengerLivetsSluttfaseSøknadValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ValiderUtil {

    private static final UngdomsytelseSøknadValidator validatorSøknad = new UngdomsytelseSøknadValidator();
    private static final UngdomsytelseInntektrapporteringValidator inntektrapporteringValidator = new UngdomsytelseInntektrapporteringValidator();


    public static List<Feil> verifyHarFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyHarFeil(UngdomsytelseInntektrapportering inntektrapportering) {
        final List<Feil> feil = valider(inntektrapportering);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyHarFeil(Søknad søknad, List<Periode> endringsperioder) {
        final List<Feil> feil = valider(søknad, endringsperioder);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(UngdomsytelseInntektrapportering inntektrapportering) {
        final List<Feil> feil = valider(inntektrapportering);
        assertThat(feil).isEmpty();
        return feil;
    }

    public static List<Feil> verifyIngenFeil(Søknad søknad, List<Periode> gyldigEndringsInterval) {
        final List<Feil> feil = valider(søknad, gyldigEndringsInterval);
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

    public static List<Feil> valider(UngdomsytelseInntektrapportering inntektrapportering) {
        try {
            return inntektrapporteringValidator.valider(inntektrapportering);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    public static List<Feil> valider(Søknad søknad, List<Periode> gyldigEndringsInterval) {
        try {
            return validatorSøknad.valider(søknad, gyldigEndringsInterval);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

}
