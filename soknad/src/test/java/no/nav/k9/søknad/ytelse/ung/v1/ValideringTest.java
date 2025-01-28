package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.ung.SøknadEksempel;
import no.nav.k9.søknad.ytelse.ung.YtelseEksempel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ValideringTest {

    @Test
    void verifiserHentingAvSøknadsperiodeUtenTomDatoIkkeFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode);
        ValiderUtil.verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

}
