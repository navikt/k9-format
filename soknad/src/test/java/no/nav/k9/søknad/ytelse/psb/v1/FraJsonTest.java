package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.psb.SøknadJsonEksempel;

class FraJsonTest {

    private static final PleiepengerSyktBarnSøknadValidator validator = new PleiepengerSyktBarnSøknadValidator();


    private List<Feil> valider(Søknad søknad) {
        try {
            return validator.valider(søknad);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.komplettSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void minimumSøknadSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.minimumSøknadJson();
        final List<Feil> feil = valider(søknad);
        assertThat(feil).isEmpty();
    }

    @Test
    public void komplettSøknadGammelVersjonSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.komplettGammelVersjonSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void minimumSøknadGammelVersjonSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.minimumGammelVersjonSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void søknadMedEndringSkalIkkeHaFeil() {
        var søknad = SøknadJsonEksempel.søknadMedEndring();
        verifyIngenFeil(søknad);
    }

}
