package no.nav.k9.søknad.ytelse.ung;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.ung.v1.Ungdomsytelse;

import java.math.BigDecimal;

public class YtelseEksempel {

    public static Ungdomsytelse komplettYtelseMedSøknadsperiode(Periode søknadsperiode, BigDecimal inntekt) {

        return new Ungdomsytelse()
                .medSøknadsperiode(søknadsperiode)
                .medInntekt(inntekt);
    }
}
