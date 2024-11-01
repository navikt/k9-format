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
    void verifiserHentingAvSøknadsperiodeUtenTomDatoFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), null);
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, BigDecimal.valueOf(1000));
        ValiderUtil.verifyHarFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    void verifiserInntektUnderNullFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, BigDecimal.valueOf(-1000));
        ValiderUtil.verifyHarFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    void verifiserInntektOverGrenseFeiler() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now());
        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, BigDecimal.valueOf(1000001.00));
        ValiderUtil.verifyHarFeil(SøknadEksempel.søknad(ytelse));
    }
}
