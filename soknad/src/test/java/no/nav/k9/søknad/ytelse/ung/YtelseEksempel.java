package no.nav.k9.søknad.ytelse.ung;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.ung.v1.UngSøknadstype;
import no.nav.k9.søknad.ytelse.ung.v1.Ungdomsytelse;

import java.math.BigDecimal;

public class YtelseEksempel {

    public static Ungdomsytelse komplettYtelseMedSøknadsperiode(Periode søknadsperiode, BigDecimal inntekt, UngSøknadstype søknadstype) {

        return new Ungdomsytelse()
                .medSøknadsperiode(søknadsperiode)
                .medInntekt(inntekt)
                .medSøknadType(søknadstype);
    }
}
