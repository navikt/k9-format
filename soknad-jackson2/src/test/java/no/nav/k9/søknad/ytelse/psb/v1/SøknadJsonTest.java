package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ytelse.psb.SøknadJsonEksempel;

class SøknadJsonTest {


    @Disabled
    @Test
    public void søknadMedNullGirFeil() {
        var søknad = SøknadJsonEksempel.søknadMedNullFeil();

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "");
    }
}
