package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.ung.SøknadEksempel;
import no.nav.k9.søknad.ytelse.ung.YtelseEksempel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class ValideringTest {

    @Test
    void verifiserHentingAvSøknadsperiodeUtenTomDatoIkkeFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, BigDecimal.valueOf(1000));
        ValiderUtil.verifyIngenFeil(SøknadEksempel.søknad(ytelse));
        Assertions.assertDoesNotThrow(ytelse::getSøknadsperiode);
    }

    @Test
    void verifiserInntektUnderNullFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, BigDecimal.valueOf(-1000));
        ValiderUtil.verifyHarFeil(SøknadEksempel.søknad(ytelse));
    }
}
