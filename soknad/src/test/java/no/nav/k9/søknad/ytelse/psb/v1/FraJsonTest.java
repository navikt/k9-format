package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ytelse.psb.SøknadJsonEksempel;

class FraJsonTest {

    private static final PleiepengerSyktBarnSøknadValidator validator = new PleiepengerSyktBarnSøknadValidator();

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.komplettSøknadJson();
        verifyIngenFeil(søknad);
    }

    @Test
    public void minimumSøknadSkalIkkeHaValideringsfeil() {
        var søknad = SøknadJsonEksempel.minimumSøknadJson();
        verifyIngenFeil(søknad);
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
        verifyIngenFeil(søknad, ((PleiepengerSyktBarn) søknad.getYtelse()).getUtledetEndringsperiode());
    }

}
