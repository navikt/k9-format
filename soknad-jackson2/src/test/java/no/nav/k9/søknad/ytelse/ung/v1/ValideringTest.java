package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.ytelse.ung.SøknadEksempel;
import no.nav.k9.søknad.ytelse.ung.YtelseEksempel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ValideringTest {

    @Test
    void verifiserHentingAvSøknadsperiodeUtenTomDatoIkkeFeiler() {
        final var fom = LocalDate.now();
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(fom);
        ValiderUtil.verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

}
